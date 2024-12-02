package me.sshcrack.winteroverhaul.entity;

import me.sshcrack.winteroverhaul.items.GolemUpgradeSlot;
import me.sshcrack.winteroverhaul.registry.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class GolemAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public GolemAttackableTargetGoal(Mob pMob, Class<T> pTargetType, int pRandomInterval, boolean pMustSee, boolean pMustReach, @Nullable Predicate<LivingEntity> pTargetPredicate) {
        super(pMob, pTargetType, pRandomInterval, pMustSee, pMustReach, pTargetPredicate);
    }

    @Override
    protected double getFollowDistance() {
        float amount = 1f;
        if (this.mob instanceof IUpgradeAbleSnowGolem upgradeAbleSnowGolem) {
            Item scarf = upgradeAbleSnowGolem.winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.SCARF).getItem();
            Item hat = upgradeAbleSnowGolem.winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.HAT).getItem();
            amount += scarf.equals(ModItems.GREEN_SCARF) ? 0.5f : 0f;
            amount += hat.equals(ModItems.GREEN_HAT) ? 0.5f : 0f;
        }
        return super.getFollowDistance() * amount;
    }

    @Override
    @SuppressWarnings("resource")
    protected void findTarget() {
        this.targetConditions.range(this.getFollowDistance());
        if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
            this.target = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()), (p_148152_) -> true), this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        } else {
            this.target = this.mob.level().getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
    }
}
