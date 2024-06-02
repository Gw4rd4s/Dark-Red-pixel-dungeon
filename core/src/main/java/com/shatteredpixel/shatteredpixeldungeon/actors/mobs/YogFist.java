/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FistSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public abstract class YogFist extends Mob {

	{
		HP = HT = 600;
		defenseSkill = 20;

		viewDistance = Light.DISTANCE;

		//for doomed resistance
		EXP = 25;
		maxLvl = -2;

		state = HUNTING;

		properties.add(Property.BOSS);
		properties.add(Property.DEMONIC);
	}

	private float rangedCooldown;
	protected boolean canRangedInMelee = true;

	protected void incrementRangedCooldown(){
		rangedCooldown += Random.NormalFloat(8, 12);
	}

	@Override
	protected boolean act() {
		if (paralysed <= 0 && rangedCooldown > 0) rangedCooldown--;

		if (Dungeon.hero.invisible <= 0 && state == WANDERING){
			beckon(Dungeon.hero.pos);
			state = HUNTING;
			enemy = Dungeon.hero;
		}

		return super.act();
	}

	@Override
	protected boolean canAttack(Char enemy) {
		if (rangedCooldown <= 0){
			return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
		} else {
			return super.canAttack(enemy);
		}
	}

	private boolean invulnWarned = false;

	protected boolean isNearYog(){
		int yogPos = Dungeon.level.exit() + 3*Dungeon.level.width();
		return Dungeon.level.distance(pos, yogPos) <= 4;
	}

	@Override
	public boolean isInvulnerable(Class effect) {
		if (isNearYog() && !invulnWarned){
			invulnWarned = true;
			GLog.w(Messages.get(this, "invuln_warn"));
		}
		return isNearYog() || super.isInvulnerable(effect);
	}

	@Override
	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.adjacent( pos, enemy.pos ) && (!canRangedInMelee || rangedCooldown > 0)) {

			return super.doAttack( enemy );

		} else {

			incrementRangedCooldown();
			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap( enemy.pos );
				return false;
			} else {
				zap();
				return true;
			}
		}
	}

	@Override
	public void damage(int piDmg, int puDmg, int fDmg, int wDmg, int vDmg, Object src) {
		int preHP = HP;
		super.damage(piDmg, puDmg, fDmg, wDmg, vDmg, src);
		int dmgTaken = HP - preHP;

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (dmgTaken > 0 && lock != null){
			if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))   lock.addTime(dmgTaken/4f);
			else                                                    lock.addTime(dmgTaken/2f);
		}
	}

	protected abstract void zap();

	public void onZapComplete(){
		zap();
		next();
	}

	@Override
	public int attackSkill( Char target ) {
		return 36;
	}

	{
		immunities.add( Sleep.class );
	}

	@Override
	public String description() {
		return Messages.get(YogFist.class, "desc") + "\n\n" + Messages.get(this, "desc");
	}

	public static final String RANGED_COOLDOWN = "ranged_cooldown";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(RANGED_COOLDOWN, rangedCooldown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		rangedCooldown = bundle.getFloat(RANGED_COOLDOWN);
	}

	public static class BurningFist extends YogFist {

		{
			spriteClass = FistSprite.Burning.class;

			properties.add(Property.FIERY);

			pierceDmg = 4;
			punchDmg = 4;
			fireDmg = 18;

			pierceArmor = 6;
			punchArmor = 7;
			fireArmor = 1500;
			waterArmor = -1200;
		}

		@Override
		public boolean act() {

			boolean result = super.act();

			if (Dungeon.level.map[pos] == Terrain.WATER){
				Level.set( pos, Terrain.EMPTY);
				GameScene.updateMap( pos );
				CellEmitter.get( pos ).burst( Speck.factory( Speck.STEAM ), 10 );
			}

			//1.67 evaporated tiles on average
			int evaporatedTiles = Random.chances(new float[]{0, 1, 2});

			for (int i = 0; i < evaporatedTiles; i++) {
				int cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
				if (Dungeon.level.map[cell] == Terrain.WATER){
					Level.set( cell, Terrain.EMPTY);
					GameScene.updateMap( cell );
					CellEmitter.get( cell ).burst( Speck.factory( Speck.STEAM ), 10 );
				}
			}

			for (int i : PathFinder.NEIGHBOURS9) {
				int vol = Fire.volumeAt(pos+i, Fire.class);
				if (vol < 4 && !Dungeon.level.water[pos + i] && !Dungeon.level.solid[pos + i]){
					GameScene.add( Blob.seed( pos + i, 4 - vol, Fire.class ) );
				}
			}

			return result;
		}

		@Override
		protected void zap() {
			spend( 1f );

			if (Dungeon.level.map[enemy.pos] == Terrain.WATER){
				Level.set( enemy.pos, Terrain.EMPTY);
				GameScene.updateMap( enemy.pos );
				CellEmitter.get( enemy.pos ).burst( Speck.factory( Speck.STEAM ), 10 );
			} else {
				Buff.affect( enemy, Burning.class ).reignite( enemy );
				enemy.damage(0,0,fireRoll(0)/2,0,0, Fire.class);
			}

			for (int i : PathFinder.NEIGHBOURS9){
				if (!Dungeon.level.water[enemy.pos+i] && !Dungeon.level.solid[enemy.pos+i]){
					int vol = Fire.volumeAt(enemy.pos+i, Fire.class);
					if (vol < 4){
						GameScene.add( Blob.seed( enemy.pos + i, 4 - vol, Fire.class ) );
					}
				}
			}

		}

		{
			immunities.add(Frost.class);

			resistances.add(StormCloud.class);
			resistances.add(GeyserTrap.class);
		}

	}

	public static class SoiledFist extends YogFist {

		{
			spriteClass = FistSprite.Soiled.class;
			punchDmg = 15;
			pierceDmg = 5;

			punchArmor = 8;
			pierceArmor = 10;
			fireArmor = 300;
			waterArmor = 180;
		}

		@Override
		public boolean act() {

			boolean result = super.act();
			int grassCells = 0;
			for (int i : PathFinder.NEIGHBOURS9) {
				if (Dungeon.level.map[pos+i] == Terrain.FURROWED_GRASS
						|| Dungeon.level.map[pos+i] == Terrain.HIGH_GRASS){
					grassCells++;
				}
			}
			//grass alter its elemental resistances and so physical
			this.pierceArmor = 8;
			this.punchArmor = 10;
			this.waterArmor = 180;
			this.fireArmor = 300;
			if (grassCells > 0){
				this.pierceArmor = 8 + grassCells * 10;
				this.punchArmor = 10 + grassCells * 12;
				this.waterArmor = 180 + grassCells * 120;
				this.fireArmor = 300 - grassCells * 90;
			}
			//1.33 grass tiles on average
			int furrowedTiles = Random.chances(new float[]{0, 2, 1});

			for (int i = 0; i < furrowedTiles; i++) {
				int cell = pos + PathFinder.NEIGHBOURS9[Random.Int(9)];
				if (Dungeon.level.map[cell] == Terrain.GRASS) {
					Level.set(cell, Terrain.FURROWED_GRASS);
					GameScene.updateMap(cell);
					CellEmitter.get(cell).burst(LeafParticle.GENERAL, 10);
				}
			}

			Dungeon.observe();

			for (int i : PathFinder.NEIGHBOURS9) {
				int cell = pos + i;
				if (canSpreadGrass(cell)){
					Level.set(pos+i, Terrain.GRASS);
					GameScene.updateMap( pos + i );
				}
			}

			return result;
		}

		@Override
		protected void zap() {
			spend( 1f );

			Invisibility.dispel(this);
			Char enemy = this.enemy;
			int dmg = GameMath.damageRoll(pierceDmg,pierceDmg,0) + GameMath.damageRoll(punchDmg, punchDmg,0);
			if (dmg > block( this, enemy)) {

				Buff.affect( enemy, Roots.class, 3f );

			} else {

				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}

			for (int i : PathFinder.NEIGHBOURS9){
				int cell = enemy.pos + i;
				if (canSpreadGrass(cell)){
					if (Random.Int(5) == 0){
						Level.set(cell, Terrain.FURROWED_GRASS);
						GameScene.updateMap( cell );
					} else {
						Level.set(cell, Terrain.GRASS);
						GameScene.updateMap( cell );
					}
					CellEmitter.get( cell ).burst( LeafParticle.GENERAL, 10 );
				}
			}
			Dungeon.observe();

		}

		private boolean canSpreadGrass(int cell){
			int yogPos = Dungeon.level.exit() + Dungeon.level.width()*3;
			return Dungeon.level.distance(cell, yogPos) > 4 && !Dungeon.level.solid[cell]
					&& !(Dungeon.level.map[cell] == Terrain.FURROWED_GRASS || Dungeon.level.map[cell] == Terrain.HIGH_GRASS);
		}

	}

	public static class RottingFist extends YogFist {

		{
			spriteClass = FistSprite.Rotting.class;

			properties.add(Property.ACIDIC);
			punchDmg = 8;
			pierceDmg = 5;
			venomDmg = 10;

			punchArmor = 10;
			pierceArmor = 5;

			venomArmor = 900;
			waterArmor = 150;
			fireArmor = -150;
		}

		@Override
		protected boolean act() {
			//ensures toxic gas acts at the appropriate time when added
			GameScene.add(Blob.seed(pos, 0, ToxicGas.class));

			if (Dungeon.level.water[pos] && HP < HT) {
				sprite.emitter().burst( Speck.factory(Speck.HEALING), 3 );
				HP += HT/50;
			}

			return super.act();
		}

		@Override
		public void damage(int piDmg, int puDmg, int fDmg, int wDmg, int vDmg, Object src) {
			if (!isInvulnerable(src.getClass())){
				super.damage(piDmg, puDmg, fDmg, wDmg, vDmg, src);
			}
		}

		@Override
		protected void zap() {
			spend( 1f );
			GameScene.add(Blob.seed(enemy.pos, 100, ToxicGas.class));
		}

		@Override
		public int attackProc( Char enemy, int damage ) {
			damage = super.attackProc( enemy, damage );

			if (Random.Int( 2 ) == 0) {
				Buff.affect( enemy, Ooze.class ).set( Ooze.DURATION );
				enemy.sprite.burst( 0xFF000000, 5 );
			}

			return damage;
		}

		{
			immunities.add(ToxicGas.class);
		}

	}

	public static class RustedFist extends YogFist {

		{
			spriteClass = FistSprite.Rusted.class;

			properties.add(Property.LARGE);
			properties.add(Property.INORGANIC);

			punchDmg = 15;
			pierceDmg = 8;
			venomDmg = 2;

			punchArmor = 5;
			pierceArmor = 15;

			venomArmor = 10000;
			waterArmor = -200;
			fireArmor = 180;
		}

		@Override
		public void damage(int piDmg, int puDmg, int fDmg, int wDmg, int vDmg, Object src) {
			if (!isInvulnerable(src.getClass()) && !(src instanceof Viscosity.DeferedDamage)){
				if (piDmg >= 0) {
					Buff.affect(this, Viscosity.DeferedDamage.class).prolong(piDmg/2);
					sprite.showStatus(CharSprite.WARNING, Messages.get(Viscosity.class, "deferred", piDmg/2));
					piDmg /= 4;
				}
				if (puDmg >= 0) {
					Buff.affect(this, Viscosity.DeferedDamage.class).prolong(puDmg);
					sprite.showStatus(CharSprite.WARNING, Messages.get(Viscosity.class, "deferred", puDmg));
					puDmg /= 2;
				}
				if (wDmg >= 0) {
					float waterCoef = GameMath.gate(-2f,GameMath.descendRatio(waterDefRoll(),HT), 1f);
					wDmg = - Math.round( wDmg * waterCoef);
					Buff.affect(this, Viscosity.DeferedDamage.class).prolong(wDmg);
					sprite.showStatus(CharSprite.WARNING, Messages.get(Viscosity.class, "deferred", wDmg));
				}
				//takes no venom damage and water damage is converted to defer damage
				super.damage(piDmg, puDmg, fDmg, 0, 0, src);
			}
		}

		@Override
		protected void zap() {
			spend( 1f );
			Buff.affect(enemy, Cripple.class, 4f);
		}

	}

	public static class BrightFist extends YogFist {

		{
			spriteClass = FistSprite.Bright.class;

			properties.add(Property.ELECTRIC);

			canRangedInMelee = false;
			punchDmg = 7;
			pierceDmg = 3;
			fireDmg = 10;

			punchArmor = 5;
			pierceArmor = 8;

			fireArmor = 300;
		}

		@Override
		protected void incrementRangedCooldown() {
			//ranged attack has no cooldown
		}

		//used so resistances can differentiate between melee and magical attacks
		public static class LightBeam{}

		@Override
		protected void zap() {
			spend( 1f );

			Invisibility.dispel(this);
			Char enemy = this.enemy;
			int dmg = GameMath.damageRoll(2*fireDmg,fireDmg,0);
			if (dmg > block( this, enemy)) {

				enemy.damage( 0,0, 3*fireRoll(0)/2,0,0, new LightBeam() );
				Buff.prolong( enemy, Blindness.class, Blindness.DURATION/2f );

				if (!enemy.isAlive() && enemy == Dungeon.hero) {
					Badges.validateDeathFromEnemyMagic();
					Dungeon.fail( this );
					GLog.n( Messages.get(Char.class, "kill", name()) );
				}

			} else {

				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}

		}

		@Override
		public void damage(int piDmg, int puDmg, int fDmg, int wDmg, int vDmg, Object src) {
			int beforeHP = HP;
			super.damage(piDmg, puDmg, fDmg, wDmg, vDmg, src);
			if (isAlive() && beforeHP > HT/2 && HP < HT/2){
				HP = HT/2;
				Buff.prolong( Dungeon.hero, Blindness.class, Blindness.DURATION*1.5f );
				int i;
				do {
					i = Random.Int(Dungeon.level.length());
				} while (Dungeon.level.heroFOV[i]
						|| Dungeon.level.solid[i]
						|| Actor.findChar(i) != null
						|| PathFinder.getStep(i, Dungeon.level.exit(), Dungeon.level.passable) == -1);
				ScrollOfTeleportation.appear(this, i);
				state = WANDERING;
				GameScene.flash(0x80FFFFFF);
				GLog.w( Messages.get( this, "teleport" ));
			} else if (!isAlive()){
				Buff.prolong( Dungeon.hero, Blindness.class, Blindness.DURATION*3f );
				GameScene.flash(0x80FFFFFF);
			}
		}

	}

	public static class DarkFist extends YogFist {

		{
			spriteClass = FistSprite.Dark.class;

			canRangedInMelee = false;

			punchDmg = 7;
			pierceDmg = 3;
			fireDmg = 5;
			waterDmg = 5;
			venomDmg = 5;

			punchArmor = 2;
			pierceArmor = 2;

			fireArmor = 420;
			waterArmor = 420;
			venomArmor = 420;
		}

		@Override
		protected void incrementRangedCooldown() {
			//ranged attack has no cooldown
		}

		//used so resistances can differentiate between melee and magical attacks
		public static class DarkBolt{}

		@Override
		protected void zap() {
			spend( 1f );

			Invisibility.dispel(this);
			Char enemy = this.enemy;
			//randomly choose 2 types of damage
			//types are not the same
			int idx1 = Random.IntRange(0,4);
			int idx2 = Random.IntRange(0,4);
			if(idx1 == idx2) idx2 = (idx1 + 1) % 5;

			int piDmg = 0;
			int puDmg = 0;
			int fDmg = 0;
			int wDmg = 0;
			int vDmg = 0;

			switch(idx1){
				case 0:
					piDmg = pierceRoll(0);
					break;
				case 1:
					puDmg = punchRoll(0);
					break;
				case 2:
					fDmg = fireRoll(0);
					break;
				case 3:
					wDmg = waterRoll(0);
					break;
				case 4:
					vDmg = venomRoll(0);
					break;
			}
			switch(idx2){
				case 0:
					piDmg = pierceRoll(0);
					break;
				case 1:
					puDmg = punchRoll(0);
					break;
				case 2:
					fDmg = fireRoll(0);
					break;
				case 3:
					wDmg = waterRoll(0);
					break;
				case 4:
					vDmg = venomRoll(0);
					break;
			}
			int dmg = piDmg + puDmg +fDmg + wDmg + vDmg;
			if (dmg > block( this, enemy)) {

				enemy.damage( piDmg, puDmg, fDmg, wDmg, vDmg, new DarkBolt() );

				Light l = enemy.buff(Light.class);
				if (l != null){
					l.weaken(50);
				}

				if (!enemy.isAlive() && enemy == Dungeon.hero) {
					Badges.validateDeathFromEnemyMagic();
					Dungeon.fail( this );
					GLog.n( Messages.get(Char.class, "kill", name()) );
				}

			} else {

				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}

		}

		@Override
		public void damage(int piDmg, int puDmg, int fDmg, int wDmg, int vDmg, Object src) {
			int beforeHP = HP;
			super.damage(piDmg, puDmg, fDmg, wDmg, vDmg, src);
			if (isAlive() && beforeHP > HT/2 && HP < HT/2){
				HP = HT/2;
				Light l = Dungeon.hero.buff(Light.class);
				if (l != null){
					l.detach();
				}
				int i;
				do {
					i = Random.Int(Dungeon.level.length());
				} while (Dungeon.level.heroFOV[i]
						|| Dungeon.level.solid[i]
						|| Actor.findChar(i) != null
						|| PathFinder.getStep(i, Dungeon.level.exit(), Dungeon.level.passable) == -1);
				ScrollOfTeleportation.appear(this, i);
				state = WANDERING;
				GameScene.flash(0, false);
				GLog.w( Messages.get( this, "teleport" ));
			} else if (!isAlive()){
				Light l = Dungeon.hero.buff(Light.class);
				if (l != null){
					l.detach();
				}
				Buff.affect(Dungeon.hero, Doom.class);
				GameScene.flash(0, false);
			}
		}

	}

}
