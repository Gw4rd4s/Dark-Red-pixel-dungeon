package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Halberd extends MeleeWeapon{
    {
        image = ItemSpriteSheet.HALBERD;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        tier = 3;

        pierceDmg = 12;
        punchDmg = 12;
        slashCoefs[0] = 1f;
        slashCoefs[1] = 0.8f;
        stabCoef = 0.4f;
    }
}
