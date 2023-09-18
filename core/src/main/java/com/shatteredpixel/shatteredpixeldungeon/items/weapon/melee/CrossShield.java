package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class CrossShield extends MeleeWeapon{
    {
        image = ItemSpriteSheet.CROSS_SHIELD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.9f;

        tier = 4;

        pierceDmg = 3;
        punchDmg = 15;
    }
}
