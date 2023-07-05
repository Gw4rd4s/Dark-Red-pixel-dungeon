package com.watabou.noosa.tweeners;

import com.watabou.noosa.Visual;
import com.watabou.utils.PointF;
public class RotTweener extends Tweener{
    public Visual visual;
    private final Visual caster;

    /**
     * Some stuff required for animation to work
     * @param visual Sprite of the weapon
     * @param caster Sprite of the attacker, who swings weapon
     * @param time Animation time
     */
    public RotTweener( Visual visual, Visual caster, float time ) {
        super( visual, time );
        this.visual = visual;
        this.caster = caster;
    }

    @Override
    protected void updateValues( float progress ) {
        PointF rotAround = caster.center();
        rotAround.y -= visual.height;//keep aligned with center of caster
        visual.point(rotAround);
    }
}
