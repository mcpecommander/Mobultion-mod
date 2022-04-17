package dev.mcpecommander.mobultion.entities.skeletons.entityGoals;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

/* McpeCommander created on 16/04/2022 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entityGoals */
public class AnimatedMeleeAttackGoal extends Goal {
    protected final PathfinderMob attacker;
    private final double speedMultiplier;
    private final boolean followingTargetEvenIfNotSeen;
    private Path path;
    private int ticksUntilNextAttack;
    private long lastCanUseCheck;
    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20;

    /**
     * Initiate this in register goals
     * @param attacker The attacker entity, almost always "this"
     * @param speedMultiplier The speed multiplier when hunting the target, get multiplied by the speed so 1.5 is 50%
     *                        faster speed when running after target
     * @param followWhenUnseen Should this mob automagically find the target even when not seeing it for a while
     */
    public AnimatedMeleeAttackGoal(PathfinderMob attacker, double speedMultiplier, boolean followWhenUnseen) {
        this.attacker = attacker;
        this.speedMultiplier = speedMultiplier;
        this.followingTargetEvenIfNotSeen = followWhenUnseen;
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
            LivingEntity livingentity = this.attacker.getTarget();
            if (livingentity == null || !livingentity.isAlive()) {
                return false;
            } else {
                //Try to create a path to the target.
                //0 is the number of blocks of leeway from the target and since it is melee, it is 0.
                this.path = this.attacker.getNavigation().createPath(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    //If the path is null, it could be either that the target is unreachable or attacker is already at target.
                    return this.getAttackReachSqr(livingentity) >= this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                }
            }
        }
    }

    /**
     * Gets called after every tick to make sure the goal/AI can continue.
     * @return true if the goal/AI can continue to tick. Calls tick() if true or stop() if false.
     */
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (livingentity == null || !livingentity.isAlive()) {
            return false;
            //Check if the attacker should continue following after reaching the end node and not seeing the player anymore.
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.attacker.getNavigation().isDone();
        } else {
            //Check whether the target switched to creative after being targeted.
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
        }
    }

    /**
     * Gets called once after the canUse() is true.
     * Used to set some variables or timers for the goal/AI.
     */
    public void start() {
        this.attacker.getNavigation().moveTo(this.path, this.speedMultiplier);
        this.attacker.setAggressive(true);
        this.ticksUntilNextAttack = 0;
    }

    /**
     * Gets called when the canContinueToUse() returns false and allows for final adjustments of the variables before
     * the goal is finished.
     */
    public void stop() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.attacker.setTarget(null);
        }

        this.attacker.setAggressive(false);
        this.attacker.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    /**
     * Gets called every server tick as long as canContinueToUse() return true but is guaranteed to get called at least
     * once after the initial start()
     */
    public void tick() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (livingentity != null) {
            this.attacker.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            double distanceToTargetSqr = this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(livingentity, distanceToTargetSqr);
        }
    }

    /**
     * Checks for the distance to actually hit the player and for the animation to start
     * @param target The target reference
     * @param distanceToTargetSqr The current distance to the target
     */
    protected void checkAndPerformAttack(LivingEntity target, double distanceToTargetSqr) {
        double attackReachSqr = this.getAttackReachSqr(target);
        if(distanceToTargetSqr < 8){
            this.attacker.swing(InteractionHand.MAIN_HAND);
        }
        if (distanceToTargetSqr <= attackReachSqr && this.ticksUntilNextAttack <= 0) {
            this.ticksUntilNextAttack = 20;
            this.attacker.doHurtTarget(target);
        }

    }

    /**
     * Simple math equation to calculate a basic intersection between the target and the attacker
     * @param target The target reference
     * @return true if the target is within a block of the attacker
     */
    private double getAttackReachSqr(LivingEntity target) {
        return this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + target.getBbWidth();
    }
}