package com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.tier2;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class StuddedShield extends MeleeWeapon {

    {
        image = ItemSpriteSheet.STUDDEN_SHIELD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.9f;

        tier = 2;

        pierceDmg = 2;
        punchDmg = 7;
        stabCoef = 1f;
    }
    
}
