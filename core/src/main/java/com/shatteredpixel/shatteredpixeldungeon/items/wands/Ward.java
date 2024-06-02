package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WardSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class Ward extends NPC {

    {
        HP = HT = 7;
        spriteClass = WardSprite.class;

        alignment = Alignment.ALLY;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.INORGANIC);

        viewDistance = 4;
        state = WANDERING;
        immunities.add( Sleep.class );
        immunities.add( Terror.class );
        immunities.add( Dread.class );
        immunities.add( Vertigo.class );
        immunities.add( AllyBuff.class );
    }

    protected boolean castByPlayer;

    @Override
    public boolean act(){
        damage(1,0,0,0,0, this);
        return false;
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }

    @Override
    public CharSprite sprite() {
        WardSprite sprite = (WardSprite) super.sprite();
        sprite.linkVisuals(this);
        return sprite;
    }

    @Override
    public void destroy() {
        super.destroy();
        Dungeon.observe();
        GameScene.updateFog(pos, viewDistance+1);
    }

    @Override
    public boolean canInteract(Char c) {
        return true;
    }

    @Override
    public boolean interact( Char c ) {
        if (c != Dungeon.hero){
            return true;
        }
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions( sprite(),
                        Messages.get(Ward.this, "dismiss_title"),
                        Messages.get(Ward.this, "dismiss_body"),
                        Messages.get(Ward.this, "dismiss_confirm"),
                        Messages.get(Ward.this, "dismiss_cancel") ){
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0){
                            die(null);
                        }
                    }
                });
            }
        });
        return true;
    }


}