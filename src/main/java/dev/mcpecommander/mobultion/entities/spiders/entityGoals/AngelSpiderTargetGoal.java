package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import dev.mcpecommander.mobultion.entities.spiders.entities.MobultionSpiderEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;

/* Created by McpeCommander on 2021/06/18 */
public class AngelSpiderTargetGoal extends TargetGoal {

    /**
     * The chance for this spider to start finding a target.
     */
    private final int targetChance;
    /**
     * The target entity that this spider will target to heal.
     */
    protected Mob targetEntity;

    public AngelSpiderTargetGoal(Mob spider) {
        super(spider, true, false);
        this.targetChance = 10;
        this.setFlags(EnumSet.of(Flag.TARGET));

    }

    /**
     * Copied from the target goal in zombies.
     * @param range: x and z parameters of the box
     * @return A box that is 4 high with x and z equals to the range plus the mob bounding box.
     */
    protected AABB getTargetSearchArea(double range) {
        return this.mob.getBoundingBox().inflate(range, 4.0D, range);
    }

    /**
     * Can the goal/AI start when the conditions are met.
     * @return true if the goal can start and then start() is called.
     */
    @Override
    public boolean canUse() {
        if (this.targetChance > 0 && this.mob.getRandom().nextInt(this.targetChance) != 0) {
            return false;
        } else if (this.mob.getTarget() != null) {
            return false;
        } else {
            this.targetEntity = this.mob.level.getNearestEntity(MobultionSpiderEntity.class,
                    TargetingConditions.DEFAULT.selector(livingEntity -> livingEntity.getHealth() < livingEntity.getMaxHealth()),
                    this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(),
                    this.getTargetSearchArea(this.getFollowDistance()));
            if(this.targetEntity == null) this.targetEntity = this.mob.level.getNearestEntity(Spider.class, TargetingConditions.DEFAULT,
                    this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(),
                    this.getTargetSearchArea(this.getFollowDistance()));
            return this.targetEntity != null;
        }
    }

    /**
     * Gets called once after the canUse() is true.
     * Used to set some variables or timers for the goal/AI.
     */
    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }

}
