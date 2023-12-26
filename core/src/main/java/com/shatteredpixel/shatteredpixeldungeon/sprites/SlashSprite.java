package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.tier3.Scimitar;
import com.watabou.noosa.Visual;
import com.watabou.noosa.tweeners.RotTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

import java.util.HashMap;
import java.util.LinkedHashSet;


public class SlashSprite extends ItemSprite implements Tweener.Listener {

    private Callback callback;
    /**
     * Hardcoded angle offsets for melee weapon animation
     */
    private static final HashMap<Class<?extends Item>, Float> ANGLES = new HashMap<>();
    static {
        ANGLES.put(Scimitar.class,        -90f);
    }
    private static final LinkedHashSet<Class<?extends Item>> FLIP = new LinkedHashSet<>();
    static{
        FLIP.add(Scimitar.class);
    }

    public void playSlash(Visual caster, Visual slashFirst, Item weapon) {
        PointF rotAround = caster.center();
        PointF slashStart = slashFirst.center();
        revive();

        if (weapon == null)   view(0, null);
        else                view( weapon );

        this.origin = new PointF(0, height());//rotate weapon around lower left corner

        rotAround.y -= height();  //This shenanigan is done because
        point( rotAround );          //lower left corner of weapon sprite must
        rotAround.y += height();  //match center of the one who swings

        PointF start = PointF.diff(slashStart, rotAround);
        //set initial angle
        this.angle = (float)(Math.atan2( start.x, -start.y ) / 3.1415926 * 180);
        //flip weapon sprite horizontally
        this.flipVertical = FLIP.contains(weapon );
        //get angle offset for current weapon used

        this.angularSpeed = -1500;//not even one circle around
        RotTweener tweener = new RotTweener( this, caster, 0.2f );
        tweener.listener = this;
        this.parent.add( tweener );
    }


    @Override
    public void onComplete( Tweener tweener ) {
        kill();
        if (callback != null) {
            callback.call();
        }
    }
}
