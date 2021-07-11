package dev.mcpecommander.mobultion.entities.endermen.entityGoals;

import dev.mcpecommander.mobultion.entities.endermen.entities.GardenerEndermanEntity;
import net.minecraft.block.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import java.util.EnumSet;

/* McpeCommander created on 11/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entityGoals */
public class GardenerEndermanGardenGoal extends Goal {

    GardenerEndermanEntity owner;
    World level;
    BlockPos pos;
    Path path;
    GardeningState state;
    int maxDelay, delay;

    public GardenerEndermanGardenGoal(GardenerEndermanEntity owner, int delay){
        this.owner = owner;
        this.level = owner.level;
        this.maxDelay = delay;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(owner.getTargetPos() != null && GardenerEndermanEntity.checkPos(level, owner.getTargetPos()) != GardeningState.NONE){
            path = owner.getNavigation().createPath(owner.getTargetPos(), 1);
            return path != null;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return owner.getTargetPos() != null && GardenerEndermanEntity.checkPos(level, owner.getTargetPos()) != GardeningState.NONE;
    }

    @Override
    public void start() {
        this.delay = 0;
        this.pos = owner.getTargetPos();
        this.state = GardenerEndermanEntity.checkPos(level, pos);
        owner.getNavigation().moveTo(path, 1);
    }

    @Override
    public void stop() {
        this.state = GardeningState.NONE;
    }

    @Override
    public void tick() {
        this.owner.getLookControl().setLookAt(pos.getX(), pos.getY(), pos.getZ());
        if(path.isDone()){
            this.owner.setGardening(true);
            if(delay++ > maxDelay){
                BlockState blockState = level.getBlockState(pos);
                BlockState above = level.getBlockState(pos.above());
                switch(state){
                    case BONEMEAL:
                        if(above.getBlock() instanceof IGrowable){
                            ((IGrowable) above.getBlock()).performBonemeal((ServerWorld) level, owner.getRandom(), pos, above);
                        }
                        break;
                    case WATERING:
                        if(blockState.is(Blocks.FARMLAND)){
                            level.setBlock(pos, blockState.setValue(FarmlandBlock.MOISTURE, 7), Constants.BlockFlags.BLOCK_UPDATE);
                        }
                        break;
                    case PLANTING:
                        if(above.isAir(level, pos.above())){
                            level.setBlock(pos.above(), Blocks.LILAC.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                        }
                        break;
                    case PICKING:
                        if(above.getBlock() instanceof FlowerBlock){
                            level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                        }
                        break;
                    case NONE:
                        break;
                }
                this.owner.setTargetPos(null);
            }
        }else if(!path.canReach()){
            teleportNearby();
        }
    }

    private boolean teleportNearby(){
        return true;
    }



    public enum GardeningState{
        BONEMEAL, WATERING, PLANTING, PICKING, NONE
    }
}
