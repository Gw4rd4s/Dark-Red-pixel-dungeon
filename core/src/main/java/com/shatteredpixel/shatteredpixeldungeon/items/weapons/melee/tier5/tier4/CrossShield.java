package com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.tier5.tier4;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class CrossShield extends MeleeWeapon {
    {
        image = ItemSpriteSheet.CROSS_SHIELD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.9f;

        tier = 4;

        pierceDmg = 3;
        punchDmg = 15;
    }
}
