package me.sshcrack.winteroverhaul.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.sshcrack.winteroverhaul.entity.GolemAttackableTargetGoal;
import me.sshcrack.winteroverhaul.entity.GolemRangedAttackGoal;
import me.sshcrack.winteroverhaul.entity.ISnowGolemSnowball;
import me.sshcrack.winteroverhaul.entity.IUpgradeAbleSnowGolem;
import me.sshcrack.winteroverhaul.items.GolemUpgradeItem;
import me.sshcrack.winteroverhaul.items.GolemUpgradeSlot;
import me.sshcrack.winteroverhaul.registry.ModItems;
import me.sshcrack.winteroverhaul.registry.ModParticles;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SyncedDataHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SnowGolem.class)
public abstract class MixinSnowGolem extends Mob implements IUpgradeAbleSnowGolem, SyncedDataHolder {
    @Unique
    private static final EntityDataAccessor<CompoundTag> UPGRADES = SynchedEntityData.defineId(MixinSnowGolem.class, EntityDataSerializers.COMPOUND_TAG);

    @Unique
    private final NonNullList<ItemStack> upgrades = NonNullList.withSize(GolemUpgradeSlot.values().length, ItemStack.EMPTY);


    protected MixinSnowGolem(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void onDefineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        CompoundTag tag = new CompoundTag();
        if (upgrades != null)
            tag.put("GolemUpgrades", serializeUpdates());

        builder.define(UPGRADES, tag);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if (!dataAccessor.equals(UPGRADES))
            return;

        CompoundTag entityData = getEntityData().get(UPGRADES);
        ListTag listTag = entityData.getList("GolemUpgrades", Tag.TAG_COMPOUND);

        for (int i = 0; i < this.upgrades.size(); ++i) {
            CompoundTag itemTag = listTag.getCompound(i);
            this.upgrades.set(i, ItemStack.parseOptional(registryAccess(), itemTag));
        }
    }

    @SuppressWarnings("resource")
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void onAiStep(CallbackInfo ci) {
        SnowGolem golem = (SnowGolem) ((Object) this);
        if (!golem.level().isClientSide) {
            if (upgrades != null) {
                upgrades.forEach(stack -> {
                    if (stack.getItem() instanceof GolemUpgradeItem upgradeItem) upgradeItem.tick(stack, golem);
                });
            }
        } else if (this.tickCount % 2 == 0) {
            SimpleParticleType particleType;
            float health = golem.getHealth() / golem.getMaxHealth();
            if (health >= 0.6f) particleType = ModParticles.SNOWFLAKE_1;
            else if (health >= 0.3) particleType = ModParticles.SNOWFLAKE_2;
            else particleType = ModParticles.SNOWFLAKE_3;
            this.level().addParticle(particleType, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
        }
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void changeRangeAttackGoal(CallbackInfo ci) {
        goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof RangedAttackGoal);
        goalSelector.addGoal(1, new GolemRangedAttackGoal(SnowGolem.class.cast(this), 1.25D, 20, 10.0F));
        targetSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof NearestAttackableTargetGoal);
        targetSelector.addGoal(1, new GolemAttackableTargetGoal<>(this, Mob.class, 10, true, false, entity -> entity instanceof Enemy));
    }

    @Inject(method = "performRangedAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getEyeY()D"))
    private void onSnowballCreation(LivingEntity pTarget, float pDistanceFactor, CallbackInfo ci, @Local Snowball snowball) {
        if (snowball instanceof ISnowGolemSnowball snowGolemSnowball) {
            snowGolemSnowball.winteroverhaul_setGolemSnowball(true);
            Item scraf = winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.SCARF).getItem();
            Item hat = winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.HAT).getItem();
            int amount = hat.equals(ModItems.RED_HAT) ? 2 : 0;
            amount += scraf.equals(ModItems.RED_SCARF) ? 2 : 0;
            if (amount > 0) snowGolemSnowball.winteroverhaul_setGolemMultiplier(amount);
            Item face = winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.FACE).getItem();
            if (face.equals(Items.CARROT) || face.equals(Items.GOLDEN_CARROT)) {
                snowGolemSnowball.winteroverhaul_addMobEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
                if (face.equals(Items.GOLDEN_CARROT)) {
                    snowGolemSnowball.winteroverhaul_addMobEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 4));
                }
            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void onSaveNbt(CompoundTag pCompound, CallbackInfo ci) {
        pCompound.put("GolemUpgrades", serializeUpdates());
    }

    @Unique
    private ListTag serializeUpdates() {
        ListTag listtag = new ListTag();

        for (ItemStack itemstack : this.upgrades) {
            Tag compoundTag = new CompoundTag();
            if (!itemstack.isEmpty()) {
                compoundTag = itemstack.save(registryAccess(), new CompoundTag());
            }
            ;

            listtag.add(compoundTag);
        }

        return listtag;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void onLoadNbt(CompoundTag pCompound, CallbackInfo ci) {
        if (pCompound.contains("GolemUpgrades", Tag.TAG_LIST)) {
            ListTag listtag = pCompound.getList("GolemUpgrades", Tag.TAG_COMPOUND);
            for (int i = 0; i < this.upgrades.size(); ++i) {
                CompoundTag itemTag = listtag.getCompound(i);
                if (!itemTag.isEmpty()) this.upgrades.set(i, ItemStack.parseOptional(registryAccess(), itemTag));
            }

            CompoundTag sendTag = new CompoundTag();
            sendTag.put("GolemUpgrades", listtag);

            entityData.set(UPGRADES, sendTag);
        }
    }

    @WrapOperation(method = "readyForShearing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/SnowGolem;hasPumpkin()Z"))
    private boolean winter_overhaul$readyForShearing(SnowGolem instance, Operation<Boolean> original) {
        if (original.call(instance))
            return true;

        return upgrades.stream().anyMatch(e -> !e.isEmpty());
    }

    @SuppressWarnings("resource")
    @Inject(method = "shear", at = @At("HEAD"), cancellable = true)
    private void winter_overhaul$shear(CallbackInfo ci, @Local(argsOnly = true) SoundSource source) {
        if (upgrades.stream().allMatch(ItemStack::isEmpty))
            return;

        ci.cancel();
        this.level().playSound(null, this, SoundEvents.SNOW_GOLEM_SHEAR, source, 1.0F, 1.0F);
        if (this.level().isClientSide())
            return;

        if (upgrades.stream().anyMatch(e -> !e.isEmpty())) {
            for (GolemUpgradeSlot slot : GolemUpgradeSlot.values()) {
                ItemStack stack = winter_overhaul$getGolemUpgradeInSlot(slot);
                if (!stack.isEmpty()) {
                    spawnAtLocation(stack, getEyeHeight());
                    winter_overhaul$setGolemUpgradeInSlot(slot, ItemStack.EMPTY);
                }
            }
        }

    }

    @SuppressWarnings("resource")
    @Override
    public ItemStack winter_overhaul$setGolemUpgradeInSlot(GolemUpgradeSlot slot, ItemStack stack) {
        if (upgrades == null)
            return ItemStack.EMPTY;

        ItemStack res = upgrades.set(slot.index, stack);
        if (!this.level().isClientSide()) {
            CompoundTag tag = new CompoundTag();
            tag.put("GolemUpgrades", serializeUpdates());

            entityData.set(UPGRADES, tag);
        }
        return res;
    }

    @Override
    public ItemStack winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot slot) {
        if (upgrades == null)
            return ItemStack.EMPTY;

        return upgrades.get(slot.index);
    }
}
