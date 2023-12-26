package com.shatteredpixel.shatteredpixeldungeon.items.weapons.ranged;

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