package dev.mcpecommander.mobultion.entities.endermen.entityGoals;

import dev.mcpecommander.mobultion.entities.endermen.entities.GardenerEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.entities.MobultionEndermanEntity;
import net.minecraft.block.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import java.util.EnumSet;
import java.util.Random;

/* McpeCommander created on 11/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entityGoals */
public class GardenerEndermanGardenGoal extends Goal {

    GardenerEndermanEntity owner;
    World level;
    BlockPos pos, initialTeleportPos;

    GardeningState state;
    int maxDelay, delay;

    int forgivenessTicks, resetTicks;
    double smallestDistance, currentDistance;

    Random random;
    int positionsReset;

    public GardenerEndermanGardenGoal(GardenerEndermanEntity owner, int delay){
        this.owner = owner;
        this.level = owner.level;
        this.random = owner.getRandom();
        this.maxDelay = delay;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(this.owner.getTargetPos() != null){
            BlockPos pos = getCanTeleportToNearby(this.owner.getTargetPos());
            if(pos != null){
                initialTeleportPos = pos;
                return true;
            }
            positionsReset++;
        }
        if(positionsReset > 50){
            this.owner.setTargetPos(null);
            positionsReset = 0;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.owner.getNavigation().getPath() != null && owner.getTargetPos() != null &&
                GardenerEndermanEntity.checkPos(level, owner.getTargetPos()) != GardeningState.NONE;
    }

    @Override
    public void start() {
        System.out.println(this.owner.teleport(initialTeleportPos));
        this.owner.getNavigation().moveTo(getPathToNearbyBlock(owner.getTargetPos()), 1);
        this.owner.setDebugRoad(MobultionEndermanEntity.getPathNodes(owner));
        this.delay = 0;
        this.pos = owner.getTargetPos();
        this.state = GardenerEndermanEntity.checkPos(level, pos);
        this.smallestDistance = Double.MAX_VALUE;
        resetTicks = 0;
    }

    @Override
    public void stop() {
        this.state = GardeningState.NONE;
        this.owner.setGardening(false);
        this.owner.setTargetPos(null);
        this.smallestDistance = Double.MAX_VALUE;
        this.currentDistance = 0;
        this.forgivenessTicks = 0;
    }

    @Override
    public void tick() {
        this.owner.getLookControl().setLookAt(pos.getX(), pos.getY(), pos.getZ());

        Path path = this.owner.getNavigation().getPath();
        if(path != null) {
            currentDistance = Math.sqrt(this.owner.distanceToSqr(this.owner.getNavigation().getTargetPos().getX(),
                    this.owner.getNavigation().getTargetPos().getY(), this.owner.getNavigation().getTargetPos().getZ()));
            if (path.isDone()) {
                this.owner.setGardening(true);
                if (delay++ > maxDelay) {
                    BlockState blockState = level.getBlockState(pos);
                    BlockState above = level.getBlockState(pos.above());
                    switch (state) {
                        case BONEMEAL:
                            if (above.getBlock() instanceof IGrowable) {
                                ((IGrowable) above.getBlock()).performBonemeal((ServerWorld) level, owner.getRandom(), pos, above);
                            }
                            break;
                        case WATERING:
                            if (blockState.is(Blocks.FARMLAND)) {
                                level.setBlock(pos, blockState.setValue(FarmlandBlock.MOISTURE, 7), Constants.BlockFlags.BLOCK_UPDATE);
                            }
                            break;
                        case PLANTING:
                            if (above.isAir(level, pos.above())) {
                                level.setBlock(pos.above(), Blocks.ALLIUM.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                            }
                            break;
                        case PICKING:
                            if (above.getBlock() instanceof FlowerBlock) {
                                level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                            }
                            break;
                        case NONE:
                            break;
                    }
                    this.owner.getNavigation().stop();

                }
            } else{
                checkStuck();
                smallestDistance = Math.min(currentDistance, smallestDistance);
            }
        }

    }

//    private boolean teleportNearby(){
//        //this.owner.teleportAround()
//        Vector3d teleport = RandomPositionGenerator.getLandPosTowards(this.owner, 10, 7,
//                new Vector3d(pos.getX(), pos.getY(), pos.getZ()));
//        if(teleport != null){
//            return this.owner.teleport(teleport.x, teleport.y, teleport.z);
//        }
//        return true;
//    }

    private void checkStuck(){
        if(currentDistance >= smallestDistance){
            System.out.println("why");
            forgivenessTicks++;
        }else{
            resetTicks++;
            if(resetTicks > 50){
                System.out.println("yes");
                resetTicks = 0;
                forgivenessTicks = 0;
            }
        }
        if(forgivenessTicks > 50){
            System.out.println("fuck");
            this.owner.getNavigation().stop();
        }
    }

    private Path getPathToNearbyBlock(BlockPos pos){
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if(i == 0 && j == 0) continue;
                Path path = owner.getNavigation().createPath(pos.offset(i, 0, j), 1);
                if(path != null) return path;
            }
        }
        return null;
    }

    private BlockPos getCanTeleportToNearby(BlockPos pos){
        for(int i = 0; i < 10; i++){
            BlockPos newPos = pos.offset((random.nextBoolean() ? -1 : 1) * (random.nextInt(7) + 3), 0,
                    (random.nextBoolean() ? -1 : 1) * (random.nextInt(7) + 3));
            if(this.owner.canTeleport(newPos.getX(), newPos.getY(), newPos.getZ())){
                return newPos;
            }
        }
//        for(int i = -8; i < 9; i++){
//            for(int j = -8; j < 9; j++){
//                if(i >= -3 && i <= 3 && j >= -3 && j <= 3) continue;
//                BlockPos newPos = pos.offset(i, 0, j);
//                if(this.owner.canTeleport(newPos.getX(), newPos.getY(), newPos.getZ())){
//                    return newPos;
//                }
//            }
//        }
        return null;
    }

    public enum GardeningState{
        BONEMEAL, WATERING, PLANTING, PICKING, NONE
    }
}
