package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Trident extends MeleeWeapon{
    {
        image = ItemSpriteSheet.TRIDENT;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1f;

        tier = 4;
        RCH = 2;
        pierceDmg = 20;
        punchDmg = 2;
    }
}
