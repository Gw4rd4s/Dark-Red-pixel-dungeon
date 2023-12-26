package com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.tier5;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MorningStar extends MeleeWeapon {
    {
        image = ItemSpriteSheet.MORNINGSTAR;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 5;

        pierceDmg = 6;
        punchDmg = 26;
        slashCoefs[0] = 1f;
        slashCoefs[1] = 0.8f;
        stabCoef = 0.4f;
    }
}
