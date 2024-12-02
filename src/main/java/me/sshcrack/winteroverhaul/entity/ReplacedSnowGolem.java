package me.sshcrack.winteroverhaul.entity;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ReplacedSnowGolem implements GeoEntity {
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.snow_golem.walk");
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.snow_golem.idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private static <P extends ReplacedSnowGolem> PlayState predicate(final AnimationState<P> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(WALK_ANIM);
        } else {
            event.getController().setAnimation(IDLE_ANIM);
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 20, ReplacedSnowGolem::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
