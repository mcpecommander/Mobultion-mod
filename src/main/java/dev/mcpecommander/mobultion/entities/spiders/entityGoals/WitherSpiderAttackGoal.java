package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import dev.mcpecommander.mobultion.entities.spiders.entities.RedEyeEntity;
import dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import static dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity.Head.LEFT;
import static dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity.Head.RIGHT;

/* McpeCommander created on 10/05/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entityGoals */
public class WitherSpiderAttackGoal extends Goal {
    protected final WitherSpiderEntity attacker;
    private final double speedMultiplier, jumpHeight, jumpSpeed;
    private Path path;
    private int ticksUntilNextAttack;
    private long lastCanUseCheck;
    private int leftSeeTime, rightSeeTime;
    private int leftAttackTime = -1, rightAttackTime = -1, leftAttackCount, rightAttackCount;
    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20;

    /**
     * Initiate this in register goals
     * @param attacker The attacker entity, almost always "this"
     * @param speedMultiplier The speed multiplier when hunting the target, get multiplied by the speed so 1.5 is 50%
     *                        faster speed when running after target
     * @param jumpHeight The height of the spider leap, vanilla uses 0.3f
     * @param jumpSpeed A float to multiply the jump vector by, vanilla uses 0.4f
     */
    public WitherSpiderAttackGoal(WitherSpiderEntity attacker, double speedMultiplier, float jumpHeight, float jumpSpeed) {
        this.attacker = attacker;
        this.speedMultiplier = speedMultiplier;
        this.jumpHeight = jumpHeight;
        this.jumpSpeed = jumpSpeed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * If the goal/AI can start when the conditions are met.
     * @return true if the goal can start and then start() is called.
     */
    public boolean canUse() {
        long i = this.attacker.level.getGameTime();
        //Check the use cool down before initiating the attack AI.
        //Minimizes the lag but not too long that the entity feels laggy.
        if (i - this.lastCanUseCheck < COOLDOWN_BETWEEN_CAN_USE_CHECKS) {
            return false;
        } else {
            //Reset the timer
            this.lastCanUseCheck = i;
            LivingEntity mainTarget = this.attacker.getTarget();
            LivingEntity leftTarget = this.attacker.getTarget(LEFT);
            LivingEntity rightTarget = this.attacker.getTarget(RIGHT);
            if ((mainTarget == null || !mainTarget.isAlive()) && (leftTarget == null || !leftTarget.isAlive()) &&
                    (rightTarget == null || !rightTarget.isAlive())) {
                return false;
            } else {
                boolean mainTargetFlag = false;
                if(mainTarget != null && mainTarget.isAlive()){
                    //Try to create a path to the target.
                    //0 is the number of blocks of leeway from the target and since it is melee, it is 0.
                    this.path = this.attacker.getNavigation().createPath(mainTarget, 0);
                    if (this.path != null) {
                        mainTargetFlag = true;
                    } else {
                        //If the path is null, it could be either that the target is unreachable or attacker is already at target.
                        mainTargetFlag = this.getAttackReachSqr(mainTarget) >= this.attacker.distanceToSqr(mainTarget.getX(), mainTarget.getY(), mainTarget.getZ());
                    }
                }
                //Attack if either one of the heads can attack.
                return mainTargetFlag || leftTarget != null || rightTarget != null;
            }
        }
    }

    /**
     * Gets called after every tick to make sure the goal/AI can continue.
     * @return true if the goal/AI can continue to tick. Calls tick() if true or stop() if false.
     */
    public boolean canContinueToUse() {
        LivingEntity mainTarget = this.attacker.getTarget();
        LivingEntity leftTarget = this.attacker.getTarget(LEFT);
        LivingEntity rightTarget = this.attacker.getTarget(RIGHT);
        if ((mainTarget == null || !mainTarget.isAlive()) && (leftTarget == null || !leftTarget.isAlive()) &&
                (rightTarget == null || !rightTarget.isAlive())) {
            return false;
        } else {
            //Check whether the target switched to creative after being targeted.
            return checkCreativePlayer(mainTarget, leftTarget, rightTarget);
        }
    }

    /**
     * Checks if the targets are players and whether the target is in creative or spectator and reset the targets accordingly.
     * @param mainTarget The middle head target.
     * @param leftTarget The left head target.
     * @param rightTarget The right head target.
     * @return false if all the targets are creative or spectator players.
     */
    private boolean checkCreativePlayer(LivingEntity mainTarget, LivingEntity leftTarget, LivingEntity rightTarget){
        boolean flag = true;
        boolean flag1 = true;
        boolean flag2 = true;
        if(mainTarget instanceof Player player && (player.isSpectator() || player.isCreative())){
            this.attacker.setTarget(null);
            flag = false;
        }
        if(leftTarget instanceof Player player && (player.isSpectator() || player.isCreative())){
            this.attacker.setTarget(LEFT, null);
            flag1 = false;
        }
        if(rightTarget instanceof Player player && (player.isSpectator() || player.isCreative())){
            this.attacker.setTarget(RIGHT, null);
            flag2 = false;
        }
        return flag || flag1 || flag2;
    }

    /**
     * Gets called once after the canUse() is true.
     * Used to set some variables or timers for the goal/AI.
     */
    public void start() {
        //this.attacker.getNavigation().moveTo(this.path, this.speedMultiplier);
        this.attacker.setAggressive(true);
        this.ticksUntilNextAttack = 0;
        int count = Math.min(Mth.ceil(this.attacker.level.getCurrentDifficultyAt(
                this.attacker.blockPosition()).getEffectiveDifficulty()) * (attacker.getRandom().nextInt(2) + 2), 10);
        this.rightAttackCount = count;
        this.leftAttackCount = count;
    }

    /**
     * Gets called when the canContinueToUse() returns false and allows for final adjustments of the variables before
     * the goal is finished.
     */
    public void stop() {
        this.attacker.setTarget(null);
        this.attacker.setTarget(LEFT, null);
        this.attacker.setTarget(RIGHT, null);

        this.attacker.setAggressive(false);
        this.attacker.getNavigation().stop();
    }

    /**
     * Used to prioritize AI goals with their need of updates every tick to lower lag on unnecessary goals.
     * @return true if this goal needs to be ticked every server tick
     */
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    /**
     * Gets called every server tick as long as canContinueToUse() return true but is guaranteed to get called at least
     * once after the initial start()
     */
    public void tick() {
        LivingEntity mainTarget = this.attacker.getTarget();
        if (mainTarget != null && (!(mainTarget instanceof Player player) || (!player.isCreative() && !player.isCreative()))) {
            double distanceToTargetSqr = this.attacker.distanceToSqr(mainTarget.getX(), mainTarget.getY(), mainTarget.getZ());
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            //Check the distance for the leap
            if (!(distanceToTargetSqr < 4.0D) && !(distanceToTargetSqr > 16.0D)) {
                //Only jump if already on ground otherwise we get infinite jumps and only 10% of times, so we don't overdo it.
                if (this.attacker.isOnGround() && this.attacker.getRandom().nextInt(10) == 0) {
                    //Some distance calculation and speed additions.
                    Vec3 currentSpeedVector = this.attacker.getDeltaMovement();
                    Vec3 distanceVector = new Vec3(mainTarget.getX() - this.attacker.getX(), 0.0D, mainTarget.getZ() - this.attacker.getZ());
                    if (distanceVector.lengthSqr() > 1.0E-7D) {
                        distanceVector = distanceVector.normalize().scale(this.jumpSpeed).add(currentSpeedVector.scale(0.2D));
                    }
                    //The actual jump
                    this.attacker.setDeltaMovement(distanceVector.x, this.jumpHeight, distanceVector.z);
                }
            }
            //Because of the jump, the spider loses its path sometimes, so we refresh if it happens.
            if(this.attacker.isOnGround() && this.attacker.getNavigation().isDone()){
                //this.attacker.getNavigation().moveTo(mainTarget, this.speedMultiplier);
            }
            //Keep looking at the target.
            this.attacker.getLookControl().setLookAt(mainTarget, 30.0F, 30.0F);
            this.checkAndPerformAttack(mainTarget, distanceToTargetSqr);

        }
        LivingEntity leftTarget = this.attacker.getTarget(LEFT);
        if(leftTarget != null && (!(leftTarget instanceof Player player) || (!player.isCreative() && !player.isCreative()))){
            //this.attacker.lookAt(leftTarget, 15, 15, 0);
            //The current distance to the target.
            double leftTargetDistance = this.attacker.distanceToSqr(leftTarget.getX(), leftTarget.getY(), leftTarget.getZ());
            //If the attacker can see the target.
            boolean canSeeLeft = this.attacker.getSensing().hasLineOfSight(leftTarget);
            //If the attacker has seen the target, the see time increases otherwise it is decreased
            boolean hasSeenLeft = this.leftSeeTime > 0;
            //If the target has not been seen for a long time, reset the seeTime.
            if (canSeeLeft != hasSeenLeft) {
                this.leftSeeTime = 0;
            }
            //If the attacker can see the target, increase the seeTime otherwise decrease it.
            if (canSeeLeft) {
                ++this.leftSeeTime;
            } else {
                --this.leftSeeTime;
            }
            if (leftTargetDistance < 400 && --this.leftAttackTime <= 0) {
                //If the attacker cannot see the target for over 60 ticks, then stop using the item.
                if (!canSeeLeft && this.leftSeeTime < -60) {
                    this.leftAttackTime = 20;
                } else if (canSeeLeft) {
                    //If the attacker attack count (AKA: eyes count) is positive then tick through them.
                    if(this.leftAttackCount > 0) {
                        //Only fire each other time to give a spray effect.
                        if(this.leftAttackCount % 2 == 0) {
                            this.performRangedAttack(leftTarget, this.attacker.getHead(LEFT), LEFT);
                        }
                        this.leftAttackCount --;
                    }else{
                        //If the attack count is 0 then reset the attack timer and reset the attack count.
                        this.leftAttackTime = 20;
                        this.leftAttackCount = Math.min(Mth.ceil(this.attacker.level.getCurrentDifficultyAt(
                                this.attacker.blockPosition()).getEffectiveDifficulty()) * (attacker.getRandom().nextInt(2) + 2), 10);
                    }
                }
            }
        }
        LivingEntity rightTarget = this.attacker.getTarget(RIGHT);
        if(rightTarget != null && (!(rightTarget instanceof Player player) || (!player.isCreative() && !player.isCreative()))){
            //The current distance to the target.
            double rightTargetDistance = this.attacker.distanceToSqr(rightTarget.getX(), rightTarget.getY(), rightTarget.getZ());
            //If the attacker can see the target.
            boolean canSeeRight = this.attacker.getSensing().hasLineOfSight(rightTarget);
            //If the attacker has seen the target, the see time increases otherwise it is decreased
            boolean hasSeenRight = this.rightSeeTime > 0;
            //If the target has not been seen for a long time, reset the seeTime.
            if (canSeeRight != hasSeenRight) {
                this.rightSeeTime = 0;
            }
            //If the attacker can see the target, increase the seeTime otherwise decrease it.
            if (canSeeRight) {
                ++this.rightSeeTime;
            } else {
                --this.rightSeeTime;
            }
            if (rightTargetDistance < 400 && --this.rightAttackTime <= 0) {
                //If the attacker cannot see the target for over 60 ticks, then reset the attack time.
                if (!canSeeRight && this.rightSeeTime < -60) {
                    this.rightAttackTime = 20;
                } else if (canSeeRight) {
                    //If the attacker attack count (AKA: eyes count) is positive then tick through them.
                    if(this.rightAttackCount > 0) {
                        //Only fire each other time to give a spray effect.
                        if(this.rightAttackCount % 2 == 0) {
                            this.performRangedAttack(rightTarget, this.attacker.getHead(RIGHT), RIGHT);
                        }
                        this.rightAttackCount--;
                    }else{
                        //If the attack count is 0 then reset the attack timer and reset the attack count.
                        this.rightAttackTime = 20;
                        this.rightAttackCount = Math.min(Mth.ceil(this.attacker.level.getCurrentDifficultyAt(
                                this.attacker.blockPosition()).getEffectiveDifficulty()) * (attacker.getRandom().nextInt(2) + 2), 10);
                    }
                }
            }
        }
    }

    /**
     * Perform the ranged attack of the left and right heads.
     * @param target The target being targeted by that specific head.
     * @param spawnPos The position to spawn the projectile at.
     */
    private void performRangedAttack(LivingEntity target, Vec3 spawnPos, WitherSpiderEntity.Head head){
//        if(head == LEFT) {
        if(head == LEFT) return;
        if(!this.attacker.getDeployed(head)){
            RedEyeEntity redEye = new RedEyeEntity(Registration.REDEYE.get(), this.attacker.level);
            redEye.setPos(spawnPos);
            redEye.setOwner(this.attacker);
            redEye.setLaunching(true);
            redEye.setHead(head);
            redEye.setTarget(target);
            this.attacker.level.addFreshEntity(redEye);
            this.attacker.setDeployed(head, true);
        }

//        }else{
//            WitheringWebEntity web = new WitheringWebEntity(Registration.WITHERINGWEB.get(), spawnPos.x, spawnPos.y, spawnPos.z,
//                    (target.getX() - spawnPos.x)/2f, (target.getEyeY() - spawnPos.y)/2f, (target.getZ() - spawnPos.z)/2f,
//                    target.level, this.attacker, target);
//            this.attacker.level.addFreshEntity(web);
//        }

    }

    /**
     * Checks for the distance to actually hit the player and for the animation to start
     * @param target The target reference
     * @param distanceToTargetSqr The current distance to the target
     */
    protected void checkAndPerformAttack(LivingEntity target, double distanceToTargetSqr) {
        double attackReachSqr = this.getAttackReachSqr(target);
        if (distanceToTargetSqr <= attackReachSqr && this.ticksUntilNextAttack <= 0) {
            this.ticksUntilNextAttack = 20;
            this.attacker.doHurtTarget(target);
        }
    }

    /**
     * An even simpler equation to calculate the "hit" box of the attacker
     * @param target The target reference
     * @return true if the target is within almost 2 blocks of the spider
     */
    private double getAttackReachSqr(LivingEntity target) {
        return 4.0F + target.getBbWidth();
    }

}