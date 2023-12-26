package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class WaterWand extends ProtoWand{

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

    @Override
    protected void onSpellCast(int spellId) {

    }

    @Override
    public void onHit(Char attacker, Char defender, int damage) {

    }

    public WaterWand(int tier){
        if(tier <= 1){
            this.image = ItemSpriteSheet.RUSTY_RING;
            return;
        }
        this.image = images[tier-2];
    }
}
