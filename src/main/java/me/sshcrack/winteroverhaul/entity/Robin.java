package me.sshcrack.winteroverhaul.entity;

import me.sshcrack.winteroverhaul.registry.ModEntities;
import me.sshcrack.winteroverhaul.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Robin extends Animal implements FlyingAnimal, GeoEntity {
    protected static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("animation.robin.fly");
    protected static final RawAnimation IDLE1_ANIM = RawAnimation.begin().thenPlay("animation.robin.idle1");
    protected static final RawAnimation IDLE2_ANIM = RawAnimation.begin().thenPlay("animation.robin.idle2");
    protected static final RawAnimation IDLE3_ANIM = RawAnimation.begin().thenPlay("animation.robin.idle3");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;

    public Robin(EntityType<? extends Animal> entity, Level level) {
        super(entity, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0).add(Attributes.FLYING_SPEED, 0.4).add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1D));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();
    }

    //region Flapping
    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed = (float) ((double) this.flapSpeed + (double) (!this.onGround() && !this.isPassenger() ? 4 : -1) * 0.3D);
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping = (float) ((double) this.flapping * 0.9D);
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
        }

        this.flap += this.flapping * 2.0F;
    }

    @Override
    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    @Override
    protected void onFlap() {
        this.playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }
    //endregion

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        return pEntity.hurt(this.damageSources().mobAttack(this), 3.0F);
    }

    //region Sounds
    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.ROBIN_AMBIENT;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    @Override
    public @NotNull SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }

    //endregion

    //region Breeding

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.SWEET_BERRIES);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return ModEntities.ROBIN.create(level);
    }

    //endregion

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    //region Animation

    private <E extends GeoEntity> PlayState idle(AnimationState<E> event) {
        boolean isRunning = !event.getController().getAnimationState().equals(AnimationController.State.STOPPED);
        if (isFlying()) {
            event.getController().forceAnimationReset();
            return PlayState.STOP;
        }

        if (isRunning) return PlayState.CONTINUE;
        boolean threeCheck = tickCount % 60 == 0 && random.nextBoolean();
        boolean fourCheck = tickCount % 80 == 0 && random.nextBoolean();
        boolean fiveCheck = tickCount % 100 == 0;
        if (threeCheck || fourCheck || fiveCheck) {
            switch (random.nextInt(3)) {
                case 0 -> {
                    event.getController().setAnimation(IDLE1_ANIM);
                    return PlayState.CONTINUE;
                }
                case 1 -> {
                    event.getController().setAnimation(IDLE2_ANIM);
                    return PlayState.CONTINUE;
                }
                default -> {
                    event.getController().setAnimation(IDLE3_ANIM);
                    return PlayState.CONTINUE;
                }
            }
        }

        event.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    private <E extends GeoEntity> PlayState flying(AnimationState<E> event) {
        if (isFlying()) {
            event.getController().setAnimation(FLY_ANIM);
            return PlayState.CONTINUE;
        }

        event.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "flight_controller", 0, this::flying));
        data.add(new AnimationController<>(this, "idle_controller", 5, this::idle));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    //endregion
}
