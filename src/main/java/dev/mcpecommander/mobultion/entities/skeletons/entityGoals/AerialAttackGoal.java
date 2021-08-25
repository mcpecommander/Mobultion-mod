package dev.mcpecommander.mobultion.entities.skeletons.entityGoals;

import dev.mcpecommander.mobultion.entities.skeletons.entities.ForestSkeletonEntity;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.BowItem;
import net.minecraft.util.Hand;

import java.util.EnumSet;

/* McpeCommander created on 23/08/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entityGoals */
public class AerialAttackGoal extends Goal{
    //Copied from vanilla with slight modifications.

    private final ForestSkeletonEntity mob;
    private final double speedModifier;
    private final int attackIntervalMin;
    private final float attackRadiusSqr;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public AerialAttackGoal(ForestSkeletonEntity skeletonEntity, double speedModifier, int attackInterval, float attackRadius) {
        this.mob = skeletonEntity;
        this.speedModifier = speedModifier;
        this.attackIntervalMin = attackInterval;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        return this.mob.getTarget() != null && this.isHoldingForestBow();
    }

    protected boolean isHoldingForestBow() {
        return this.mob.isHolding(Registration.FORESTBOW.get());
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingForestBow();
    }

    public void start() {
        this.mob.setAggressive(true);
    }

    public void stop() {
        this.mob.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.mob.stopUsingItem();
    }

    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            //The current distance to the target.
            double distance = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            //If the skeleton can see the target.
            boolean canSee = this.mob.getSensing().canSee(livingentity);
            //If the skeleton has seen the target, the see time increases otherwise it is decreased
            boolean hasSeen = this.seeTime > 0;
            //If the target has not been seen for a long time, reset the seeTime.
            if (canSee != hasSeen) {
                this.seeTime = 0;
            }
            //If the skeleton can see the target, increase the seeTime otherwise decrease it.
            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            //If within attack distance or can not reach but has and is seeing the target for more than 20 ticks.
            if (distance < (double)this.attackRadiusSqr && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.mob.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }

            //If the skeleton has been strafing for over 20 ticks, it has a chance to change strafing direction.
            if (this.strafingTime >= 20) {
                if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            //If strafing is possible (indicated by strafing time higher than -1) then strafe.
            if (this.strafingTime > -1) {
                //If the distance is sufficiently far away but still within the attack distance, then don't strafe any
                //further back.
                if (distance > (double)(this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (distance < (double)(this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.mob.lookAt(livingentity, 30.0F, 30.0F);
            } else {
                this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

            if (this.mob.isUsingItem()) {
                //If the skeleton cannot see the target for over 60 ticks, then stop using the item.
                if (!canSee && this.seeTime < -60) {
                    this.mob.stopUsingItem();
                } else if (canSee) {
                    int i = this.mob.getTicksUsingItem();
                    if (i >= 20) {
                        this.mob.stopUsingItem();
                        this.mob.performRangedAttack(livingentity, BowItem.getPowerForTime(i));
                        this.attackTime = this.attackIntervalMin;
                    }
                }
            //Start using the item if the attack cooldown is 0 and if still seeing the player for long enough time.
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.mob.startUsingItem(Hand.MAIN_HAND);
            }

        }
    }
}
