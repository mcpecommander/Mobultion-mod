package dev.mcpecommander.mobultion.entities.endermen.entityGoals;

import dev.mcpecommander.mobultion.entities.endermen.entities.WanderingEndermanEntity;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

/* McpeCommander created on 27/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entityGoals */
public class WanderingEndermanLightningAttackGoal extends Goal {

    WanderingEndermanEntity owner;
    Level world;
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

    /**
     * If the goal/AI can start when the conditions are met.
     * @return true if the goal can start and then start() is called.
     */
    @Override
    public boolean canUse() {
        if(this.owner.isAlive() && this.owner.getTarget() != null){
            return isWandInHand();
        }
        return false;
    }

    /**
     * Gets called once after the canUse() is true.
     * Used to set some variables or timers for the goal/AI.
     */
    @Override
    public void start() {
        this.target = this.owner.getTarget();
        this.cooldown = this.maxCooldown;
    }

    /**
     * Gets called when the canContinueToUse() returns false and allows for final adjustments of the variables before
     * the goal is finished.
     */
    @Override
    public void stop() {
        this.target = null;
        this.lightningTarget = null;
        this.teleportCooldown = 0;
        this.owner.setTarget(null);
        this.owner.getNavigation().stop();
        this.owner.setCasting(false);
    }

    /**
     * Gets called after every tick to make sure the goal/AI can continue.
     * @return true if the goal/AI can continue to tick. Calls tick() if true or stop() if false.
     */
    @Override
    public boolean canContinueToUse() {
        return this.owner.isAlive() && this.target.isAlive() && this.isWandInHand();
    }

    /**
     * Gets called every server tick as long as canContinueToUse() return true but is guaranteed to get called at least
     * once after the initial start()
     */
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
                    LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
                    bolt.setPos(lightningTarget.getX(), lightningTarget.getY(), lightningTarget.getZ());
                    bolt.setDamage((float) this.owner.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    this.world.addFreshEntity(bolt);
                    this.cooldown = maxCooldown;
                    lightningTarget = null;
                    this.owner.setCasting(false);
                }
                if(this.cooldown == 25) this.owner.setCasting(true);
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
        return this.owner.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Registration.THUNDERSTAFF.get();
    }
}
