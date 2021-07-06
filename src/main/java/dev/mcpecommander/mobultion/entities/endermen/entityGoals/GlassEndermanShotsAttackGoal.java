package dev.mcpecommander.mobultion.entities.endermen.entityGoals;

import dev.mcpecommander.mobultion.entities.endermen.entities.GlassEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.entities.GlassShotEntity;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;

import java.util.EnumSet;

/* McpeCommander created on 04/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entityGoals */
public class GlassEndermanShotsAttackGoal extends Goal {

    private final GlassEndermanEntity endermanEntity;
    private int timer, teleportCooldown;

    public GlassEndermanShotsAttackGoal(GlassEndermanEntity endermanEntity){
        this.endermanEntity = endermanEntity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(endermanEntity.getBalls() == 0) return false;
        LivingEntity target = this.endermanEntity.getTarget();
        return target != null && target.isAlive() && endermanEntity.canAttack(target);
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && endermanEntity.getBalls() > 0;
    }

    @Override
    public void start() {
        timer = 30;
        teleportCooldown = 0;
    }

    @Override
    public void stop() {
        endermanEntity.setTarget(null);
    }

    @Override
    public void tick() {
        timer --;
        LivingEntity target = endermanEntity.getTarget();
        endermanEntity.lookAt(target, 30f, 30f);
        if(timer <= 0){
            timer = 30;
            if(endermanEntity.canSee(target)){
                GlassShotEntity shot = new GlassShotEntity(Registration.GLASSSHOT.get(), endermanEntity.getX(),
                        endermanEntity.getEyeY() + 1, endermanEntity.getZ(),
                        (target.getX() - endermanEntity.getX()) / 2d,
                        (target.getY() - endermanEntity.getEyeY()) / 2d,
                        (target.getZ() - endermanEntity.getZ()) / 2d,
                        endermanEntity.level, endermanEntity);
                endermanEntity.level.addFreshEntity(shot);
                endermanEntity.useBall();
            }
        }else{
            double distance = endermanEntity.distanceToSqr(target);
            if(distance < 16d){
                if(endermanEntity.tickCount - teleportCooldown > 100){
                    for(int i = 0; i < 3; i++){
                        if(endermanEntity.teleportAround(target)) break;
                    }
                    this.teleportCooldown = endermanEntity.tickCount;
                }
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
