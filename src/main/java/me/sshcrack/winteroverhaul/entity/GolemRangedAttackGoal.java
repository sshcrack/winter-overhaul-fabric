package me.sshcrack.winteroverhaul.entity;

import me.sshcrack.winteroverhaul.items.GolemUpgradeSlot;
import me.sshcrack.winteroverhaul.registry.ModItems;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;

public class GolemRangedAttackGoal extends Goal {

    private final SnowGolem snowGolem;
    @Nullable
    private LivingEntity target;
    private int attackTime = -1;
    private final double speedModifier;
    private int seeTime;
    private final int interval;
    private final float attackRadius;

    public GolemRangedAttackGoal(SnowGolem snowGolem, double pSpeedModifier, int interval, float pAttackRadius) {
        this.snowGolem = snowGolem;
        this.speedModifier = pSpeedModifier;
        this.interval = interval;
        this.attackRadius = pAttackRadius;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        LivingEntity livingentity = this.snowGolem.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return this.canUse() || !this.snowGolem.getNavigation().isDone();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public int getInterval() {
        if (!(snowGolem instanceof IUpgradeAbleSnowGolem upgradeAbleSnowGolem)) return interval;
        Item scarf = upgradeAbleSnowGolem.winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.SCARF).getItem();
        Item hat = upgradeAbleSnowGolem.winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.HAT).getItem();
        int amount = 1;
        amount += scarf.equals(ModItems.CYAN_SCARF) ? 1 : 0;
        amount += hat.equals(ModItems.CYAN_HAT) ? 1 : 0;
        return interval / amount;
    }

    public float getAttackRadius() {
        if (!(snowGolem instanceof IUpgradeAbleSnowGolem upgradeAbleSnowGolem)) return interval;
        Item scarf = upgradeAbleSnowGolem.winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.SCARF).getItem();
        Item hat = upgradeAbleSnowGolem.winter_overhaul$getGolemUpgradeInSlot(GolemUpgradeSlot.HAT).getItem();
        float amount = 1f;
        amount += scarf.equals(ModItems.GREEN_SCARF) ? 0.5f : 0f;
        amount += hat.equals(ModItems.GREEN_HAT) ? 0.5f : 0f;
        return attackRadius * amount;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (this.target == null) return;
        double d0 = this.snowGolem.distanceToSqr(this.target);
        boolean flag = this.snowGolem.getSensing().hasLineOfSight(this.target);
        if (flag) this.seeTime++;
        else this.seeTime = 0;

        float attackRadius = getAttackRadius();

        if (!(d0 > (double) (attackRadius * attackRadius)) && this.seeTime >= 5) {
            this.snowGolem.getNavigation().stop();
        } else {
            this.snowGolem.getNavigation().moveTo(this.target, this.speedModifier);
        }

        this.snowGolem.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {
            if (!flag) return;

            float f = (float) Math.sqrt(d0) / attackRadius;
            float f1 = Mth.clamp(f, 0.1F, 1.0F);
            this.snowGolem.performRangedAttack(this.target, f1);
            this.attackTime = Mth.floor(getInterval());
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(getInterval());
        }

    }
}
