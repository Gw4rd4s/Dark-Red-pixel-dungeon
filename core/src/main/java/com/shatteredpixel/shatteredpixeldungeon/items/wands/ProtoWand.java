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

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ScrollEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ItemStatusHandler;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public abstract class ProtoWand extends Weapon {

	public static final String AC_ZAP	= "ZAP";

	private static final float TIME_TO_ZAP	= 1f;
	public int charge;
	
	protected Charger charger;
	
	public boolean curChargeKnown = false;
	
	public boolean curseInfusionBonus = false;

	private static final int USES_TO_ID = 10;
	private float usesLeftToID = USES_TO_ID;
	private float availableUsesToID = USES_TO_ID/2f;

	protected int collisionProperties = Ballistica.MAGIC_BOLT;
	
	{
		defaultAction = AC_ZAP;
		usesTargeting = true;
		bones = true;
	}

	public int tier;
	//Merge those arrays bellow, IF ward can live with another character on the same cell
	protected Ward[] wards = new Ward[5];//memorised wards: for quick removal
	protected Point[] points = new Point[5];//coors of wards in dungeon grid
	protected int pointCnt;
	protected float spellQuality;
	//==============================================================================
	//randomized textures tier by tier
	//randomized textures tier by tier

	//variable sprites of wands of the element from tier 2 to tier 5
	protected int[] images = new int[4];
	//variable names of wands of the element from tier 2 to tier 5
	protected String[] names = new String[4];
	protected static ItemStatusHandler<ProtoWand> handler2;
	protected static ItemStatusHandler<ProtoWand> handler3;
	protected static ItemStatusHandler<ProtoWand> handler4;
	protected static ItemStatusHandler<ProtoWand> handler5;

	private static final LinkedHashMap<String, Integer> tier2 = new LinkedHashMap<String, Integer>() {
		{
			put("silver_knuckels",ItemSpriteSheet.SILVER_KNUCKELS);
			put("golden_knuckels",ItemSpriteSheet.GOLDEN_KEY);
			put("golden_bracelet",ItemSpriteSheet.GOLDEN_BRACELET);
			put("silver_bracelet",ItemSpriteSheet.SILVER_BRACELET);
			put("pixie_dust",ItemSpriteSheet.PIXIE_DUST_POUCH);
			put("uncut_opal",ItemSpriteSheet.UNCUT_OPAL);
			put("uranite",ItemSpriteSheet.URANITE_ORE);
			put("blue_flower",ItemSpriteSheet.BLUE_FLOWER);
		}
	};
	private static final LinkedHashMap<String, Integer> tier3 = new LinkedHashMap<String, Integer>() {
		{
			put("purple_wand",ItemSpriteSheet.PURPLE_WAND);
			put("willow_wand",ItemSpriteSheet.WILLOW_WAND);
			put("obsidian_wand",ItemSpriteSheet.OBSIDIAN_WAND);
			put("prismatic_wand",ItemSpriteSheet.PRISMATIC_WAND);
			put("ashen_wand",ItemSpriteSheet.ASHEN_WAND);
			put("oak_wand",ItemSpriteSheet.OAK_WAND);
			put("yellow_wand",ItemSpriteSheet.YELLOW_WAND);
			put("painted_wand",ItemSpriteSheet.PAINTED_WAND);
		}
	};
	private static final LinkedHashMap<String, Integer> tier4 = new LinkedHashMap<String, Integer>() {
		{
			put("red_staff",ItemSpriteSheet.RED_STAFF);
			put("blue_staff",ItemSpriteSheet.BLUE_STAFF);
			put("metal_staff",ItemSpriteSheet.METAL_STAFF);
			put("root_staff",ItemSpriteSheet.ROOT_STAFF);
			put("magenta_staff",ItemSpriteSheet.MAGENTA_STAFF);
			put("marble_staff",ItemSpriteSheet.MARBLE_STAFF);
			put("skull_staff",ItemSpriteSheet.SKULL_STAFF);
			put("green_staff",ItemSpriteSheet.GREEN_STAFF);
		}
	};
	private static final LinkedHashMap<String, Integer> tier5 = new LinkedHashMap<String, Integer>() {
		{
			put("rune_blade",ItemSpriteSheet.RUNE_BLADE);
			put("crusade_shield",ItemSpriteSheet.CRUSADE_SHIELD);
			put("scythe",ItemSpriteSheet.SCYTHE);
			put("platin_hammer",ItemSpriteSheet.PLATINUM_HAMMER);
			put("tomahawk",ItemSpriteSheet.TOMAHAWK);
			put("pitchfork",ItemSpriteSheet.PITCHFORK);
			put("shining_staff",ItemSpriteSheet.SHINING_STAFF);
			put("eye_pyramid",ItemSpriteSheet.EYE_PYRAMID);
		}
	};

	public static void initElements() {
		handler2 = new ItemStatusHandler<>( (Class<? extends ProtoWand>[]) Generator.Category.PROTOWAND.classes, tier2 );
		handler3 = new ItemStatusHandler<>( (Class<? extends ProtoWand>[]) Generator.Category.PROTOWAND.classes, tier3 );
		handler4 = new ItemStatusHandler<>( (Class<? extends ProtoWand>[]) Generator.Category.PROTOWAND.classes, tier4 );
		handler5 = new ItemStatusHandler<>( (Class<? extends ProtoWand>[]) Generator.Category.PROTOWAND.classes, tier5 );
	}

	//==============================================================================
	private static final String USES_LEFT_TO_ID     = "uses_left_to_id";
	private static final String AVAILABLE_USES      = "available_uses";
	private static final String CUR_CHARGE          = "curCharges";
	private static final String CUR_CHARGE_KNOWN    = "curChargeKnown";
	private static final String CURSE_INFUSION_BONUS= "curse_infusion_bonus";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( USES_LEFT_TO_ID, usesLeftToID );
		bundle.put( AVAILABLE_USES, availableUsesToID );
		bundle.put( CUR_CHARGE, charge );
		bundle.put( CUR_CHARGE_KNOWN, curChargeKnown );
		bundle.put( CURSE_INFUSION_BONUS, curseInfusionBonus );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		usesLeftToID = bundle.getInt( USES_LEFT_TO_ID );
		availableUsesToID = bundle.getInt( AVAILABLE_USES );
		curseInfusionBonus = bundle.getBoolean(CURSE_INFUSION_BONUS);

		charge = bundle.getInt( CUR_CHARGE );
		curChargeKnown = bundle.getBoolean( CUR_CHARGE_KNOWN );
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_ZAP );

		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_ZAP )) {
			
			curUser = hero;
			curItem = this;
			GameScene.selectCell( zapper );
			
		}
	}

	@Override
	public int targetingPos(Hero user, int dst) {
		return new Ballistica( user.pos, dst, collisionProperties ).collisionPos;
	}

	public void onZap(Ballistica bolt){
		int target = bolt.collisionPos;

		//hit wall: do nothing
		if (!Dungeon.level.passable[target]){
			GLog.w( Messages.get(this, "bad_location"));
			Dungeon.level.pressCell(target);
			return;
		}
		//hit character: cast spell
		Char ch = Actor.findChar(target);
		if (ch != null){
			points[pointCnt] = new Point(target % Dungeon.level.width(), target / Dungeon.level.width() );

			tryCastSpell();
			return;
		}

		//hit empty cell: spawn ward
		Ward ward = new Ward();
		ward.pos = target;
		GameScene.add(ward, 1f);
		Dungeon.level.occupyCell(ward);
		ward.sprite.emitter().burst(MagicMissile.WardParticle.UP, 2);
		Dungeon.level.pressCell(target);

		wards[pointCnt] = ward;
		points[pointCnt] = new Point(target % Dungeon.level.width(), target / Dungeon.level.width() );
		pointCnt++;

		//spawned 5th ward: cast the spell
		if(pointCnt >= 5){
			tryCastSpell();
		}
	}

	public abstract void onHit(Char attacker, Char defender, int damage);

	//not affected by enchantment proc chance changers
	public static float procChanceMultiplier( Char attacker ){
		if (attacker.buff(Talent.EmpoweredStrikeTracker.class) != null){
			return 1f + ((Hero)attacker).pointsInTalent(Talent.EMPOWERED_STRIKE)/2f;
		}
		return 1f;
	}

	public boolean tryToZap( Hero owner, int target ){

		if (owner.buff(MagicImmune.class) != null){
			GLog.w( Messages.get(this, "no_magic") );
			return false;
		}

		if ( charge >= chargesPerCast()){
			return true;
		} else {
			GLog.w(Messages.get(this, "fizzles"));
			return false;
		}
	}

	@Override
	public boolean collect( Bag container ) {
		if (super.collect( container )) {
			if (container.owner != null) {
				if (container instanceof MagicalHolster)
					charge( container.owner, ((MagicalHolster) container).HOLSTER_SCALE_FACTOR );
				else
					charge( container.owner );
			}
			return true;
		} else {
			return false;
		}
	}

	public void gainCharge( int amt ){
		gainCharge( amt, false );
	}

	public void gainCharge( int amt, boolean overcharge ){
		if (overcharge) charge = Math.min(100+amt, charge+1);
		else charge = Math.min(100, charge+1);
		updateQuickslot();
	}
	
	public void charge( Char owner ) {
		if (charger == null) charger = new Charger();
		charger.attachTo( owner );
	}

	public void charge( Char owner, float chargeScaleFactor ){
		charge( owner );
		charger.setScaleFactor( chargeScaleFactor );
	}

	protected void wandProc(Char target, int chargesUsed){
		wandProc(target, buffedLvl(), chargesUsed);
	}

	//TODO Consider externalizing char awareness buff
	protected static void wandProc(Char target, int wandLevel, int chargesUsed){
		if (Dungeon.hero.hasTalent(Talent.ARCANE_VISION)) {
			int dur = 5 + 5*Dungeon.hero.pointsInTalent(Talent.ARCANE_VISION);
			Buff.append(Dungeon.hero, TalismanOfForesight.CharAwareness.class, dur).charID = target.id();
		}

		if (target != Dungeon.hero &&
				Dungeon.hero.subClass == HeroSubClass.WARLOCK &&
				//standard 1 - 0.92^x chance, plus 7%. Starts at 15%
				Random.Float() > (Math.pow(0.92f, (wandLevel*chargesUsed)+1) - 0.07f)){
			SoulMark.prolong(target, SoulMark.class, SoulMark.DURATION + wandLevel);
		}
	}

	@Override
	public void onDetach( ) {
		stopCharging();
	}

	public void stopCharging() {
		if (charger != null) {
			charger.detach();
			charger = null;
		}
	}
	
	@Override
	public Item identify( boolean byHero ) {
		
		curChargeKnown = true;
		super.identify(byHero);
		
		updateQuickslot();
		
		return this;
	}
	
	public void onHeroGainExp( float levelPercent, Hero hero ){
		levelPercent *= Talent.itemIDSpeedFactor(hero, this);
		if (!isIdentified() && availableUsesToID <= USES_TO_ID/2f) {
			//gains enough uses to ID over 1 level
			availableUsesToID = Math.min(USES_TO_ID/2f, availableUsesToID + levelPercent * USES_TO_ID/2f);
		}
	}

	@Override
	public String info() {
		String desc = desc();

		desc += "\n\n" + statsDesc();

		if (cursed && cursedKnown) {
			desc += "\n\n" + Messages.get(ProtoWand.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			desc += "\n\n" + Messages.get(ProtoWand.class, "not_cursed");
		}

		if (Dungeon.hero.subClass == HeroSubClass.BATTLEMAGE){
			desc += "\n\n" + Messages.get(this, "bmage_desc");
		}

		return desc;
	}

	public String statsDesc(){
		return Messages.get(this, "stats_desc");
	}
	
	@Override
	public boolean isIdentified() {
		return super.isIdentified() && curChargeKnown;
	}
	
	@Override
	public String status() {
		if (levelKnown) {
			return (curChargeKnown ? charge : "?") + "/" + 100f;
		} else {
			return null;
		}
	}
	
	@Override
	public int level() {
		if (!cursed && curseInfusionBonus){
			curseInfusionBonus = false;
		}
		int level = super.level();
		if (curseInfusionBonus) level += 1 + level/6;
		return level;
	}
	
	@Override
	public Item upgrade() {

		super.upgrade();

		if (Random.Int(3) == 0) {
			cursed = false;
		}
		updateQuickslot();
		
		return this;
	}
	
	@Override
	public Item degrade() {
		super.degrade();

		updateQuickslot();
		
		return this;
	}

	@Override
	public int buffedLvl() {
		int lvl = super.buffedLvl();

		if (charger != null && charger.target != null) {
			if (charger.target.buff(WildMagic.WildMagicTracker.class) != null){
				int bonus = 4 + ((Hero)charger.target).pointsInTalent(Talent.WILD_POWER);
				if (Random.Int(2) == 0) bonus++;
				bonus /= 2; // +2/+2.5/+3/+3.5/+4 at 0/1/2/3/4 talent points

				int maxBonusLevel = 3 + ((Hero)charger.target).pointsInTalent(Talent.WILD_POWER);
				if (lvl < maxBonusLevel) {
					lvl = Math.min(lvl + bonus, maxBonusLevel);
				}
			}

			if (charger.target.buff(ScrollEmpower.class) != null){
				lvl += 3;
			}

			WandOfMagicMissile.MagicCharge buff = charger.target.buff(WandOfMagicMissile.MagicCharge.class);
			if (buff != null && buff.level() > lvl){
				return buff.level();
			}
		}
		return lvl;
	}

	protected int chargesPerCast() {
		return 1;
	}
	
	public void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.MAGIC_MISSILE,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	public void staffFx( MagesStaff.StaffParticle particle ){
		//exists only to be overriden
	}

	protected void wandUsed() {
		if (!isIdentified()) {
			float uses = Math.min( availableUsesToID, Talent.itemIDSpeedFactor(Dungeon.hero, this) );
			availableUsesToID -= uses;
			usesLeftToID -= uses;
			if (usesLeftToID <= 0 || Dungeon.hero.pointsInTalent(Talent.SCHOLARS_INTUITION) == 2) {
				identify();
				GLog.p( Messages.get(ProtoWand.class, "identify") );
				Badges.validateItemLevelAquired( this );
			}
		}

		//inside staff
		if (charger != null && charger.target == Dungeon.hero && !Dungeon.hero.belongings.contains(this)){
			if (Dungeon.hero.hasTalent(Talent.EXCESS_CHARGE) && charge >= 100f){
				Buff.affect(Dungeon.hero, Barrier.class).setShield(Math.round(buffedLvl()*0.67f*Dungeon.hero.pointsInTalent(Talent.EXCESS_CHARGE)));
			}
		}
		
		charge -= chargesPerCast();

		//If hero owns wand but it isn't in belongings it must be in the staff
		if (Dungeon.hero.hasTalent(Talent.EMPOWERED_STRIKE)
				&& charger != null && charger.target == Dungeon.hero
				&& !Dungeon.hero.belongings.contains(this)){

			Buff.prolong(Dungeon.hero, Talent.EmpoweredStrikeTracker.class, 10f);

		}
		Invisibility.dispel();
		updateQuickslot();

		curUser.spendAndNext( TIME_TO_ZAP );
	}
	
	@Override
	public Item random() {
		//+0: 66.67% (2/3)
		//+1: 26.67% (4/15)
		//+2: 6.67%  (1/15)
		int n = 0;
		if (Random.Int(3) == 0) {
			n++;
			if (Random.Int(5) == 0){
				n++;
			}
		}
		level(n);
		
		//30% chance to be cursed
		if (Random.Float() < 0.3f) {
			cursed = true;
		}

		return this;
	}

	@Override
	public int value() {
		int price = 75;
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level() > 0) {
				price *= (level() + 1);
			} else if (level() < 0) {
				price /= (1 - level());
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}
	
	@Override
	public void reset() {
		super.reset();
		usesLeftToID = USES_TO_ID;
		availableUsesToID = USES_TO_ID/2f;
	}

	public int collisionProperties(int target) {
		if (cursed || !Dungeon.level.heroFOV[target])   return Ballistica.PROJECTILE;
		else                                            return Ballistica.STOP_TARGET;
	}

	/**
	 * Checking ward pattern.
	 * Calculating angles, and distances between wards
	 * Then it decide what case this is
	 * @return ID spell id
	 * <p>attribute <b>spellQuality</b> is used as 2nd output</p>
	 */
	protected int shapeRecognition(){
		if (pointCnt == 1) {
			//tier 1 spell
			return 1;
		}
		float[] angles = new float[4];
		if(pointCnt == 2){
			//tier 2 spell
			//special case when measuring angle with respect to caster

			//wards are too far away from each other
			if(Point.distance1(points[0], points[1]) > 2){
				return 0;//spell fails
			}

			//FIXME make this work for MOBs too
			Point pCaster = new Point(curUser.pos % Dungeon.level.width(), curUser.pos / Dungeon.level.width());
			angles[0] = Point.angle(points[1],points[0]) - Point.angle(pCaster, points[0]);
			angles[0] = Math.abs(angles[0]);
			//special case when taking into account caster position
			return angleQuality(angles);
			//do Not go further from this case OR game will break!
		}

		//2nd output
		spellQuality = 1;
		//assume distance of all wards is 2
		boolean isDist1 = true;//normal power
		boolean tooFar = false;
		for(int idp = 1; idp < pointCnt; idp++){
			//at least 1 distance is 1 => weaker spell
			isDist1 = isDist1 && ( Point.distance1(points[idp], points[idp-1]) == 1);
			//wards are too far from each other (cell dist 3+)
			tooFar = tooFar || (Point.distance1(points[idp], points[idp-1]) > 2);
		}
		//in case last zap is on character
		//ward is not created but max distance check is still performed
		if(points[pointCnt] != null){
			tooFar = tooFar || (Point.distance1(points[pointCnt], points[pointCnt-1]) > 2);
		}
		//gaps between wards are too wide
		if(tooFar){
			//spell fails
			return 0;
		}
		//weaken spell
		if(isDist1){
			spellQuality = 0.5f;
		}
		for( int idp = 1; idp < pointCnt; idp++){
			angles[idp-1] = Point.angle(points[idp+1],points[idp]) - Point.angle(points[idp-1], points[idp]);
		}
		return angleQuality(angles);
	}

	/**
	 * Based on input angles, this evaluates spell shape
	 * @param angles FLOAT[] angles of spell pattern
	 * @return ID spell key in spell list
	 */
	protected int angleQuality(float[] angles){
		if(pointCnt <= 1){
			//brain-dead-proof check
			return 0;
		}
		//special case of only 2 wards
		if (pointCnt == 2) {
			//no line formed
			if( Math.PI * 0.8 > angles[0]){
				return 0; //spell failed
			}
			//2nd output of this function
			spellQuality = 1;//spell power modifier
			//wards are too close (dist = 1)
			if(Point.distance1(points[0], points[1]) == 1){
				spellQuality = 0.5f; //reduced spell power
			}
			return 2;
		}

		//checking for straight line consisting of 3, 4 and 5 wards
		//-----------------------------------------------------------------------
		boolean isLine = true;
		for(int ida = 0; ida<pointCnt-2;ida++) {
			//				straight line has angle=PI or 180 deg
			isLine = isLine && (Math.PI * 0.8f < angles[ida] && Math.PI * 1.2f > angles[ida]);
		}
		if(isLine){
			return pointCnt;
		}

		//testing for regular shapes
		//--------------------------------------------------------------------------
		//triangle: angle is PI/3 or 60 deg
		//rectangle: angle is PI/2 or 90 deg
		// pentagon: angle is 2PI/3 or 120 deg
		float idealAng = (float) (Math.PI / 3 + (pointCnt-3) * Math.PI / 6);
		boolean isShape = true;
		for(int ida = 0; ida<pointCnt-2;ida++) {
			//		regular angle +- 20%        not bellow            not above
			isShape = isShape && (idealAng * 0.8f < angles[ida] && angles[ida] < idealAng * 1.2f);
		}
		if(isShape){
			return pointCnt + 3;
		}

		//nothing found: spell failed
		return 0;
	}

	/**
	 * Tries to cast spell: checking spell pattern and distances between wards
	 */
	protected void tryCastSpell(){
		int out = shapeRecognition();
		//kill wards casting this spell
		int idw = 0;
		while(wards[idw] != null && idw < 5){
			wards[idw].destroy();
			//fixme possibly not necessary, but I'm not sure
			wards[idw] = null;
			idw++;
		}
		onSpellCast(out);
	}

	/**
	 * Search spell list and cast the spell. List is implemented in child classes.
	 * For each element, spell differ
	 * @param spellId ID of spell
	 */
	protected abstract void onSpellCast(int spellId);


	public static class PlaceHolder extends ProtoWand {

		{
			image = ItemSpriteSheet.WAND_HOLDER;
		}

		@Override
		public boolean isSimilar(Item item) {
			return item instanceof ProtoWand;
		}

		public void onHit(Char attacker, Char defender, int damage) {}
		protected void onSpellCast(int spellId){}

		@Override
		public String info() {
			return "";
		}
	}
	
	protected static CellSelector.Listener zapper = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			
			if (target != null) {
				
				//FIXME this safety check shouldn't be necessary
				//it would be better to eliminate the curItem static variable.
				final ProtoWand curWand;
				if (curItem instanceof ProtoWand) {
					curWand = (ProtoWand) ProtoWand.curItem;
				} else {
					return;
				}

				final Ballistica shot = new Ballistica( curUser.pos, target, curWand.collisionProperties(target));
				int cell = shot.collisionPos;
				
				if (target == curUser.pos || cell == curUser.pos) {
					if (target == curUser.pos && curUser.hasTalent(Talent.SHIELD_BATTERY)){

						if (curUser.buff(MagicImmune.class) != null){
							GLog.w( Messages.get(ProtoWand.class, "no_magic") );
							return;
						}

						if (curWand.charge <= 0){
							GLog.w( Messages.get(ProtoWand.class, "fizzles") );
							return;
						}

						float shield = curUser.HT * (0.04f*curWand.charge);
						if (curUser.pointsInTalent(Talent.SHIELD_BATTERY) == 2) shield *= 1.5f;
						Buff.affect(curUser, Barrier.class).setShield(Math.round(shield));
						curWand.charge = 0;
						curUser.sprite.operate(curUser.pos);
						Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
						ScrollOfRecharging.charge(curUser);
						updateQuickslot();
						curUser.spendAndNext(Actor.TICK);
						return;
					}
					GLog.i( Messages.get(ProtoWand.class, "self_target") );
					return;
				}

				curUser.sprite.zap(cell);

				//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
				if (Actor.findChar(target) != null)
					QuickSlotButton.target(Actor.findChar(target));
				else
					QuickSlotButton.target(Actor.findChar(cell));
				
				if (curWand.tryToZap(curUser, target)) {
					
					curUser.busy();
					
					if (curWand.cursed){
						if (!curWand.cursedKnown){
							GLog.n(Messages.get(ProtoWand.class, "curse_discover", curWand.name()));
						}
						CursedWand.cursedZap(curWand,
								curUser,
								new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT),
								new Callback() {
									@Override
									public void call() {
										curWand.wandUsed();
									}
								});
					} else {
						curWand.fx(shot, new Callback() {
							public void call() {
								curWand.onZap(shot);
								curWand.wandUsed();
							}
						});
					}
					curWand.cursedKnown = true;
					
				}
				
			}
		}
		
		@Override
		public String prompt() {
			return Messages.get(ProtoWand.class, "prompt");
		}
	};
	
	public class Charger extends Buff {
		
		private static final float BASE_CHARGE_DELAY = 10f;
		private static final float SCALING_CHARGE_ADDITION = 40f;
		private static final float NORMAL_SCALE_FACTOR = 0.875f;

		float scalingFactor = NORMAL_SCALE_FACTOR;

		@Override
		public boolean attachTo( Char target ) {
			if (super.attachTo( target )) {
				//if we're loading in and the hero has partially spent a turn, delay for 1 turn
				if (target instanceof Hero && Dungeon.hero == null && cooldown() == 0 && target.cooldown() > 0) {
					spend(TICK);
				}
				return true;
			}
			return false;
		}
		
		@Override
		public boolean act() {
			if (charge < 100 && target.buff(MagicImmune.class) == null)
				recharge();
			spend( TICK );
			return true;
		}

		private void recharge(){
			float missingCharges = 100 - charge;
			missingCharges = Math.max(0, missingCharges);

			float turnsToCharge = (float) (BASE_CHARGE_DELAY
					+ (SCALING_CHARGE_ADDITION * Math.pow(scalingFactor, missingCharges)));

			LockedFloor lock = target.buff(LockedFloor.class);
		}
		
		public ProtoWand wand(){
			return ProtoWand.this;
		}

		public void gainCharge(){
			if (charge < 100) {
				charge++;
				charge = Math.min(charge, 100);
				updateQuickslot();
			}
		}

		private void setScaleFactor(float value){
			this.scalingFactor = value;
		}
	}
}
