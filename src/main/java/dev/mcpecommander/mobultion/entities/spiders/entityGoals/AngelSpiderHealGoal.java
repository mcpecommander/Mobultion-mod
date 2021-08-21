package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import dev.mcpecommander.mobultion.entities.spiders.entities.AngelSpiderEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

import java.util.EnumSet;

/* Created by McpeCommander on 2021/06/18 */
public class AngelSpiderHealGoal extends Goal {

    World world;
    /**
     * The angel spider that will do this goal
     */
    private final AngelSpiderEntity mob;
    /**
     * A heal cool down to make sure the spider is not op and can instantly heal every spider
     */
    private int healCoolDown;

    public AngelSpiderHealGoal(AngelSpiderEntity mob){
        this.mob = mob;
        this.world = mob.level;
        //Sets the flags for something, just check other goals for comparison and which flags are appropriate.
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * If the goal/AI can start when the conditions are met.
     * @return true if the goal can start and then start() is called.
     */
    @Override
    public boolean canUse() {
        if (this.mob == null || this.mob.isDeadOrDying()) {
            return false;
        } else if (this.mob.getTarget() != null) {
            if (this.mob.getTarget().getHealth() < this.mob.getTarget().getMaxHealth()) {
                return this.mob.getHealth() > 4f;
            } else {
                this.mob.setTarget(null);
            }
        }
        return false;
    }

    /**
     * Gets called once after the canUse() is true.
     * Used to set some variables or timers for the goal/AI.
     */
    @Override
    public void start() {
        if (this.mob != null && !this.mob.isDeadOrDying()) {
            if (this.mob.getTarget() != null && !this.mob.getTarget().isDeadOrDying()) {
                this.healCoolDown = 100;
            }
        }
    }

    /**
     * Gets called after every tick to make sure the goal/AI can continue.
     * @return true if the goal/AI can continue to tick. Calls tick() if true or stop() if false.
     */
    @Override
    public boolean canContinueToUse() {
        if (this.mob == null || this.mob.isDeadOrDying() || this.mob.getTarget() == null
                || (this.mob.getTarget() != null && this.mob.getTarget().getHealth() >= this.mob.getTarget().getMaxHealth())) {
            return false;
        } else {
            return this.healCoolDown >= 0;
        }
    }

    /**
     * Gets called every server tick as long as canContinueToUse() return true but is guaranteed to get called at least
     * once after the initial start()
     */
    @Override
    public void tick() {
        this.healCoolDown -= 1;
        if (this.mob.getTarget() != null && !this.mob.getTarget().isDeadOrDying()) {
            LivingEntity entity = this.mob.getTarget();
            this.mob.getLookControl().setLookAt(entity, 30f, 30f);
            if (this.healCoolDown < 10) {
                entity.heal(4f);
                //TODO: entity.heal(SpidersConfig.spiders.angel.healAmount);
                this.healCoolDown = 0;
            }
        }
    }

    /**
     * Gets called when the canContinueToUse() returns false and allows for final adjustments of the variables before
     * the goal is finished.
     */
    @Override
    public void stop() {
        this.mob.setTarget(null);
    }
}
