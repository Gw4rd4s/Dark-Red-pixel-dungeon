package com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.tier5.tier4;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Kukri extends MeleeWeapon {
    {
        image = ItemSpriteSheet.KUKRI;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        tier = 4;

        pierceDmg = 18;
        punchDmg = 7;
        slashCoefs[0] = 1f;
        slashCoefs[1] = 0.8f;
        stabCoef = 0.5f;
    }
}
