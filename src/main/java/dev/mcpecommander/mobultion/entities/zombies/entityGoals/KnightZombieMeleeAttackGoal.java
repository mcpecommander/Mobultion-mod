package dev.mcpecommander.mobultion.entities.zombies.entityGoals;

import dev.mcpecommander.mobultion.entities.zombies.entities.KnightZombieEntity;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.easing.EasingManager;
import software.bernie.geckolib3.core.easing.EasingType;

import java.util.EnumSet;

/* McpeCommander created on 11/06/2022 inside the package - dev.mcpecommander.mobultion.entities.zombies.entityGoals */
public class KnightZombieMeleeAttackGoal extends Goal {

    protected final KnightZombieEntity attacker;
    private Vec3 chargingAngle;
    private final double speedMultiplier;
    private Path path;
    private long lastCanUseCheck;
    private int ticksUntilNextAttack, ticksUntilNextCharge;
    private int chargingTime;
    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20;

    /**
     * Initiate this in register goals
     * @param attacker The attacker entity, almost always "this"
     * @param speedMultiplier The speed multiplier when hunting the target, get multiplied by the speed so 1.5 is 50%
     *                        faster speed when running after target
     */
    public KnightZombieMeleeAttackGoal(KnightZombieEntity attacker, double speedMultiplier) {
        this.attacker = attacker;
        this.speedMultiplier = speedMultiplier;
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
        } else if (this.attacker.getNavigation().isStuck()) {
            System.out.println("stuck");
            return false;
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
        this.chargingTime = 0;
        this.ticksUntilNextCharge = 0;
        this.attacker.setCharging(false);
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
        if (target != null) {

            this.attacker.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double distanceToTargetSqr = this.attacker.distanceToSqr(target.getX(), target.getY(), target.getZ());
            if(!target.blockPosition().equals(this.attacker.getNavigation().getTargetPos())){
                resetPath(target);
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            if(!this.attacker.isCharging() && ticksUntilNextCharge-- < 0 && distanceToTargetSqr < 25 && this.attacker.getBlockY() == target.getBlockY()){
                this.attacker.setCharging(true);
                this.chargingAngle = target.position().subtract(this.attacker.position()).normalize();
                chargingTime = 0;
            }
            if(this.attacker.isCharging()){
                chargingTime ++;
                this.attacker.setDeltaMovement(this.chargingAngle.scale(EasingManager.ease(this.chargingTime/8f, EasingType.EaseOutCubic, null)));
                this.attacker.getLookControl().setLookAt(this.chargingAngle.add(this.attacker.position()));
                if(chargingTime == 8) {
                    this.attacker.setCharging(false);
                    this.ticksUntilNextCharge = 40;
                    this.attacker.setDeltaMovement(0, 0, 0);
                    resetPath(target);
                }

            }
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
        if (distanceToTargetSqr <= attackReachSqr ) {
            if(this.ticksUntilNextAttack <= 0) {
                this.ticksUntilNextAttack = 20;
                this.attacker.doHurtTarget(target);
            }
        } else if (this.attacker.getNavigation().isDone()){
            resetPath(target);
        }
    }

    private void resetPath(LivingEntity target){
        path = this.attacker.getNavigation().createPath(target, 0);
        if(path != null) this.attacker.getNavigation().moveTo(this.path, this.speedMultiplier);
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
