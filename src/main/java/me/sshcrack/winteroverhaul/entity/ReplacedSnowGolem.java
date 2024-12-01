package me.sshcrack.winteroverhaul.entity;

public class ReplacedSnowGolem implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    private static  <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.snow_golem.walk", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.snow_golem.idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, ReplacedSnowGolem::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
