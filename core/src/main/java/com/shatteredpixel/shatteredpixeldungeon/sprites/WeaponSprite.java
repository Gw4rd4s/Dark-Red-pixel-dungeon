package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.watabou.noosa.Visual;
import com.watabou.noosa.tweeners.RotTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

import java.util.HashMap;
import java.util.LinkedHashSet;


public class WeaponSprite extends ItemSprite implements Tweener.Listener {

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

    public void reset(Visual caster, Visual slashFirst, Item weapon) {
        PointF rotAround = caster.center();
        PointF from = slashFirst.center();
        revive();

        if (weapon == null)   view(0, null);
        else                view( weapon );

        setup( caster,
                from,
                weapon);
    }

    private void setup( Visual caster, PointF slashFirst, Item weapon){
        PointF rotAround = caster.center();
        this.origin = new PointF(0, this.height);//rotate weapon around lower left corner

        rotAround.y -= this.height;  //This shenanigan is done because
        point( rotAround );          //lower left corner of weapon sprite must
        rotAround.y += this.height;  //match center of the one who swings

        float aniTime = 0.5f;//swing time
        PointF start = PointF.diff(slashFirst, rotAround);
        //set initial angle
        angle = (float)(Math.atan2( start.x, -start.y ) / 3.1415926 * 180);
        //flip weapon sprite horizontally
        flipVertical = FLIP.contains(weapon );
        //get angle offset for current weapon used
        for (Class<?extends Item> cls : ANGLES.keySet()){
            if (cls.isAssignableFrom(weapon.getClass())){
                angularSpeed = ANGLES.get(cls);
                break;
            }
        }

        angularSpeed = -650;//not even one circle around
        RotTweener tweener = new RotTweener( this, caster, aniTime );
        tweener.listener = this;
        parent.add( tweener );
    }
    @Override
    public void onComplete( Tweener tweener ) {
        kill();
        if (callback != null) {
            callback.call();
        }
    }
}
