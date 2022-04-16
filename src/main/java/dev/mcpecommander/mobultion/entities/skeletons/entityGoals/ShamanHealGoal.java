package dev.mcpecommander.mobultion.entities.skeletons.entityGoals;

import dev.mcpecommander.mobultion.entities.skeletons.SkeletonsConfig;
import dev.mcpecommander.mobultion.entities.skeletons.entities.ShamanSkeletonEntity;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/* McpeCommander created on 23/08/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entityGoals */
public class ShamanHealGoal extends Goal{
    //Copied from vanilla with slight modifications.

    private final ShamanSkeletonEntity mob;
    private final double speedModifier;
    private final int healingIntervalMin;
    private final float healingRadiusSqr;
    private int healingTime = -1;
    private int seeTime;

    public ShamanHealGoal(ShamanSkeletonEntity skeletonEntity, double speedModifier, int healingInterval, float healingRadius) {
        this.mob = skeletonEntity;
        this.speedModifier = speedModifier;
        this.healingIntervalMin = healingInterval;
        this.healingRadiusSqr = healingRadius * healingRadius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        return this.mob.getTarget() != null && this.mob.getTarget().getHealth() < this.mob.getMaxHealth() && this.isHoldingStaff();
    }

    private boolean isHoldingStaff() {
        return this.mob.isHolding(Registration.HEALINGSTAFF.get());
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingStaff();
    }

    public void start() {
        this.mob.setAggressive(true);
    }

    public void stop() {
        this.mob.setAggressive(false);
        this.mob.setTarget(null);
        this.seeTime = 0;
        this.healingTime = -1;
        this.mob.stopUsingItem();
    }

    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            //The current distance to the target.
            double distance = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
            //If the skeleton can see the target.
            boolean canSee = this.mob.getSensing().hasLineOfSight(target);
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

            //If within healing distance or can not reach but has and is seeing the target for more than 20 ticks.
            if (distance < this.healingRadiusSqr && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
            } else {
                this.mob.getNavigation().moveTo(target, this.speedModifier);
            }

            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (this.mob.isUsingItem()) {
                //If the skeleton cannot see the target for over 60 ticks, then stop using the item.
                if (!canSee && this.seeTime < -60) {
                    this.mob.stopUsingItem();
                } else if (canSee) {
                    int i = this.mob.getTicksUsingItem();
                    if (i >= 70) {
                        this.mob.stopUsingItem();
                        this.mob.performRangedAttack(target, SkeletonsConfig.SHAMAN_HEALING.get().floatValue());
                        this.healingTime = this.healingIntervalMin;
                    }
                }
            //Start using the item if the healing cooldown is 0 and if still seeing the player for long enough time.
            } else if (--this.healingTime <= 0 && this.seeTime >= -60) {
                this.mob.startUsingItem(InteractionHand.MAIN_HAND);
            }

        }
    }

}
