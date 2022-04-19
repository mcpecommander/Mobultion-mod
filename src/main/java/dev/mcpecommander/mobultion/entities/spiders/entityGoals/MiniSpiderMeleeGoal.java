package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/* McpeCommander created on 18/04/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entityGoals */
public class MiniSpiderMeleeGoal extends Goal {
    protected final PathfinderMob attacker;
    private final double speedMultiplier, jumpHeight, jumpSpeed;
    private Path path;
    private long lastCanUseCheck;
    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20;

    /**
     * Initiate this in register goals
     * @param attacker The attacker entity, almost always "this"
     * @param speedMultiplier The speed multiplier when hunting the target, get multiplied by the speed so 1.5 is 50%
     *                        faster speed when running after target
     * @param jumpHeight The height of the spider leap, vanilla uses 0.3f
     * @param jumpSpeed A float to multiply the jump vector by, vanilla uses 0.4f
     */
    public MiniSpiderMeleeGoal(PathfinderMob attacker, double speedMultiplier, float jumpHeight, float jumpSpeed) {
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
        } else {
            //Check whether the target switched to creative after being targeted.
            if(livingentity instanceof Player player && (player.isCreative() || player.isSpectator())){
                return false;
            }
            if(this.attacker.isPassenger()){
                if(this.attacker.getVehicle() != this.attacker.getTarget()){
                    this.attacker.setTarget((LivingEntity) this.attacker.getVehicle());
                }
                return true;
            }
            return true;
        }
    }

    /**
     * Gets called once after the canUse() is true.
     * Used to set some variables or timers for the goal/AI.
     */
    public void start() {
        this.attacker.getNavigation().moveTo(this.path, this.speedMultiplier);
        this.attacker.setAggressive(true);
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
        if(this.attacker.isPassenger()){
            Entity vehicle = this.attacker.getVehicle();
            this.attacker.unRide();
            if(vehicle instanceof ServerPlayer serverPlayer){
                serverPlayer.connection.send(new ClientboundSetPassengersPacket(serverPlayer));
            }
        }
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
        LivingEntity target = this.attacker.getTarget();
        if(this.attacker.isPassenger() && this.attacker.getVehicle() == target){
            if(this.attacker.tickCount % 20 == 0){
                assert target != null;
                this.attacker.doHurtTarget(target);
            }
            return;
        }
        if (target != null) {
            double distanceToTargetSqr = this.attacker.distanceToSqr(target.getX(), target.getY(), target.getZ());
            //Check the distance for the leap
            if (!(distanceToTargetSqr < 4.0D) && !(distanceToTargetSqr > 16.0D)) {
                //Only jump if already on ground otherwise we get infinite jumps and only 10% of times, so we don't overdo it.
                if (this.attacker.isOnGround() && this.attacker.getRandom().nextInt(10) == 0) {
                    //Some distance calculation and speed additions.
                    Vec3 currentSpeedVector = this.attacker.getDeltaMovement();
                    Vec3 distanceVector = new Vec3(target.getX() - this.attacker.getX(), 0.0D, target.getZ() - this.attacker.getZ());
                    if (distanceVector.lengthSqr() > 1.0E-7D) {
                        distanceVector = distanceVector.normalize().scale(this.jumpSpeed).add(currentSpeedVector.scale(0.2D));
                    }
                    //The actual jump
                    this.attacker.setDeltaMovement(distanceVector.x, this.jumpHeight, distanceVector.z);
                }
            }
            //Because of the jump, the spider loses its path sometimes, so we refresh if it happens.
            if(this.attacker.isOnGround() && this.attacker.getNavigation().isDone()){
                this.attacker.getNavigation().moveTo(target, this.speedMultiplier);
            }
            //Keep looking at the target.
            this.attacker.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.checkAndPerformAttack(target, distanceToTargetSqr);

        }
    }

    /**
     * Checks for the distance to actually hit the player and for the animation to start
     * @param target The target reference
     * @param distanceToTargetSqr The current distance to the target
     */
    protected void checkAndPerformAttack(LivingEntity target, double distanceToTargetSqr) {
        double attackReachSqr = this.getAttackReachSqr(target);
        if (distanceToTargetSqr <= attackReachSqr) {
            this.attacker.startRiding(target);
            if(target instanceof ServerPlayer serverPlayer){
                serverPlayer.connection.send(new ClientboundSetPassengersPacket(target));
            }
        }

    }

    /**
     * An even simpler equation to calculate the "hit" box of the attacker
     * @param target The target reference
     * @return true if the target is within almost 2 blocks of the spider
     */
    private double getAttackReachSqr(LivingEntity target) {
        return 1.5 + target.getBbWidth();
    }
}
