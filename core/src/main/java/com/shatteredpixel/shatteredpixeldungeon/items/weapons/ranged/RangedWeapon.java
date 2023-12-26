package com.shatteredpixel.shatteredpixeldungeon.items.weapons.ranged;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapons.missiles.Ammo;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RangedWeapon extends Weapon{

    public static final String AC_SHOOT		= "SHOOT";
    {
        bones = true;
        defaultAction = AC_SHOOT;
        usesTargeting = true;


    }

    public int tier;
    protected int targetPos;

    @Override
    public String defaultAction() {
        return super.defaultAction();
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        //if equipped shooting from weapon is possible
        if (isEquipped(hero) && Dungeon.hero.belongings.getItem(Ammo.class) != null) {
            actions.add(AC_SHOOT);
        }
        return actions;
    }

    @Override
    public String actionName(String action, Hero hero) {
        return super.actionName(action, hero);

    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        //if shooting run range logic
        if (action.equals(AC_SHOOT)) {
            curUser = hero;
            curItem = this;
            GameScene.selectCell( shooter );
        }
    }

    @Override
    public boolean doEquip(Hero hero) {
        if (super.doEquip(hero)){
            ActionIndicator.refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean equipSecondary(Hero hero) {
        if (super.equipSecondary(hero)){
            ActionIndicator.refresh();
            return true;
        }
        return false;
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)){
            ActionIndicator.refresh();
            return true;
        }
        return false;
    }

    @Override
    public int targetingPos(Hero user, int dst) {
        return shootProj().targetingPos(user, dst);
    }

    public int wepTier(){
        return tier;
    }

    @Override
    public String info() {

        String info = desc();

        if (levelKnown) {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, 0, 10, STRReq());
            if (STRReq() > Dungeon.hero.lvl) {
                info += " " + Messages.get(Weapon.class, "too_heavy");
            } else if (Dungeon.hero.lvl > STRReq()){
                info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.lvl - STRReq());
            }
        } else {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, 0, 10, STRReq());
            if (STRReq() > Dungeon.hero.lvl) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
            }
        }

        String statsInfo = statsInfo();
        if (!statsInfo.equals("")) info += "\n\n" + statsInfo;

        switch (augment) {
            case SPEED:
                info += " " + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += " " + Messages.get(Weapon.class, "stronger");
                break;
            case NONE:
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", enchantment.name()));
            info += " " + enchantment.desc();
        }

        if (cursed && isEquipped( Dungeon.hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            if (enchantment != null && enchantment.curse()) {
                info += "\n\n" + Messages.get(Weapon.class, "weak_cursed");
            } else {
                info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
            }
        }

        return info;
    }

    public String statsInfo(){
        return Messages.get(this, "stats_desc");
    }

    @Override
    public String status() {
            return super.status();
    }

    @Override
    public int value() {
        int price = 20 * tier;
        if (hasGoodEnchant()) {
            price *= 1.5;
        }
        if (cursedKnown && (cursed || hasCurseEnchant())) {
            price /= 2;
        }
        if (levelKnown && level() > 0) {
            price *= (level() + 1);
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }

    public RangedWeapon.Projectile shootProj(){
        return new RangedWeapon.Projectile();
    }

    /**
     * Handles projectile logic for Ranged Weapon
     */
    public class Projectile extends MissileWeapon {

        {
            image = ItemSpriteSheet.SPIRIT_ARROW;

            hitSound = Assets.Sounds.HIT_ARROW;
        }

        @Override//TODO enchanting weapon and ammo is going to be a thing. In the same time
        public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
            return RangedWeapon.this.hasEnchant(type, owner);
        }

        @Override//TODO enchanting weapon and ammo is going to be a thing. In the same time
        public int proc(Char attacker, Char defender, int damage) {
            return RangedWeapon.this.proc(attacker, defender, damage);
        }

        @Override
        public float delayFactor(Char user) {
            return RangedWeapon.this.delayFactor(user);
        }

        @Override
        public int wepTier() {
            return this.tier;
        }

        @Override
        protected void onThrow( int cell ) {
            Char enemy = Actor.findChar( cell );
            if (enemy == null || enemy == curUser) {
                parent = null;
                Splash.at( cell, 0xCC99FFFF, 1 );
            } else {
                if (!curUser.shoot( enemy, this )) {
                    Splash.at(cell, 0xCC99FFFF, 1);
                }
            }
        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play( Assets.Sounds.ATK_SPIRITBOW, 1, Random.Float(0.87f, 1.15f) );
        }

        int flurryCount = -1;
        Actor flurryActor = null;

        @Override
        public void cast(final Hero user, final int dst) {
            final int cell = throwPos( user, dst );
            RangedWeapon.this.targetPos = cell;
            if (RangedWeapon.this.augment == Augment.SPEED){
                if (flurryCount == -1) flurryCount = 3;

                final Char enemy = Actor.findChar( cell );

                if (enemy == null){
                    if (user.buff(Talent.LethalMomentumTracker.class) != null){
                        user.buff(Talent.LethalMomentumTracker.class).detach();
                        user.next();
                    } else {
                        user.spendAndNext(castDelay(user, dst));
                    }
                    flurryCount = -1;

                    if (flurryActor != null){
                        flurryActor.next();
                        flurryActor = null;
                    }
                    return;
                }

                QuickSlotButton.target(enemy);

                user.busy();

                throwSound();

                user.sprite.zap(cell);
                ((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
                        reset(user.sprite,
                                cell,
                                this,
                                new Callback() {
                                    @Override
                                    public void call() {
                                        if (enemy.isAlive()) {
                                            curUser = user;
                                            onThrow(cell);
                                        }

                                        flurryCount--;
                                        if (flurryCount > 0){
                                            Actor.add(new Actor() {

                                                {
                                                    actPriority = VFX_PRIO-1;
                                                }

                                                @Override
                                                protected boolean act() {
                                                    flurryActor = this;
                                                    int target = QuickSlotButton.autoAim(enemy, RangedWeapon.Projectile.this);
                                                    if (target == -1) target = cell;
                                                    cast(user, target);
                                                    Actor.remove(this);
                                                    return false;
                                                }
                                            });
                                            curUser.next();
                                        } else {
                                            if (user.buff(Talent.LethalMomentumTracker.class) != null){
                                                user.buff(Talent.LethalMomentumTracker.class).detach();
                                                user.next();
                                            } else {
                                                user.spendAndNext(castDelay(user, dst));
                                            }
                                            flurryCount = -1;
                                        }

                                        if (flurryActor != null){
                                            flurryActor.next();
                                            flurryActor = null;
                                        }
                                    }
                                });

            } else {

                if (user.hasTalent(Talent.SEER_SHOT)
                        && user.buff(Talent.SeerShotCooldown.class) == null){
                    int shotPos = throwPos(user, dst);
                    if (Actor.findChar(shotPos) == null) {
                        RevealedArea a = Buff.affect(user, RevealedArea.class, 5 * user.pointsInTalent(Talent.SEER_SHOT));
                        a.depth = Dungeon.depth;
                        a.pos = shotPos;
                        Buff.affect(user, Talent.SeerShotCooldown.class, 20f);
                    }
                }

                super.cast(user, dst);
            }
        }
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                shootProj().cast(curUser, target);
            }
        }
        @Override
        public String prompt() {
            return Messages.get(Bow.class, "prompt");
        }
    };
}
