package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;

import static dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity.Head.LEFT;
import static dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity.Head.RIGHT;

/* McpeCommander created on 07/05/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entityGoals */
public class ThreeAttackableTargetsGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final LivingEntity[] extraTargets = new LivingEntity[2];

    private final int[] unseenTicks = new int[3];

    public ThreeAttackableTargetsGoal(WitherSpiderEntity spider, Class<T> targetClass, boolean mustSee) {
        super(spider, targetClass, mustSee);
    }

    /**
     * If the goal/AI can start when the conditions are met.
     * @return true if the goal can start and then start() is called.
     */
    @Override
    public boolean canUse() {
        if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
            return false;
        } else {
            //Try to find and set the three different targets.
            this.findTarget();
            this.findTarget(LEFT);
            this.findTarget(RIGHT);
            //Continue with the task if at least one of the targets is not null.
            return this.target != null || this.extraTargets[LEFT.number] != null || this.extraTargets[RIGHT.number] != null;
        }
    }

    /**
     * Gets called once after the canUse() is true.
     * Used to set some variables or timers for the goal/AI.
     */
    @Override
    public void start() {
        //Set the targets to the different heads.
        ((WitherSpiderEntity)this.mob).setTarget(LEFT, extraTargets[LEFT.number]);
        ((WitherSpiderEntity)this.mob).setTarget(RIGHT, extraTargets[RIGHT.number]);
        //Reset the unseen ticks.
        this.unseenTicks[0] = 0;
        this.unseenTicks[1] = 0;
        this.unseenTicks[2] = 0;
        super.start();
    }

    /**
     * Gets called after every tick to make sure the goal/AI can continue.
     * @return true if the goal/AI can continue to tick. Calls tick() if true or stop() if false.
     */
    @Override
    public boolean canContinueToUse() {
        LivingEntity mainHeadTarget = this.mob.getTarget();
        LivingEntity leftHeadTarget = ((WitherSpiderEntity)this.mob).getTarget(LEFT);
        LivingEntity rightHeadTarget = ((WitherSpiderEntity)this.mob).getTarget(RIGHT);
        //Reset the target if the target is still available.
        if (mainHeadTarget == null) {
            mainHeadTarget = this.target;
        }
        if (leftHeadTarget == null) {
            leftHeadTarget = this.extraTargets[LEFT.number];
        }
        if (rightHeadTarget == null) {
            rightHeadTarget = this.extraTargets[RIGHT.number];
        }
        //If all three heads have null targets then reset the task.
        if (mainHeadTarget == null && leftHeadTarget == null && rightHeadTarget == null) {
            return false;
        //Reset the task if all the targets are
        } else if (!canAttackAndNotSameTeam(mainHeadTarget, leftHeadTarget, rightHeadTarget)) {
            return false;
        } else {
            double followDistance = this.getFollowDistance();
            if(leftHeadTarget != null){
                //If the target is out of range, reset the target.
                if (this.mob.distanceToSqr(leftHeadTarget) > followDistance * followDistance) {
                    ((WitherSpiderEntity)this.mob).setTarget(LEFT, null);
                    this.extraTargets[LEFT.number] = null;
                }else{
                    //Artifact of the vanilla class, kept for debugging.
                    if (this.mustSee) {
                        if (this.mob.getSensing().hasLineOfSight(leftHeadTarget)) {
                            this.unseenTicks[0] = 0;
                            //Reset the target if the spider hasn't seen it in around 30 ticks.
                        } else if (++this.unseenTicks[0] > reducedTickDelay(this.unseenMemoryTicks)) {
                            ((WitherSpiderEntity)this.mob).setTarget(LEFT, null);
                            this.extraTargets[LEFT.number] = null;
                        }
                    }
                }
            }

            if(rightHeadTarget != null){
                if (this.mob.distanceToSqr(rightHeadTarget) > followDistance * followDistance) {
                    ((WitherSpiderEntity)this.mob).setTarget(RIGHT, null);
                    this.extraTargets[RIGHT.number] = null;
                }else{
                    if (this.mustSee) {
                        if (this.mob.getSensing().hasLineOfSight(rightHeadTarget)) {
                            this.unseenTicks[1] = 0;
                        } else if (++this.unseenTicks[1] > reducedTickDelay(this.unseenMemoryTicks)) {
                            ((WitherSpiderEntity)this.mob).setTarget(RIGHT, null);
                            this.extraTargets[RIGHT.number] = null;
                        }
                    }
                }
            }

            if(mainHeadTarget != null){
                if (this.mob.distanceToSqr(mainHeadTarget) > followDistance * followDistance) {
                    this.mob.setTarget(null);
                    this.target = null;
                }else{
                    if (this.mustSee) {
                        if (this.mob.getSensing().hasLineOfSight(mainHeadTarget)) {
                            this.unseenTicks[2] = 0;
                        } else if (++this.unseenTicks[1] > reducedTickDelay(this.unseenMemoryTicks)) {
                            this.mob.setTarget(null);
                            this.target = null;
                        }
                    }
                }
            }
            //If after all the checks and resets the targets are all null then reset the task.
            if(this.target == null && this.extraTargets[LEFT.number] == null && this.extraTargets[RIGHT.number] == null){
                return false;
            }else{
                setAlternativeTargetMobs();
                return true;
            }
        }
    }

    /**
     * If the logic arrived to this method, we know that at least one of the heads has a non-null target.
     * In this case, apply that one target to the rest of the heads.
     */
    private void setAlternativeTargetMobs(){
        if(this.target == null){
            this.target = this.extraTargets[LEFT.number] != null ? this.extraTargets[LEFT.number] : this.extraTargets[RIGHT.number];
            this.mob.setTarget(target);
        }else{
            if(this.extraTargets[LEFT.number] == null){
                this.extraTargets[LEFT.number] = this.target;
                ((WitherSpiderEntity)this.mob).setTarget(LEFT, target);
            }
            if(this.extraTargets[RIGHT.number] == null){
                this.extraTargets[RIGHT.number] = this.target;
                ((WitherSpiderEntity)this.mob).setTarget(RIGHT, target);
            }
        }
    }

    /**
     * Checks if the targets are attackable and don't belong to the same team.
     * @param mainTarget The middle head target.
     * @param leftTarget The left head target.
     * @param rightTarget The right head target.
     * @return true if at least one of the targets is attackable and doesn't belong to the same team as the spider.
     */
    private boolean canAttackAndNotSameTeam(LivingEntity mainTarget, LivingEntity leftTarget, LivingEntity rightTarget){
        Team team = this.mob.getTeam();
        boolean flag = true;
        boolean flag1 = true;
        boolean flag2 = true;
        if(mainTarget != null){
            flag = this.mob.canAttack(mainTarget) && (team == null || team != mainTarget.getTeam());
            //If the target is not suitable then make it null.
            if(!flag){
                this.target = null;
                this.mob.setTarget(null);
            }
        }
        if(leftTarget != null){
            flag1 = this.mob.canAttack(leftTarget) && (team == null || team != leftTarget.getTeam());
            //If the target is not suitable then make it null.
            if(!flag1){
                this.extraTargets[LEFT.number] = null;
                ((WitherSpiderEntity)this.mob).setTarget(LEFT, null);
            }
        }
        if(rightTarget != null){
            flag2 = this.mob.canAttack(rightTarget) && (team == null || team != rightTarget.getTeam());
            //If the target is not suitable then make it null.
            if(!flag2){
                this.extraTargets[RIGHT.number] = null;
                ((WitherSpiderEntity)this.mob).setTarget(RIGHT, null);
            }
        }
        return flag || flag1 || flag2;
    }

    /**
     * A similar copy to the find target method but for the other two heads, has a check to try to get different targets
     * than the main head.
     * @param head The head that is searching for a target.
     */
    private void findTarget(WitherSpiderEntity.Head head){
        Vec3 headPos = ((WitherSpiderEntity) this.mob).getHead(head);
        if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
            this.extraTargets[head.number] = this.mob.level.getNearestEntity(this.mob.level.getEntitiesOfClass(this.targetType,
                            this.getTargetSearchArea(this.getFollowDistance()), (entity) -> entity !=
                                    this.extraTargets[head == LEFT ? 1 : 0] && entity != this.target),
                    this.targetConditions, this.mob, headPos.x, headPos.y, headPos.z);
        } else {
            this.extraTargets[head.number] = this.mob.level.getNearestPlayer(this.targetConditions.copy().
                            selector(entity -> entity != this.extraTargets[head == LEFT ? 1 : 0] && entity != this.target),
                    this.mob, headPos.x, headPos.y, headPos.z);
        }
    }

}
