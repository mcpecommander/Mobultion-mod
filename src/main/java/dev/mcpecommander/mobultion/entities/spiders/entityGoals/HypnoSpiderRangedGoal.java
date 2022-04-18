package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import dev.mcpecommander.mobultion.entities.spiders.entities.HypnoSpiderEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

/* McpeCommander created on 17/04/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entityGoals */
public class HypnoSpiderRangedGoal extends Goal {
    //Copied from vanilla with slight modifications.

    private final HypnoSpiderEntity attacker;
    private final double speedModifier;
    private final int attackIntervalMin;
    private final float attackRadiusSqr;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public HypnoSpiderRangedGoal(HypnoSpiderEntity hypnoSpiderEntity, double speedModifier, int attackInterval, float attackRadius) {
        this.attacker = hypnoSpiderEntity;
        this.speedModifier = speedModifier;
        this.attackIntervalMin = attackInterval;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.attacker.getTarget() != null;
    }

    public boolean canContinueToUse() {
        if(canUse()){
            if (this.attacker.getTarget() instanceof Player player){
                return !player.isCreative();
            }
        }
        return !this.attacker.getNavigation().isDone();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.attacker.setAggressive(true);
    }

    @Override
    public void stop() {
        this.attacker.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.attacker.stopUsingItem();
    }

    @Override
    public void tick() {
        LivingEntity target = this.attacker.getTarget();
        if (target != null) {
            //The current distance to the target.
            double distance = this.attacker.distanceToSqr(target.getX(), target.getY(), target.getZ());
            //If the skeleton can see the target.
            boolean canSee = this.attacker.getSensing().hasLineOfSight(target);
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
            if (distance < this.attackRadiusSqr && this.seeTime >= 20) {
                this.attacker.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.attacker.getNavigation().moveTo(target, this.speedModifier);
                this.strafingTime = -1;
            }

            //If the skeleton has been strafing for over 20 ticks, it has a chance to change strafing direction.
            if (this.strafingTime >= 20) {
                if (this.attacker.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if (this.attacker.getRandom().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                //After changing (or not changing) strafe, reset the switch timer
                this.strafingTime = 0;
            }

            //If strafing is possible (indicated by strafing time higher than -1) then strafe.
            if (this.strafingTime > -1) {
                //If the distance is sufficiently far away but still within the attack distance, then don't strafe any
                //further back.
                if (distance > this.attackRadiusSqr * 0.75F) {
                    this.strafingBackwards = false;
                } else if (distance < this.attackRadiusSqr * 0.5F) {
                    this.strafingBackwards = true;
                }

                this.attacker.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.attacker.lookAt(target, 30.0F, 30.0F);
            } else {
                this.attacker.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }

            if (this.attacker.isUsingItem()) {
                //If the skeleton cannot see the target for over 60 ticks, then stop using the item.
                if (!canSee && this.seeTime < -60) {
                    this.attacker.stopUsingItem();
                } else if (canSee) {
                    int i = this.attacker.getTicksUsingItem();
                    if (i >= 17) {
                        this.attacker.stopUsingItem();
                        this.attacker.performRangedAttack(target);
                        this.attackTime = this.attackIntervalMin;
                    }
                }
                //Start using the item if the attack cooldown is 0 and if still seeing the player for long enough time.
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.attacker.startUsingItem(InteractionHand.MAIN_HAND);
            }

        }
    }
}
