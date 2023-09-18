package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class Ammo extends Weapon {
    //public static final String AC_SHOOT		= "SHOOT";

    {
        bones = true;
        //defaultAction = AC_SHOOT;
        //usesTargeting = true;
    }

    public boolean holster;

    protected int tier;

    public int wepTier(){
        return tier;
    }

    /**
     * Calculates minimal damage
     * @param lvl weapon lvl
     * @return min dmg possible
     */
    public int min(int lvl){
        return tier +//base
                lvl; //scaling
    }

    /**
     * Calculates maximal damage
     * @param lvl weapon lvl
     * @return max dmg possible
     */
    public int max(int lvl){
        return 5*(tier+1) + //base
                lvl*(tier+1);//scaling
    }
    @Override
    public Item upgrade() {
            return super.upgrade();
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.remove( AC_EQUIP );
        return actions;
    }

    @Override
    public boolean collect(Bag container) {
        if (container instanceof MagicalHolster) holster = true;
        return super.collect(container);
    }

    @Override
    public int throwPos(Hero user, int dst) {
            return super.throwPos(user, dst);
    }


    @Override
    public Item random() {
        //not stackable
        return this;
    }

    //TODO Remove
    public String status() {
        //show quantity even when it is 1
        return Integer.toString( quantity );
    }


    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        return super.doPickUp(hero, pos);
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public String info() {

        String info = desc();

        info += "\n\n" + Messages.get( MissileWeapon.class, "stats",
                tier,
                Math.round(augment.damageFactor(0)),
                Math.round(augment.damageFactor(1)),
                STRReq());

        if (STRReq() > Dungeon.hero.lvl) {
            info += " " + Messages.get(Weapon.class, "too_heavy");
        } else if (Dungeon.hero.lvl > STRReq()){
            info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.lvl - STRReq());
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped( Dungeon.hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        info += "\n\n" + Messages.get(MissileWeapon.class, "distance");

        info += "\n\n" + Messages.get(this, "durability");


        info += " " + Messages.get(this, "unlimited_uses");

        return info;
    }

    @Override
    public int value() {
        return 20 * tier * (level() + 1);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
    }


    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
    }

}
