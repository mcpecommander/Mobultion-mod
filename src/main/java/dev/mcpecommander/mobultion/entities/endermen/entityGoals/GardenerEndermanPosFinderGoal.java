package dev.mcpecommander.mobultion.entities.endermen.entityGoals;

import dev.mcpecommander.mobultion.entities.endermen.entities.GardenerEndermanEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

/* McpeCommander created on 11/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entityGoals */
public class GardenerEndermanPosFinderGoal extends Goal {

    GardenerEndermanEntity owner;
    int probability, searchDistance;
    //TODO: remake the position finder to prioritize some tasks over others.
    public GardenerEndermanPosFinderGoal(GardenerEndermanEntity owner, int probability, int distance){
        this.owner = owner;
        this.setFlags(EnumSet.of(Flag.TARGET));
        this.probability = probability;
        this.searchDistance = distance;
    }

    @Override
    public boolean canUse() {
        return !owner.isGardening() && owner.getTargetPos() == null && owner.getRandom().nextInt(probability) == 0;
    }

    @Override
    public void start() {

        for(int i = 0; i < 10; i++){
            Vector3d vector = RandomPositionGenerator.getLandPos(owner, searchDistance, 5);
            if (vector != null){
                BlockPos pos = new BlockPos(vector);
                if(GardenerEndermanEntity.checkPos(owner.level, pos.below()) != GardenerEndermanEntity.GardeningState.NONE){
                    this.owner.setTargetPos(pos.below());
                    return;
                }
            }
        }

    }
}
