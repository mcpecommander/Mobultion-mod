package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import dev.mcpecommander.mobultion.entities.spiders.entities.AngelSpiderEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

import java.util.EnumSet;

/* Created by McpeCommander on 2021/06/18 */
public class AngelSpiderHealGoal extends Goal {

    World world;
    private final AngelSpiderEntity mob;
    private int healCoolDown;

    public AngelSpiderHealGoal(AngelSpiderEntity mob){
        this.mob = mob;
        this.world = mob.level;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

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

    @Override
    public void start() {
        if (this.mob != null && !this.mob.isDeadOrDying()) {
            if (this.mob.getTarget() != null && !this.mob.getTarget().isDeadOrDying()) {
                this.healCoolDown = 100;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob == null || this.mob.isDeadOrDying() || this.mob.getTarget() == null
                || (this.mob.getTarget() != null && this.mob.getTarget().getHealth() >= this.mob.getTarget().getMaxHealth())) {
            return false;
        } else {
            return this.healCoolDown >= 0;
        }
    }

    @Override
    public void tick() {
        this.healCoolDown -= 1;
        if (this.mob.getTarget() != null && !this.mob.getTarget().isDeadOrDying()) {
            LivingEntity entity = this.mob.getTarget();
            this.mob.getLookControl().setLookAt(entity, 30f, 30f);
            if (this.healCoolDown < 10) {
                entity.heal(4f);
                //TODO: entity.heal(SpidersConfig.spiders.angel.healAmount);
                    //TODO: fix the particles and redo the attribute system
//                    WorldServer temp = (WorldServer) this.mob.getEntityWorld();
//                    temp.spawnParticle(EnumParticleTypes.HEART, true, this.mob.getTarget().posX,
//                            this.mob.getTarget().posY + this.mob.getTarget().getEyeHeight(),
//                            this.mob.getTarget().posZ, 2, 0.0f, 0.0f, 0.0f, 0.01f);

//                    IAttributeInstance iattributeinstance = entity.getAttributes()
//                            .getAttributeInstanceByName("generic.blessed");
//                    if (!iattributeinstance.hasModifier(BLESSED) && iattributeinstance.getAttributeValue() == 0.0D) {
//                        iattributeinstance.applyModifier(BLESSED);
//                    }
                this.healCoolDown = 0;
            }
        }
    }

    @Override
    public void stop() {
        this.mob.setTarget(null);

    }
}
