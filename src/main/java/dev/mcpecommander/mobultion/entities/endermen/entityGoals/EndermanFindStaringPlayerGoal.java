package dev.mcpecommander.mobultion.entities.endermen.entityGoals;

import dev.mcpecommander.mobultion.entities.endermen.entities.MobultionEndermanEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.function.Predicate;

/* McpeCommander created on 28/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entityGoals */
public class EndermanFindStaringPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {

    MobultionEndermanEntity enderman;
    PlayerEntity possibleTarget;
    EntityPredicate condition;

    public EndermanFindStaringPlayerGoal(MobultionEndermanEntity endermanEntity, @Nonnull Predicate<LivingEntity> extraCondition) {
        super(endermanEntity, PlayerEntity.class, false);
        this.setFlags(EnumSet.of(Flag.TARGET));
        this.enderman = endermanEntity;
        this.condition = new EntityPredicate().range(this.getFollowDistance()).
                selector(((Predicate<LivingEntity>) livingEntity -> enderman.isLookingAtMe((PlayerEntity) livingEntity)).and(extraCondition));
    }

    @Override
    public boolean canUse() {
        this.possibleTarget = this.enderman.level.getNearestPlayer(condition, this.mob);
        return this.possibleTarget != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.condition.test(this.enderman, this.target) || super.canContinueToUse();
    }

    @Override
    public void start() {
        this.enderman.setBeingStaredAt();
        this.target = possibleTarget;
        this.possibleTarget = null;
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        this.possibleTarget = null;
    }


}
