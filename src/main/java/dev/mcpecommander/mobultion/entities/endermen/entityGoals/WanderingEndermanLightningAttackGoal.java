package dev.mcpecommander.mobultion.entities.endermen.entityGoals;

import dev.mcpecommander.mobultion.entities.endermen.entities.WanderingEndermanEntity;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

/* McpeCommander created on 27/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entityGoals */
public class WanderingEndermanLightningAttackGoal extends Goal {

    WanderingEndermanEntity owner;
    World world;
    LivingEntity target;
    BlockPos lightningTarget;
    int maxCooldown, cooldown;
    int teleportCooldown;

    public WanderingEndermanLightningAttackGoal(WanderingEndermanEntity owner, int cooldown){
        this.owner = owner;
        this.world = owner.level;
        this.maxCooldown = cooldown;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(this.owner.isAlive() && this.owner.getTarget() != null){
            return isWandInHand();
        }
        return false;
    }

    @Override
    public void start() {
        this.target = this.owner.getTarget();
        this.cooldown = this.maxCooldown;
    }

    @Override
    public void stop() {
        this.target = null;
        this.lightningTarget = null;
        this.teleportCooldown = 0;
        this.owner.setTarget(null);
        this.owner.getNavigation().stop();
        this.owner.setCasting(false);
    }

    @Override
    public boolean canContinueToUse() {
        return this.owner.isAlive() && this.target.isAlive() && this.isWandInHand();
    }

    @Override
    public void tick() {
        double distance = this.owner.distanceToSqr(this.target);
        if(!this.owner.getNavigation().isInProgress()){
            if (distance > 256d){
                Path path = this.owner.getNavigation().createPath(this.target, 20);
                if(path != null){
                    this.owner.getNavigation().moveTo(path, this.owner.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    this.lightningTarget = null;
                }
            }else{
                if(lightningTarget == null || cooldown == 5) lightningTarget = this.target.blockPosition();
                if(this.cooldown-- == 0){
                    LightningBoltEntity bolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
                    bolt.setPos(lightningTarget.getX(), lightningTarget.getY(), lightningTarget.getZ());
                    this.world.addFreshEntity(bolt);
                    this.cooldown = maxCooldown;
                    lightningTarget = null;
                    this.owner.setCasting(false);
                }
                if(this.cooldown == 36) this.owner.setCasting(true);
            }
        }else if(distance < 200d){
            this.owner.getNavigation().stop();
        }
        if(distance < 64d){
            if(this.owner.tickCount - this.teleportCooldown > 120) {
                if(this.owner.teleport()) this.teleportCooldown = this.owner.tickCount;
            }
        }
    }

    private boolean isWandInHand(){
        return this.owner.getItemBySlot(EquipmentSlotType.MAINHAND).getItem() == Registration.THUNDERSTAFF.get();
    }
}