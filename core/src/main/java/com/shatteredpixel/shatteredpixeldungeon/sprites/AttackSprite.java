package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.tier3.Scimitar;
import com.watabou.noosa.Visual;
import com.watabou.noosa.tweeners.RotTweener;
import com.watabou.noosa.tweeners.ThrustTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

import java.util.HashMap;
import java.util.LinkedHashSet;


public class AttackSprite extends ItemSprite implements Tweener.Listener {

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

    public void playStab(Visual casterSprite, Visual targetSprite, Item weapon){
        PointF caster = casterSprite.point();
        PointF target = targetSprite.point();
        revive();

        originToCenter();
        caster.x += casterSprite.width()/2;
        caster.y -= casterSprite.height() * 0.2f;//begin stab at body, not neck
        target.x += targetSprite.width()/2;
        target.y -= targetSprite.height()/2;
        if (weapon == null)   view(0, null);
        else                view( weapon );

        this.origin = new PointF(0, height());//rotate weapon around lower left corner
        PointF vector = PointF.diff(target, caster);
        //set initial angle
        this.angle = 135 - (float)(Math.atan2( vector.x, vector.y ) / 3.1415926 * 180);
        this.angularSpeed = 0;
        ThrustTweener tweener = new ThrustTweener( this,caster, target, 0.3f );
        tweener.listener = this;
        this.parent.add( tweener );
    }

    public void playBlock(Visual casterSprite, Visual targetSprite, Item weapon){
        PointF caster = casterSprite.point();
        PointF target = targetSprite.point();
        revive();

        originToCenter();
        caster.x += casterSprite.width()/2;
        caster.y -= casterSprite.height() * 0.2f;//begin stab at body, not neck
        target.x += targetSprite.width()/2;
        target.y -= targetSprite.height()/2;

        target.x = 0.3f * (target.x - caster.x) + caster.x;
        target.y = 0.3f * (target.y - caster.y) + caster.y;
        if (weapon == null)   view(0, null);
        else                view( weapon );
        //this.origin = new PointF(0, height());//rotate weapon around lower left corner
        this.angle = 0;
        this.angularSpeed = 0;
        ThrustTweener tweener = new ThrustTweener( this,caster, target, 0.1f );
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
