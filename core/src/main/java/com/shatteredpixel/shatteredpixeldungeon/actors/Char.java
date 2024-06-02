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

package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LifeLink;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SnipersMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Speed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Potential;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public abstract class Char extends Actor {
	
	public int pos = 0;
	
	public CharSprite sprite;
	/**Max HP*/
	public int HT;
	/**Current HP*/
	public int HP;
	/**piercing type of damage*/
	protected int pierceDmg;
	/**punching type of damage*/
	protected int punchDmg;
	/**fire, burning, hot type of damage*/
	protected int fireDmg;
	/**cold, chilling, freezing type of damage*/
	protected int waterDmg;
	/**toxic, poisonous, venomous type of damage*/
	protected int venomDmg;
	/**armor against piercing*/
	protected int pierceArmor;
	/**armor against punching*/
	protected int punchArmor;
	/**responses to fire, water and venom dmg
	 * response = total armor / max health, min -2, max 1
	 * +100% healing, -100% damaging, -200% twice damaging*/
	protected int fireArmor;
	protected int waterArmor;
	protected int venomArmor;
	/**attacking power: degrades stamina, replacement for accuracy*/
	protected int attackSkill;
	/**blocking count down: number of turns before another block*/
	protected int blockCD;
	/**number of additional blocks in blocking turn*/
	protected int blockPersists;
	protected float baseSpeed	= 1;
	protected PathFinder.Path path;

	public int paralysed	    = 0;
	public boolean rooted		= false;
	public boolean flying		= false;
	public int invisible		= 0;

	//these are relative to the hero
	public enum Alignment{
		ENEMY,
		NEUTRAL,
		ALLY
	}
	public Alignment alignment;
	
	public int viewDistance	= 8;
	
	public boolean[] fieldOfView = null;
	
	private LinkedHashSet<Buff> buffs = new LinkedHashSet<>();

	@Override
	protected boolean act() {
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( this, fieldOfView );

		//throw any items that are on top of an immovable char
		if (properties().contains(Property.IMMOVABLE)){
			throwItems();
		}
		blockCD = Math.max(blockCD--, 0);
		return false;
	}

	protected void throwItems(){
		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null && heap.type == Heap.Type.HEAP) {
			int n;
			do {
				n = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			} while (!Dungeon.level.passable[n] && !Dungeon.level.avoid[n]);
			Item item = heap.peek();
			if (!(item instanceof Tengu.BombAbility.BombItem || item instanceof Tengu.ShockerAbility.ShockerItem)){
				Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( pos );
			}
		}
	}

	public String name(){
		return Messages.get(this, "name");
	}

	public boolean canInteract(Char c){
		if (Dungeon.level.adjacent( pos, c.pos )){
			return true;
		} else if (c instanceof Hero
				&& alignment == Alignment.ALLY
				&& Dungeon.level.distance(pos, c.pos) <= 2*Dungeon.hero.pointsInTalent(Talent.ALLY_WARP)){
			return true;
		} else {
			return false;
		}
	}
	//TODO: This returns TRUE only
	//swaps places by default
	public boolean interact(Char c){

		//don't allow char to swap onto hazard unless they're flying
		//you can swap onto a hazard though, as you're not the one instigating the swap
		if (!Dungeon.level.passable[pos] && !c.flying){
			return true;
		}

		//can't swap into a space without room
		if (properties().contains(Property.LARGE) && !Dungeon.level.openSpace[c.pos]
			|| c.properties().contains(Property.LARGE) && !Dungeon.level.openSpace[pos]){
			return true;
		}

		//we do a little raw position shuffling here so that the characters are never
		// on the same cell when logic such as occupyCell() is triggered
		int oldPos = pos;
		int newPos = c.pos;

		//warp instantly with allies in this case
		if (c == Dungeon.hero && Dungeon.hero.hasTalent(Talent.ALLY_WARP)){
			PathFinder.buildDistanceMap(c.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
			if (PathFinder.distance[pos] == Integer.MAX_VALUE){
				return true;
			}
			pos = newPos;
			c.pos = oldPos;
			ScrollOfTeleportation.appear(this, newPos);
			ScrollOfTeleportation.appear(c, oldPos);
			Dungeon.observe();
			GameScene.updateFog();
			return true;
		}

		//can't swap places if one char has restricted movement
		if (rooted || c.rooted || buff(Vertigo.class) != null || c.buff(Vertigo.class) != null){
			return true;
		}

		c.pos = oldPos;
		moveSprite( oldPos, newPos );
		move( newPos );

		c.pos = newPos;
		c.sprite.move( newPos, oldPos );
		c.move( oldPos );
		
		c.spend( 1 / c.speed() );

		if (c == Dungeon.hero){

			Dungeon.hero.busy();
		}
		
		return true;
	}
	//TODO: This returns TRUE only
	protected boolean moveSprite( int from, int to ) {
		
		if (sprite.isVisible() && (Dungeon.level.heroFOV[from] || Dungeon.level.heroFOV[to])) {
			sprite.move( from, to );
			return true;
		} else {
			sprite.turnTo(from, to);
			sprite.place( to );
			return true;
		}
	}

	public void hitSound( float pitch ){
		Sample.INSTANCE.play(Assets.Sounds.HIT, 1, pitch);
	}

	public boolean blockSound( float pitch ) {
		return false;
	}
	
	protected static final String POS       = "pos";
	protected static final String TAG_HP    = "HP";
	protected static final String TAG_HT    = "HT";
	protected static final String TAG_SHLD  = "SHLD";
	protected static final String BUFFS	    = "buffs";
	protected static final String PIERCE_DMG = "pierceDmg";
	protected static final String PUNCH_DMG = "punchDmg";
	protected static final String FIRE_DMG = "fireDmg";
	protected static final String WATER_DMG = "waterDmg";
	protected static final String POISON_DMG = "poisonDmg";
	protected static final String PIERCE_ARMOR = "pierceArmor";
	protected static final String PUNCH_ARMOR = "punchArmor";
	protected static final String FIRE_ARMOR = "fireCoef";
	protected static final String WATER_ARMOR = "waterCoef";
	protected static final String VENOM_ARMOR = "poisonCoef";
	protected static final String BLOCKCD = "blockCD";
	protected static final String BLOCKPERSISTS = "blockPersists";

	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );
		
		bundle.put( POS, pos );
		bundle.put( TAG_HP, HP );
		bundle.put( TAG_HT, HT );
		bundle.put( BUFFS, buffs );
		bundle.put( PIERCE_DMG, pierceDmg);
		bundle.put( PUNCH_DMG, punchDmg);
		bundle.put( FIRE_DMG, fireDmg);
		bundle.put( WATER_DMG, waterDmg);
		bundle.put( POISON_DMG, venomDmg);
		bundle.put( PIERCE_ARMOR, pierceArmor);
		bundle.put( PUNCH_ARMOR, punchArmor);
		bundle.put( FIRE_ARMOR, fireArmor);
		bundle.put( WATER_ARMOR, waterArmor);
		bundle.put( VENOM_ARMOR, venomArmor);
		bundle.put( BLOCKCD, blockCD);
		bundle.put( BLOCKPERSISTS, blockPersists);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );
		
		pos = bundle.getInt( POS );
		HP = bundle.getInt( TAG_HP );
		HT = bundle.getInt( TAG_HT );
		pierceDmg = bundle.getInt(PIERCE_DMG);
		punchDmg = bundle.getInt(PUNCH_DMG);
		fireDmg = bundle.getInt(FIRE_DMG);
		waterDmg = bundle.getInt(WATER_DMG);
		venomDmg = bundle.getInt(POISON_DMG);
		pierceArmor = bundle.getInt(PIERCE_ARMOR);
		punchArmor = bundle.getInt(PUNCH_ARMOR);
		fireArmor = bundle.getInt(FIRE_ARMOR);
		waterArmor = bundle.getInt(WATER_ARMOR);
		venomArmor = bundle.getInt(VENOM_ARMOR);
		blockCD = bundle.getInt(BLOCKCD);
		blockPersists = bundle.getInt(BLOCKPERSISTS);

		for (Bundlable b : bundle.getCollection( BUFFS )) {
			if (b != null) {
				((Buff)b).attachTo( this );
			}
		}
	}

	final public boolean attack( Char enemy, float dmgMulti ){
		return attack(enemy, dmgMulti, 1f, 1f);
	}
	
	public boolean attack( Char enemy, float dmgMulti, float accMulti, float critBonus ) {

		if (enemy == null) return false;
		
		boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];

		//enemy is invulnerable: no damage
		if (enemy.isInvulnerable(getClass())) {
			if (visibleFight) {
				enemy.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "invulnerable") );
				Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1f, Random.Float(0.96f, 1.05f));
			}
			return false;
		}
		//interrupt if hero gets hit
		if (this instanceof Hero && ((Hero) this).damageInterrupt){
			((Hero) this).interrupt();
		}
			float tempMulti = 1f;

			if (enemy.buff(ScrollOfChallenge.ChallengeArena.class) != null){
				tempMulti *= 0.67f;
			}

			if ( buff(Weakness.class) != null ){
				tempMulti *= 0.67f;
			}

			int piDmg = Math.round(tempMulti * pierceRoll(critBonus) );
			int puDmg = Math.round(tempMulti * punchRoll(critBonus) );
			int fDmg = Math.round(tempMulti * fireRoll(critBonus) );
			int wDmg = Math.round(tempMulti * waterRoll(critBonus) );
			int vDmg = Math.round(tempMulti * venomRoll(critBonus) );

			//TODO: Forgets return value: breaks glyph of stone, wand of living earth...
			enemy.defenseProc( this, piDmg + puDmg + fDmg + wDmg + vDmg);
			//todo: Forgets return value: this breaks a lot of stuff
			attackProc( enemy, piDmg + puDmg + fDmg + wDmg + vDmg );

			// If the enemy is already dead, interrupt the attack.
			// This matters as defence procs can sometimes inflict self-damage, such as armor glyphs.
			if (!enemy.isAlive()){
				return true;
			}
			int blckPwr = enemy.block( this, enemy);
			//damage was blocked completely
			if ( (piDmg+puDmg+fDmg+wDmg+vDmg) <= blckPwr ) {
				if (visibleFight) {
					//ToDo: add blocking animation
				//TODO enemy.defenseSound? currently miss plays for monks/crab even when they parry
				Sample.INSTANCE.play(Assets.Sounds.MISS);
				}
				return false;
			}
			//armor & blocking: subtracting
			piDmg -= enemy.pierceDefRoll() + blckPwr;
			puDmg -= enemy.punchDefRoll() + blckPwr;
			//blocking: subtracting
			fDmg -= blckPwr;
			wDmg -= blckPwr;
			vDmg -= blckPwr;
			//physical damage can not be negative
			piDmg = Math.max( piDmg, 0 );
			puDmg = Math.max( puDmg, 0 );
			//and so elemental damage can not be negative from blocking
			fDmg = Math.max( fDmg, 0);
			wDmg = Math.max( wDmg, 0);
			vDmg = Math.max( vDmg, 0);
			enemy.damage(piDmg, puDmg, fDmg, fDmg, vDmg, this );

			if (buff(FireImbue.class) != null)  buff(FireImbue.class).proc(enemy);
			if (buff(FrostImbue.class) != null) buff(FrostImbue.class).proc(enemy);

			enemy.sprite.bloodBurstA( sprite.center(), piDmg + puDmg + fDmg + wDmg + vDmg );
			enemy.sprite.flash();
			//enemy dead check
			if (!enemy.isAlive() && visibleFight) {
				if (enemy == Dungeon.hero) {
					
					if (this == Dungeon.hero) {
						return true;
					}

					if (this instanceof WandOfLivingEarth.EarthGuardian
							|| this instanceof MirrorImage || this instanceof PrismaticImage){
						Badges.validateDeathFromFriendlyMagic();
					}
					Dungeon.fail( this );
					GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
					
				} else if (this == Dungeon.hero) {
					GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name())) );
				}
			}

			if(blckPwr > Math.round(this.HT / 10f)){
				//todo add blocking animation if block more than 10% hp
			}
			return true;
	}

	public static int INFINITE_ACCURACY = 1_000_000;
	public static int INFINITE_EVASION = 1_000_000;

	public int block( Char attacker, Char defender) {
		float blck = 0;
		//invisible chars never blocks (for the hero this is surprise attacking)
		//todo add surprise to monsters!
		if (attacker.invisible > 0 && attacker.canSurpriseAttack()) return -1;
		// last turns of cold down = no blocking
		if( defender.blockCD < 4) return 0;

		//getting blocking values
		if(defender instanceof Hero && ((Hero) defender).belongings.weapon != null) blck += ((Hero) defender).belongings.weapon.blocking;
		if(defender instanceof Mob && ((Mob) defender).weaponL() != null) blck += ((Mob) defender).weaponL().blocking;
		if(defender instanceof Mob && ((Mob) defender).weaponR() != null) blck += ((Mob) defender).weaponR().blocking;

		//natural decaying
		blck *= ( defender.blockCD /4f - 1);

		if (attacker.buff(Bless.class) != null) blck *= 0.8f;
		if (attacker.buff(  Hex.class) != null) blck *= 1.25f;
		if (attacker.buff( Daze.class) != null) blck *= 2f;
		for (ChampionEnemy buff : attacker.buffs(ChampionEnemy.class)){
			blck *= buff.evasionAndAccuracyFactor();
		}
		blck *= AscensionChallenge.statModifier(attacker);
		//same as attacker code above: but inverted numbers
		if (defender.buff(Bless.class) != null) blck *= 1.25f;
		if (defender.buff(  Hex.class) != null) blck *= 0.8f;
		if (defender.buff( Daze.class) != null) blck *= 0.5f;
		for (ChampionEnemy buff : defender.buffs(ChampionEnemy.class)){
			blck *= buff.evasionAndAccuracyFactor();
		}
		blck *= AscensionChallenge.statModifier(defender);

		return Math.round(blck);
	}

	public int attackSkill( Char target ) {
		return 0;
	}
	
	public int defenseSkill( Char enemy ) {
		return 0;
	}
	
	public String defenseVerb() {
		return Messages.get(this, "def_verb");
	}

	/**
	 * Roll of piercing damage
	 * @param critBonus critical bonus to this type of damage
	 * @return INT actual damage roll
	 */
	public int pierceRoll(float critBonus){
		return GameMath.damageRoll(pierceDmg, pierceDmg, critBonus);
	}
	/**
	 * Roll of punching damage
	 * @param critBonus critical bonus to this type of damage
	 * @return INT actual damage roll
	 */
	public int punchRoll(float critBonus){
		return GameMath.damageRoll(punchDmg, punchDmg, critBonus);
	}
	/**
	 * Roll of burn damage
	 * @param critBonus critical bonus to this type of damage
	 * @return INT actual damage roll
	 */
	public int fireRoll(float critBonus){
		return GameMath.damageRoll(fireDmg, fireDmg, critBonus);
	}
	/**
	 * Roll of frost damage
	 * @param critBonus critical bonus to this type of damage
	 * @return INT actual damage roll
	 */
	public int waterRoll(float critBonus){
		return GameMath.damageRoll(waterDmg, waterDmg, critBonus);
	}
	/**
	 * Roll of poison damage
	 * @param critBonus critical bonus to this type of damage
	 * @return INT actual damage roll
	 */
	public int venomRoll(float critBonus){
		return GameMath.damageRoll(venomDmg, venomDmg, critBonus);
	}
	//TODO: bark skin buff is not used. Add this here!
	/**
	 * Roll of piercing protection
	 * @return INT actual damage roll
	 */
	public int pierceDefRoll(){
		return GameMath.damageRoll(pierceArmor, pierceArmor, 0);
	}
	/**
	 * Roll of punching protection
	 * @return INT actual damage roll
	 */
	public int punchDefRoll(){
		return GameMath.damageRoll(punchArmor, punchArmor, 0);
	}
	/**
	 * Roll of fire protection
	 * @return INT actual damage roll
	 */
	public int fireDefRoll(){return GameMath.damageRoll(fireArmor, fireArmor,0);}
	/**
	 * Roll of water protection
	 * @return INT actual damage roll
	 */
	public int waterDefRoll(){return GameMath.damageRoll(waterArmor, waterArmor,0);}
	/**
	 * Roll of venom protection
	 * @return INT actual damage roll
	 */
	public int venomDefRoll(){return GameMath.damageRoll(venomArmor, venomArmor,0);}
	//TODO it would be nice to have a pre-armor and post-armor proc.
	// atm attack is always post-armor and defence is already pre-armor
	
	public int attackProc( Char enemy, int damage ) {
		return damage;
	}
	
	public int defenseProc( Char enemy, int damage ) {

		Earthroot.Armor armor = buff( Earthroot.Armor.class );
		if (armor != null) {
			damage = armor.absorb( damage );
		}

		return damage;
	}
	
	public float speed() {
		float speed = baseSpeed;
		if ( buff( Cripple.class ) != null ) speed /= 2f;
		if ( buff( Stamina.class ) != null) speed *= 1.5f;
		if ( buff( Adrenaline.class ) != null) speed *= 2f;
		if ( buff( Haste.class ) != null) speed *= 3f;
		if ( buff( Dread.class ) != null) speed *= 2f;
		return speed;
	}

	//currently only used by invisible chars, or by the hero
	public boolean canSurpriseAttack(){
		return true;
	}
	
	//used so that buffs(Shieldbuff.class) isn't called every time unnecessarily
	private int cachedShield = 0;
	public boolean needsShieldUpdate = true;
	
	public int shielding(){
		if (!needsShieldUpdate){
			return cachedShield;
		}
		
		cachedShield = 0;
		for (ShieldBuff s : buffs(ShieldBuff.class)){
			cachedShield += s.shielding();
		}
		needsShieldUpdate = false;
		return cachedShield;
	}

	/**
	 * Apply damage to this character
	 * @param src Special argument: Used for Life Link buff and type of damage
	 */
	public void damage(int piDmg, int puDmg, int fDmg, int wDmg, int vDmg, Object src ) {
		//dead or no damage
		if (!isAlive() ||  (piDmg < 0 && puDmg < 0) ) {
			return;
		}

		if(isInvulnerable(src.getClass())){
			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
			return;
		}

		//Damage spread through life link
		if (!(src instanceof LifeLink) && buff(LifeLink.class) != null){
			HashSet<LifeLink> links = buffs(LifeLink.class);
			for (LifeLink link : links.toArray(new LifeLink[0])){
				if (Actor.findById(link.object) == null){
					links.remove(link);
					link.detach();
				}
			}
			piDmg = (int)Math.ceil(piDmg / (float)(links.size()+1));
			puDmg = (int)Math.ceil(puDmg / (float)(links.size()+1));
			fDmg = (int)Math.ceil(fDmg / (float)(links.size()+1));
			wDmg = (int)Math.ceil(wDmg / (float)(links.size()+1));
			vDmg = (int)Math.ceil(vDmg / (float)(links.size()+1));
			for (LifeLink link : links){
				Char ch = (Char)Actor.findById(link.object);
				if (ch != null) {
					ch.damage(piDmg, puDmg, fDmg, wDmg, vDmg, link);
					if (HP <= 0) {//dead
						link.detach();
					}
				}
			}
		}
		float fireCoef = GameMath.gate(-1f,GameMath.descendRatio(fireDefRoll(),HT), 2f);
		float waterCoef = GameMath.gate(-1f,GameMath.descendRatio(waterDefRoll(),HT), 2f);
		float venomCoef = GameMath.gate(-1f,GameMath.descendRatio(venomDefRoll(),HT), 2f);

		//detach buffs
		Terror t = buff(Terror.class);
		if (t != null){
			t.recover();
		}
		Dread d = buff(Dread.class);
		if (d != null){
			d.recover();
		}
		Charm c = buff(Charm.class);
		if (c != null){
			c.recover(src);
		}
		if (this.buff(Frost.class) != null){
			Buff.detach( this, Frost.class );
		}
		if (this.buff(MagicalSleep.class) != null){
			Buff.detach(this, MagicalSleep.class);
		}
		if (this.buff(Doom.class) != null && !isImmune(Doom.class)){
			piDmg = 3 * piDmg / 2;
			puDmg = 3 * puDmg / 2;
			//elemental damage is affected only when it harms this character.
			if(fireCoef < 0) fDmg = 3 * fDmg / 2;
			if(waterCoef < 0) wDmg = 3 * wDmg / 2;
			if(venomCoef < 0) vDmg = 3 * vDmg / 2;
		}

		fDmg = Math.round( fireCoef * fDmg );
		wDmg = Math.round( waterCoef * wDmg );
		vDmg = Math.round( venomCoef * vDmg );
		if (buff( Paralysis.class ) != null) {
			buff( Paralysis.class ).processDamage(piDmg + puDmg + fDmg + wDmg + vDmg);
		}

		int HPdiff = HP;
		HP -= (piDmg + puDmg + fDmg + wDmg + vDmg);
		HP = Math.min(HT, HP);
		HPdiff = HP - HPdiff;

		if (HP < 0 && src instanceof Char && alignment == Alignment.ENEMY){
			if (((Char) src).buff(Kinetic.KineticTracker.class) != null){
				int dmgToAdd = -HP;
				dmgToAdd -= ((Char) src).buff(Kinetic.KineticTracker.class).conservedDamage;
				dmgToAdd = Math.round(dmgToAdd * Weapon.Enchantment.genericProcChanceMultiplier((Char) src));
				if (dmgToAdd > 0) {
					Buff.affect((Char) src, Kinetic.ConservedDamage.class).setBonus(dmgToAdd);
				}
				((Char) src).buff(Kinetic.KineticTracker.class).detach();
			}
		}
		
		if (sprite != null) {
			String dText = "";
			if(piDmg > 0) dText = "^" + piDmg + " ";
			if(puDmg > 0) dText = dText + "&" + puDmg + " ";
			//damaging elemental power in this section only, healing in next
			if(fDmg > 0) dText = dText + fDmg + "# ";
			if(wDmg > 0) dText = dText + wDmg + "~ ";
			if(vDmg > 0) dText = dText + vDmg + "@";
			//elemental healing, if any
			if(fDmg < 0) dText = dText + "+" + Integer.toString(fDmg).substring(1) + "# ";
			if(wDmg < 0) dText = dText + "+" + Integer.toString(wDmg).substring(1) + "~ ";
			if(vDmg < 0) dText = dText + "+" + Integer.toString(vDmg).substring(1) + "@";

			//largest damage type determines the display color
			int color = CharSprite.NEGATIVE;//default or piercing
			if( puDmg >= piDmg && puDmg >= fDmg && puDmg >= wDmg && puDmg >= vDmg) color = CharSprite.PUNCHING;
			if( fDmg >= piDmg && fDmg >= puDmg && fDmg >= wDmg && fDmg >= vDmg) color = CharSprite.WARNING;
			if( wDmg >= piDmg && wDmg >= puDmg && wDmg >= fDmg && wDmg >= vDmg) color = CharSprite.WATER;
			if( vDmg >= piDmg && vDmg >= puDmg && vDmg >= fDmg && vDmg >= wDmg) color = CharSprite.POISON;
			if(HPdiff > 0) color = CharSprite.POSITIVE;//healing if health gets greater

			if(dText.isEmpty() || HPdiff == 0) {//no damage or Character has max health and taking healing effect
				sprite.showStatus(CharSprite.NEUTRAL, "0");
			}else {
				sprite.showStatus(color, dText);
				//todo: add healing effect
			}
		}

		HP = Math.max(0, HP); //no negative health

		if (!isAlive()) {
			die( src );
		}
	}

	/**
	 * Direct damage, no armor or resistances, only invulnerability
	 * @param dmg damage to deal
	 * @param src source of damage or life link
	 */
	public void damage(int dmg, Object src ) {
		//dead or no damage
		if (HP <= 0 || dmg > 0) {
			return;
		}

		if(isInvulnerable(src.getClass())){
			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
			return;
		}

		//Damage spread through life link
		if (!(src instanceof LifeLink) && buff(LifeLink.class) != null){
			HashSet<LifeLink> links = buffs(LifeLink.class);
			for (LifeLink link : links.toArray(new LifeLink[0])){
				if (Actor.findById(link.object) == null){
					links.remove(link);
					link.detach();
				}
			}
			dmg = (int)Math.ceil(dmg / (float)(links.size()+1));
			for (LifeLink link : links){
				Char ch = (Char)Actor.findById(link.object);
				if (ch != null) {
					ch.damage(dmg, link);
					if (HP <= 0) {//dead
						link.detach();
					}
				}
			}
		}
		//detach buffs
		Terror t = buff(Terror.class);
		if (t != null){
			t.recover();
		}
		Dread d = buff(Dread.class);
		if (d != null){
			d.recover();
		}
		Charm c = buff(Charm.class);
		if (c != null){
			c.recover(src);
		}
		if (this.buff(Frost.class) != null){
			Buff.detach( this, Frost.class );
		}
		if (this.buff(MagicalSleep.class) != null){
			Buff.detach(this, MagicalSleep.class);
		}
		if (this.buff(Doom.class) != null && !isImmune(Doom.class)){
			dmg *= 1.67f;
		}

		if (buff( Paralysis.class ) != null) {
			buff( Paralysis.class ).processDamage(dmg);
		}

		HP -= dmg;

		if (sprite != null) {
			sprite.showStatus(CharSprite.DEEP,Integer.toString(dmg));
		}

		HP = Math.max(0, HP); //no negative health

		if (!isAlive()) {
			die( src );
		}
	}

	public void destroy() {
		HP = 0;
		Actor.remove( this );

		for (Char ch : Actor.chars().toArray(new Char[0])){
			if (ch.buff(Charm.class) != null && ch.buff(Charm.class).object == id()){
				ch.buff(Charm.class).detach();
			}
			if (ch.buff(Dread.class) != null && ch.buff(Dread.class).object == id()){
				ch.buff(Dread.class).detach();
			}
			if (ch.buff(Terror.class) != null && ch.buff(Terror.class).object == id()){
				ch.buff(Terror.class).detach();
			}
			if (ch.buff(SnipersMark.class) != null && ch.buff(SnipersMark.class).object == id()){
				ch.buff(SnipersMark.class).detach();
			}
			if (ch.buff(Talent.FollowupStrikeTracker.class) != null
					&& ch.buff(Talent.FollowupStrikeTracker.class).object == id()){
				ch.buff(Talent.FollowupStrikeTracker.class).detach();
			}
			if (ch.buff(Talent.DeadlyFollowupTracker.class) != null
					&& ch.buff(Talent.DeadlyFollowupTracker.class).object == id()){
				ch.buff(Talent.DeadlyFollowupTracker.class).detach();
			}
		}
	}
	
	public void die( Object src ) {
		destroy();
		if (src != Chasm.class) sprite.die();
	}

	public boolean isAlive() {
		return HP > 0;
	}

	public boolean isActive() {
		return isAlive();
	}

	@Override
	protected void spendConstant(float time) {
		TimekeepersHourglass.timeFreeze freeze = buff(TimekeepersHourglass.timeFreeze.class);
		if (freeze != null) {
			freeze.processTime(time);
			return;
		}

		Swiftthistle.TimeBubble bubble = buff(Swiftthistle.TimeBubble.class);
		if (bubble != null){
			bubble.processTime(time);
			return;
		}

		super.spendConstant(time);
	}

	@Override
	protected void spend( float time ) {

		float timeScale = 1f;
		if (buff( Slow.class ) != null) {
			timeScale *= 0.5f;
			//slowed and chilled do not stack
		} else if (buff( Chill.class ) != null) {
			timeScale *= buff( Chill.class ).speedFactor();
		}
		if (buff( Speed.class ) != null) {
			timeScale *= 2.0f;
		}
		
		super.spend( time / timeScale );
	}
	
	public synchronized LinkedHashSet<Buff> buffs() {
		return new LinkedHashSet<>(buffs);
	}
	
	@SuppressWarnings("unchecked")
	//returns all buffs assignable from the given buff class
	public synchronized <T extends Buff> HashSet<T> buffs( Class<T> c ) {
		HashSet<T> filtered = new HashSet<>();
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				filtered.add( (T)b );
			}
		}
		return filtered;
	}

	@SuppressWarnings("unchecked")
	//returns an instance of the specific buff class, if it exists. Not just assignable
	public synchronized  <T extends Buff> T buff( Class<T> c ) {
		for (Buff b : buffs) {
			if (b.getClass() == c) {
				return (T)b;
			}
		}
		return null;
	}

	public synchronized boolean isCharmedBy( Char ch ) {
		int chID = ch.id();
		for (Buff b : buffs) {
			if (b instanceof Charm && ((Charm)b).object == chID) {
				return true;
			}
		}
		return false;
	}

	public synchronized boolean add( Buff buff ) {

		if (buff(PotionOfCleansing.Cleanse.class) != null) { //cleansing buff
			if (buff.type == Buff.buffType.NEGATIVE
					&& !(buff instanceof AllyBuff)
					&& !(buff instanceof LostInventory)){
				return false;
			}
		}

		if (sprite != null && buff(Challenge.SpectatorFreeze.class) != null){
			return false; //can't add buffs while frozen and game is loaded
		}

		buffs.add( buff );
		if (Actor.chars().contains(this)) Actor.add( buff );

		if (sprite != null && buff.announced) {
			switch (buff.type) {
				case POSITIVE:
					sprite.showStatus(CharSprite.POSITIVE, Messages.titleCase(buff.name()));
					break;
				case NEGATIVE:
					sprite.showStatus(CharSprite.NEGATIVE, Messages.titleCase(buff.name()));
					break;
				case NEUTRAL:
				default:
					sprite.showStatus(CharSprite.NEUTRAL, Messages.titleCase(buff.name()));
					break;
			}
		}

		return true;

	}
	
	public synchronized boolean remove( Buff buff ) {
		
		buffs.remove( buff );
		Actor.remove( buff );

		return true;
	}
	
	public synchronized void remove( Class<? extends Buff> buffClass ) {
		for (Buff buff : buffs( buffClass )) {
			remove( buff );
		}
	}
	
	@Override
	protected synchronized void onRemove() {
		for (Buff buff : buffs.toArray(new Buff[buffs.size()])) {
			buff.detach();
		}
	}
	
	public synchronized void updateSpriteState() {
		for (Buff buff:buffs) {
			buff.fx( true );
		}
	}
	
	public float stealth() {
		return 0;
	}

	/**
	 * Movement of actor to given position only 1 tile away!
	 * @param step target position, code should make sure it is adjacent (distance = 1)!
	 */
	public final void move( int step ) {
		move( step, true );
	}

	//travelling may be false when a character is moving instantaneously, such as via teleportation
	public void move( int step, boolean travelling ) {
		//Vertigo debuff takes effect
		if (travelling && Dungeon.level.adjacent( step, pos ) && buff( Vertigo.class ) != null) {
			sprite.interruptMotion();
			//int newPos = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			int newPos = 2*pos - step;//TESTING this should send Char to opposite direction
			//some checking
			if (!(Dungeon.level.passable[newPos] || Dungeon.level.avoid[newPos])
					|| (properties().contains(Property.LARGE) && !Dungeon.level.openSpace[newPos])
					|| Actor.findChar(newPos) != null){
				return; //abort if this is true
			}
				sprite.move(pos, newPos, 0.1f);//handling this here could cause issues for slash
				step = newPos;
		}

		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}

		pos = step;
		
		if (this != Dungeon.hero) {
			sprite.visible = Dungeon.level.heroFOV[pos];
		}

		Dungeon.level.occupyCell(this );
	}
	
	public int distance( Char other ) {
		return Dungeon.level.distance( pos, other.pos );
	}
	
	public void onMotionComplete() {
		//Does nothing by default
		//The main actor thread already accounts for motion,
		// so calling next() here isn't necessary (see Actor.process)
	}
	
	public void onAttackComplete() {
		next();
	}
	
	public void onOperateComplete() {
		next();
	}
	
	protected final HashSet<Class> resistances = new HashSet<>();
	
	//returns percent effectiveness after resistances
	//TODO currently resistances reduce effectiveness by a static 50%, and do not stack.
	public float resist( Class effect ){
		HashSet<Class> resists = new HashSet<>(resistances);
		for (Property p : properties()){
			resists.addAll(p.resistances());
		}
		for (Buff b : buffs()){
			resists.addAll(b.resistances());
		}
		
		float result = 1f;
		for (Class c : resists){
			if (c.isAssignableFrom(effect)){
				result *= 0.5f;
			}
		}
		return result * RingOfElements.resist(this, effect);
	}
	
	protected final HashSet<Class> immunities = new HashSet<>();
	
	public boolean isImmune(Class effect ){
		HashSet<Class> immunes = new HashSet<>(immunities);
		for (Property p : properties()){
			immunes.addAll(p.immunities());
		}
		for (Buff b : buffs()){
			immunes.addAll(b.immunities());
		}
		
		for (Class c : immunes){
			if (c.isAssignableFrom(effect)){
				return true;
			}
		}
		return false;
	}

	//similar to isImmune, but only factors in damage.
	//Is used in AI decision-making
	public boolean isInvulnerable( Class effect ){
		return buff(Challenge.SpectatorFreeze.class) != null;
	}

	protected HashSet<Property> properties = new HashSet<>();

	public HashSet<Property> properties() {
		HashSet<Property> props = new HashSet<>(properties);
		//TODO any more of these and we should make it a property of the buff, like with resistances/immunities
		if (buff(ChampionEnemy.Giant.class) != null) {
			props.add(Property.LARGE);
		}
		return props;
	}

	public enum Property{
		BOSS ( new HashSet<Class>( Arrays.asList(Grim.class, GrimTrap.class, ScrollOfRetribution.class, ScrollOfPsionicBlast.class)),
				new HashSet<Class>( Arrays.asList(AllyBuff.class, Dread.class) )),
		MINIBOSS ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(AllyBuff.class, Dread.class) )),
		BOSS_MINION,
		UNDEAD,
		DEMONIC,
		INORGANIC ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(Bleeding.class, ToxicGas.class, Poison.class) )),
		FIERY ( new HashSet<Class>( Arrays.asList(WandOfFireblast.class, Elemental.FireElemental.class)),
				new HashSet<Class>( Arrays.asList(Burning.class, Blazing.class))),
		ICY ( new HashSet<Class>( Arrays.asList(WandOfFrost.class, Elemental.FrostElemental.class)),
				new HashSet<Class>( Arrays.asList(Frost.class, Chill.class))),
		ACIDIC ( new HashSet<Class>( Arrays.asList(Corrosion.class)),
				new HashSet<Class>( Arrays.asList(Ooze.class))),
		ELECTRIC ( new HashSet<Class>( Arrays.asList(WandOfLightning.class, Shocking.class, Potential.class, Electricity.class, Elemental.ShockElemental.class )),
				new HashSet<Class>()),
		LARGE,
		IMMOVABLE;
		
		private HashSet<Class> resistances;
		private HashSet<Class> immunities;
		
		Property(){
			this(new HashSet<Class>(), new HashSet<Class>());
		}
		
		Property( HashSet<Class> resistances, HashSet<Class> immunities){
			this.resistances = resistances;
			this.immunities = immunities;
		}
		
		public HashSet<Class> resistances(){
			return new HashSet<>(resistances);
		}
		
		public HashSet<Class> immunities(){
			return new HashSet<>(immunities);
		}

	}

	public static boolean hasProp( Char ch, Property p){
		return (ch != null && ch.properties().contains(p));
	}
}
