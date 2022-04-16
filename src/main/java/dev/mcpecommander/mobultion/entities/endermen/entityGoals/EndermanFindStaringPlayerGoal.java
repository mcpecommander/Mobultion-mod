package dev.mcpecommander.mobultion.entities.endermen.entityGoals;

import dev.mcpecommander.mobultion.entities.endermen.entities.MobultionEndermanEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.function.Predicate;

/* McpeCommander created on 28/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entityGoals */
public class EndermanFindStaringPlayerGoal extends NearestAttackableTargetGoal<Player> {

    MobultionEndermanEntity enderman;
    Player possibleTarget;
    TargetingConditions condition;

    public EndermanFindStaringPlayerGoal(MobultionEndermanEntity endermanEntity, @Nonnull Predicate<LivingEntity> extraCondition) {
        super(endermanEntity, Player.class, false);
        this.setFlags(EnumSet.of(Flag.TARGET));
        this.enderman = endermanEntity;
        this.condition = TargetingConditions.forCombat().range(this.getFollowDistance()).
                selector(((Predicate<LivingEntity>) livingEntity -> enderman.isLookingAtMe((Player) livingEntity)).and(extraCondition));
    }

    /**
     * If the goal/AI can start when the conditions are met.
     * @return true if the goal can start and then start() is called.
     */
    @Override
    public boolean canUse() {
        this.possibleTarget = this.enderman.level.getNearestPlayer(condition, this.mob);
        return this.possibleTarget != null;
    }

    /**
     * Gets called after every tick to make sure the goal/AI can continue.
     * @return true if the goal/AI can continue to tick. Calls tick() if true or stop() if false.
     */
    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.condition.test(this.enderman, this.target) || super.canContinueToUse();
    }

    /**
     * Gets called once after the canUse() is true.
     * Used to set some variables or timers for the goal/AI.
     */
    @Override
    public void start() {
        this.enderman.setBeingStaredAt();
        this.target = possibleTarget;
        this.possibleTarget = null;
        super.start();
    }

    /**
     * Gets called when the canContinueToUse() returns false and allows for final adjustments of the variables before
     * the goal is finished.
     */
    @Override
    public void stop() {
        super.stop();
        this.possibleTarget = null;
    }


}
