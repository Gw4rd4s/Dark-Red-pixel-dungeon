package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.watabou.noosa.Visual;
import com.watabou.noosa.tweeners.RotTweener;
import com.watabou.noosa.tweeners.ThrustTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

import java.util.HashMap;
import java.util.LinkedHashSet;


public class StabSprite extends ItemSprite implements Tweener.Listener {

    private Callback callback;

    public void playStab(Visual caster, Visual targetSprite, Item weapon){
        PointF casterCenter = caster.point();
        PointF target = targetSprite.point();
        revive();

        originToCenter();
        casterCenter.x += width()/2;
        casterCenter.y -= height() * 0.2f;//begin stab at body, not neck
        target.x += width()/2;
        target.y -= height()/2;
        point( casterCenter );
        if (weapon == null)   view(0, null);
        else                view( weapon );

        this.origin = new PointF(0, height());//rotate weapon around lower left corner
        PointF vector = PointF.diff(target, casterCenter);
        //set initial angle
        this.angle = 135 - (float)(Math.atan2( vector.x, vector.y ) / 3.1415926 * 180);

        ThrustTweener tweener = new ThrustTweener( this,casterCenter, target, 0.1f );
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
