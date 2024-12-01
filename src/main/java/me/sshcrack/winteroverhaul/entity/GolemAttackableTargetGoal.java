package me.sshcrack.winteroverhaul.entity;

import me.sshcrack.winteroverhaul.items.GolemUpgradeSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class GolemAttackableTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {

    public GolemAttackableTargetGoal(MobEntity pMob, Class<T> pTargetType, int pRandomInterval, boolean pMustSee, boolean pMustReach, @Nullable Predicate<LivingEntity> pTargetPredicate) {
        super(pMob, pTargetType, pRandomInterval, pMustSee, pMustReach, pTargetPredicate);
    }

    @Override
    protected double getFollowRange() {
        float amount = 1f;
        if (this.mob instanceof IUpgradeAbleSnowGolem upgradeAbleSnowGolem) {
            Item scarf = upgradeAbleSnowGolem.getGolemUpgradeInSlot(GolemUpgradeSlot.SCARF).getItem();
            Item hat = upgradeAbleSnowGolem.getGolemUpgradeInSlot(GolemUpgradeSlot.HAT).getItem();
            amount += scarf.equals(ModItems.GREEN_SCARF.get()) ? 0.5f : 0f;
            amount += hat.equals(ModItems.GREEN_HAT.get()) ? 0.5f : 0f;
        }

        return super.getFollowRange() * amount;
    }

    @Override
    protected void findClosestTarget() {
        this.targetPredicate.setBaseMaxDistance(this.getFollowRange());
        if (this.targetClass != PlayerEntity.class && this.targetClass != ServerPlayerEntity.class) {
            this.targetEntity = this.mob.getWorld().getClosestEntity(
                    this.mob.getWorld().getEntitiesByClass(
                            this.targetClass,
                            this.getSearchBox(this.getFollowRange()), (p_148152_) -> true),
                    this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        } else {
            this.targetEntity = this.mob.getWorld().getClosestPlayer(this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
    }
}
