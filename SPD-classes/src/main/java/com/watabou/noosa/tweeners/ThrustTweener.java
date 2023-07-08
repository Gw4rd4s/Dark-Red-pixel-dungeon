package com.watabou.noosa.tweeners;

import com.watabou.noosa.Visual;
import com.watabou.utils.PointF;
public class ThrustTweener extends Tweener{
    public Visual visual;
    public PointF start;
    public PointF end;
    /**
     * Some stuff required for animation to work
     * @param visual Sprite of the weapon
     * @param end final destination of the stab
     * @param time Animation time
     */
    public ThrustTweener( Visual visual, PointF start, PointF end, float time ) {
        super( visual, time );

        this.visual = visual;
        this.start = start;
        this.end = end;

    }

    @Override
    protected void updateValues( float progress ) {
        if(progress >= 0.5){//reverse back
            progress = 1 - progress;
        }
        visual.point( PointF.inter( start, end, 2*progress - 0.5f) );
    }
}
