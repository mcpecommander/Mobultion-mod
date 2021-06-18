package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import dev.mcpecommander.mobultion.entities.spiders.entities.MobultionSpiderEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumSet;

/* Created by McpeCommander on 2021/06/18 */
public class AngelSpiderTargetGoal extends TargetGoal {

    private final int targetChance;
    protected MobEntity targetEntity;

    public AngelSpiderTargetGoal(MobEntity spider) {
        super(spider, true, false);
        this.targetChance = 10;
        this.setFlags(EnumSet.of(Flag.TARGET));

    }

    protected AxisAlignedBB getTargetSearchArea(double range) {
        return this.mob.getBoundingBox().inflate(range, 4.0D, range);
    }

    @Override
    public boolean canUse() {
        if (this.targetChance > 0 && this.mob.getRandom().nextInt(this.targetChance) != 0) {
            return false;
        } else if (this.mob.getTarget() != null) {
            return false;
        } else {
            this.targetEntity = this.mob.level.getNearestLoadedEntity(MobultionSpiderEntity.class,
                    EntityPredicate.DEFAULT.selector(livingEntity -> livingEntity.getHealth() < livingEntity.getMaxHealth()),
                    this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(),
                    this.getTargetSearchArea(this.getFollowDistance()));
            if(this.targetEntity == null) this.targetEntity = this.mob.level.getNearestLoadedEntity(SpiderEntity.class, EntityPredicate.DEFAULT, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(),
                    this.getTargetSearchArea(this.getFollowDistance()));
            return this.targetEntity != null;
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }

}
