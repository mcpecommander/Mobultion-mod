package dev.mcpecommander.mobultion.entities.spiders.entityGoals;

import dev.mcpecommander.mobultion.entities.spiders.entities.RedEyeEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/* McpeCommander created on 29/05/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entityGoals */
public class RedEyeHoverAroundOwnerGoal extends Goal {

    private final RedEyeEntity follower;

    public RedEyeHoverAroundOwnerGoal(RedEyeEntity follower) {
        this.follower = follower;
        //Don't set flags to allow the random look goal to be run in parallel.
        //this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(this.follower.isLaunching()) return false;
        if(this.follower.getOwner() == null || !this.follower.getOwner().isAlive()) return false;
        return this.follower.getTarget() == null || !this.follower.getTarget().isAlive();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        Vec3 pos = this.follower.getOwner().getHead(this.follower.head).add(0, 2, 0);
        Vec3 deltaMovement = pos.subtract(this.follower.position());
        double distance = deltaMovement.length();
        this.follower.setDeltaMovement(deltaMovement.scale(0.1D /distance)
                .add(0, Mth.sin(this.follower.tickCount % 30f / 30f * Mth.TWO_PI) * 0.05d, 0));
        if(distance > 1) {
            this.follower.getLookControl().setLookAt(this.follower.position().add(deltaMovement));
        }
    }
}
