package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import dev.mcpecommander.mobultion.entities.spiders.entities.RedEyeEntity;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;

import java.util.EnumSet;

/* McpeCommander created on 25/05/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entityGoals */
public class RedEyeZapAttackGoal  extends Goal{
    protected final RedEyeEntity attacker;
    private int ticksUntilNextAttack;
    private long lastCanUseCheck;
    private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20;

    /**
     * Initiate this in register goals
     * @param attacker The attacker entity, almost always "this"
     */
    public RedEyeZapAttackGoal(RedEyeEntity attacker) {
        this.attacker = attacker;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * If the goal/AI can start when the conditions are met.
     * @return true if the goal can start and then start() is called.
     */
    public boolean canUse() {
        if(this.attacker.isLaunching()) return false;
        long i = this.attacker.level.getGameTime();
        //Check the use cool down before initiating the attack AI.
        //Minimizes the lag but not too long that the entity feels laggy.
        if (i - this.lastCanUseCheck < COOLDOWN_BETWEEN_CAN_USE_CHECKS) {
            return false;
        } else {
            //Reset the timer
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.attacker.getTarget();
            return livingentity != null && livingentity.isAlive();

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
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
        }
    }

    /**
     * Gets called once after the canUse() is true.
     * Used to set some variables or timers for the goal/AI.
     */
    public void start() {
        //Add some delay before the first attack, so it is fair against players who are too close when the eyes are spawned.
        this.ticksUntilNextAttack = 19;
        //Make sure that the aggressive state is reset when starting, although it is also reset in the stop method.
        this.attacker.setAggressive(false);
    }

    /**
     * Gets called when the canContinueToUse() returns false and allows for final adjustments of the variables before
     * the goal is finished.
     */
    public void stop() {
        this.attacker.setTarget(null);
        //Reset the aggressive state, so that it doesn't keep zapping if the goal got interrupted mid-attack.
        this.attacker.setAggressive(false);
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
            //Look at the target
            this.attacker.getLookControl().setLookAt(target);

            float tickCount = this.attacker.tickCount;
            //Calculate the radius in a spiral around the target
            double targetRadius = Math.max(2, Mth.abs(Mth.sin(tickCount%160f/160f * Mth.PI)) * 4);
            //Calculate the x and z positions using trigonometry in a circle around the target.
            double xTarget = Mth.cos(tickCount/40f * Mth.PI + this.attacker.head.number * Mth.PI) * targetRadius + target.getX();
            double zTarget = Mth.sin(tickCount/40f * Mth.PI + this.attacker.head.number * Mth.PI) * targetRadius + target.getZ();
            //Calculate the vector from the current position to the calculated position.
            Vec3 deltaMovement = new Vec3(xTarget - this.attacker.getX(), target.getY() + 2 - this.attacker.getY(), zTarget - this.attacker.getZ());
            //The length of the vector is the distance.
            double actualDistance = deltaMovement.length();
            //The magical deltaMovement.scale(0.2D/actualDistance) is to get equal speed no matter the distance.
            //Mth.sin(tickCount%30f/30f * Mth.TWO_PI) * 0.05d is the oscillating up and down movement.
            this.attacker.setDeltaMovement(deltaMovement.scale(0.2D/actualDistance)
                    .add(0, Mth.sin(tickCount%30f/30f * Mth.TWO_PI) * 0.05d, 0));

            //Only attack if the red eye is visible to the target (aka not in a block), the delay is over and the distance is close enough.
            if(this.attacker.hasLineOfSight(target) && --this.ticksUntilNextAttack < 0 && actualDistance <= 3){
                this.attacker.setAggressive(true);
                this.attacker.level.playSound(null, this.attacker.blockPosition(), Registration.ZAP_SOUND.get(), SoundSource.HOSTILE,
                        0.3f, (this.attacker.getRandom().nextFloat() - this.attacker.getRandom().nextFloat()) * 0.2F + 1.5F);
                this.ticksUntilNextAttack = 25;
            }
            //End of the visual part and start of the logical part of the attack.
            if(this.ticksUntilNextAttack == 20){
                this.attacker.setAggressive(false);
                target.hurt(DamageSource.mobAttack(this.attacker), 2.0F);
            }

        }
    }
}