package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import dev.mcpecommander.mobultion.entities.spiders.entities.MiniSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.entities.MotherSpiderEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;

import java.util.EnumSet;

/* McpeCommander created on 23/04/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entityGoals */
public class FollowMotherGoal extends Goal {

    private final MiniSpiderEntity miniSpider;
    private MotherSpiderEntity owner;
    private final double speedModifier;
    private int timeToRecalcPath;

    public FollowMotherGoal(MiniSpiderEntity miniSpider, double speedMultiplier) {
        this.miniSpider = miniSpider;
        this.speedModifier = speedMultiplier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        if (this.miniSpider.getOwnerID() == null) {
            return false;
        } else {
            Entity entity = ((ServerLevel)this.miniSpider.level).getEntity(this.miniSpider.getOwnerID());

            if (entity == null) {
                System.out.println("Why did this happen, did we forget to update the owner somewhere");
                return false;
            } else if (this.miniSpider.distanceToSqr(entity) < 25D) {
                return false;
            } else {
                this.owner = (MotherSpiderEntity) entity;
                return true;
            }
        }
    }

    public boolean canContinueToUse() {
        if (this.owner == null) {
            return false;
        } else if (!this.owner.isAlive()) {
            return false;
        } else {
            double sqrDistance = this.miniSpider.distanceToSqr(this.owner);
            return !(sqrDistance < 25.0D) && !(sqrDistance > 256.0D);
        }
    }

    public void start() {
        this.timeToRecalcPath = 0;
    }

    public void stop() {
        this.owner = null;
    }

    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.miniSpider.getNavigation().moveTo(this.owner, this.speedModifier);
        }
    }
}
