package com.shatteredpixel.shatteredpixeldungeon.items.weapon.ranged;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Bow extends RangedWeapon {

    {
        image = ItemSpriteSheet.TOY_BOW;
        tier = 1;
    }

    @Override
    public int wepTier() {
        return tier; //tier 1
    }




}