package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class LightWand extends ProtoWand{

    @Override
    public void reset(){
        super.reset();
        //tier 2
        if (handler2 != null && handler2.contains(this)) {
            images[0] = handler2.image(this);
            names[0] = handler2.label(this);
        }
        //tier 3
        if (handler3 != null && handler3.contains(this)) {
            images[1] = handler3.image(this);
            names[1] = handler3.label(this);
        }
        //tier 4
        if (handler4 != null && handler4.contains(this)) {
            images[2] = handler4.image(this);
            names[2] = handler4.label(this);
        }
        //tier 2
        if (handler5 != null && handler5.contains(this)) {
            images[3] = handler5.image(this);
            names[3] = handler5.label(this);
        }
    }

    public LightWand(int tier){
        if(tier <= 1){
            this.image = ItemSpriteSheet.RUSTY_RING;
            return;
        }
        this.image = images[tier-2];
    }

    @Override
    protected void onSpellCast(int spellId) {

    }

    @Override
    public void onHit(Char attacker, Char defender, int damage) {

    }

    public void staffFx(MagesStaff.StaffParticle particle){
        particle.color( 0x8822FF );
        particle.am = 0.3f;
        particle.setLifespan(3f);
        particle.speed.polar(Random.Float(PointF.PI2), 0.3f);
        particle.setSize( 1f, 2f);
        particle.radiateXY(2.5f);
    }
}
