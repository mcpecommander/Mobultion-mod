package dev.mcpecommander.mobultion.entities.endermen.entityGoals;

import dev.mcpecommander.mobultion.entities.endermen.entities.GlassEndermanEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;

/* McpeCommander created on 04/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entityGoals */
public class GlassEndermanShotsAttackGoal extends Goal {

    private final GlassEndermanEntity endermanEntity;
    private int timer, teleportCooldown;

    public GlassEndermanShotsAttackGoal(GlassEndermanEntity endermanEntity){
        this.endermanEntity = endermanEntity;
    }

    @Override
    public boolean canUse() {
        if(endermanEntity.getBalls() == 0) return false;
        LivingEntity target = this.endermanEntity.getTarget();
        return target != null && target.isAlive() && endermanEntity.canAttack(target);
    }

    @Override
    public void start() {
        timer = 30;
    }

    @Override
    public void tick() {
        timer --;
        LivingEntity target = endermanEntity.getTarget();
        if(timer <= 0){
            timer = 30;
            if(endermanEntity.canSee(target)){
//                GlassShotEntity shot = new GlassShotEntity(Registration.GLASSSHOT.get(), endermanEntity.getX(),
//                        endermanEntity.getEyeY() + 2, endermanEntity.getZ(), target.getX(), target.getEyeY(), target.getZ(),
//                        endermanEntity.level, endermanEntity);
//                shot.setPos(endermanEntity.getX(), endermanEntity.getEyeY(), endermanEntity.getZ());
//                shot.shoot(target.getX() - endermanEntity.getX(),
//                        target.getEyeY() - endermanEntity.getEyeY(),
//                        target.getZ() - endermanEntity.getZ(), 1.6f, 1);
//                endermanEntity.level.addFreshEntity(shot);
            }
        }else{
            double distance = endermanEntity.distanceToSqr(target);
            if(distance < 16d){
                if(endermanEntity.tickCount - teleportCooldown > 100){
                    for(int i = 0; i < 3; i++){
                        if(endermanEntity.teleport()) break;
                    }
                }
                this.teleportCooldown = endermanEntity.tickCount;
            }else if(distance > 512d){
                if(!endermanEntity.getNavigation().isInProgress()){
                    Path path = endermanEntity.getNavigation().createPath(target, 8);
                    if(path != null) endermanEntity.getNavigation().moveTo(path, endermanEntity.getAttributeValue(Attributes.MOVEMENT_SPEED));
                }
            }else{
                if(endermanEntity.getNavigation().isInProgress() && endermanEntity.getRandom().nextFloat() > 0.9f){
                    endermanEntity.getNavigation().stop();
                }
            }
        }
    }
}
