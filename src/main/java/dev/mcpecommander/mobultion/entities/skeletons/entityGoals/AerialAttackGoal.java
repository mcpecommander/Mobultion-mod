package dev.mcpecommander.mobultion.entities.skeletons.entityGoals;

import dev.mcpecommander.mobultion.entities.skeletons.entities.ForestSkeletonEntity;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.BowItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;
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

    private boolean isHoldingForestBow() {
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
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            boolean openSpace = checkVicinity(target, this.mob.position());
            if(openSpace){
                //The current distance to the target.
                double distance = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
                //If the skeleton can see the target.
                boolean canSee = this.mob.getSensing().canSee(target);
                //If the skeleton has seen the target, the see time increases otherwise it is decreased
                boolean hasSeen = this.seeTime > 0;
                //If the target has not been seen for a long time, reset the seeTime.
                if (canSee != hasSeen) {
                    this.seeTime = 0;
                }
                //If the skeleton can see the target and is under direct sky, increase the seeTime otherwise decrease it.
                if (canSee) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                //If within attack distance or can not reach but has and is seeing the target for more than 20 ticks.
                if (distance < this.attackRadiusSqr && this.seeTime >= 20) {
                    this.mob.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.mob.getNavigation().moveTo(target, this.speedModifier);
                    this.strafingTime = -1;
                }

                //If the skeleton has been strafing for over 20 ticks, it has a chance to change strafing direction.
                if (this.strafingTime >= 20) {
                    if (this.mob.getRandom().nextFloat() < 0.3D) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if (this.mob.getRandom().nextFloat() < 0.3D) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }

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

                    this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.mob.lookAt(target, 30.0F, 30.0F);
                } else {
                    this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
                }

                if (this.mob.isUsingItem()) {
                    //If the skeleton cannot see the target for over 60 ticks, then stop using the item.
                    if (!canSee && this.seeTime < -60) {
                        this.mob.stopUsingItem();
                    } else if (canSee) {
                        int i = this.mob.getTicksUsingItem();
                        if (i >= 20) {
                            this.mob.stopUsingItem();
                            this.mob.performRangedAttack(target, BowItem.getPowerForTime(i));
                            this.attackTime = this.attackIntervalMin;
                        }
                    }
                    //Start using the item if the attack cooldown is 0 and if still seeing the player for long enough time.
                } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                    this.mob.startUsingItem(Hand.MAIN_HAND);
                }
            }else {
                if(this.mob.getNavigation().isInProgress()) return;
                //Try a maximum of 5 times to move towards open sky.
                for(int i = 0; i < 5; i++){
                    Vector3d pos = RandomPositionGenerator.getLandPosAvoid(this.mob, 10, 7, target.position());
                    if(pos != null && target.distanceToSqr(pos) < attackRadiusSqr && checkVicinity(target, pos)){
                        if(this.mob.getNavigation().moveTo(pos.x, pos.y, pos.z, 1)){
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean checkVicinity(@Nonnull LivingEntity target, Vector3d pos) {
        double targetX = (target.getX() - pos.x) / 2 + pos.x;
        double targetY = pos.y + 15;
        double targetZ = (target.getZ() - pos.z) / 2 + pos.z;
        BlockRayTraceResult rayTraceResult = this.mob.level.clip(new RayTraceContext(pos.add(0, 1.7, 0),
                new Vector3d(targetX, targetY, targetZ), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE,
                this.mob));
        return rayTraceResult.getType() == RayTraceResult.Type.MISS;
    }
}
