package com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.tier5.tier4;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Trident extends MeleeWeapon {
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
