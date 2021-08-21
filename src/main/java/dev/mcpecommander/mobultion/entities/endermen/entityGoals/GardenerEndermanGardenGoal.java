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

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

/* McpeCommander created on 11/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entityGoals */
public class GardenerEndermanGardenGoal extends Goal {

    GardenerEndermanEntity owner;
    World level;
    BlockPos pos, initialTeleportPos;

    GardenerEndermanEntity.GardeningState state;
    int maxDelay, delay;

    int forgivenessTicks, resetTicks;
    double smallestDistance, currentDistance;

    Random random;
    int positionsReset;

    //TODO: make better
    public GardenerEndermanGardenGoal(GardenerEndermanEntity owner, int delay){
        this.owner = owner;
        this.level = owner.level;
        this.random = owner.getRandom();
        this.maxDelay = delay;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * If the goal/AI can start when the conditions are met.
     * @return true if the goal can start and then start() is called.
     */
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

    /**
     * Gets called after every tick to make sure the goal/AI can continue.
     * @return true if the goal/AI can continue to tick. Calls tick() if true or stop() if false.
     */
    @Override
    public boolean canContinueToUse() {
        return this.owner.getNavigation().getPath() != null && owner.getTargetPos() != null &&
                GardenerEndermanEntity.checkPos(level, owner.getTargetPos()) != GardenerEndermanEntity.GardeningState.NONE;
    }

    /**
     * Gets called once after the canUse() is true.
     * Used to set some variables or timers for the goal/AI.
     */
    @Override
    public void start() {
        //Teleports the enderman to a closer location to the target if the target is far enough.
        this.owner.teleport(initialTeleportPos.getX(), initialTeleportPos.getY(), initialTeleportPos.getZ());
        this.owner.getNavigation().moveTo(getPathToNearbyBlock(owner.getTargetPos()), 1);
        //Debug only
        this.owner.setDebugRoad(MobultionEndermanEntity.getPathNodes(owner));
        this.delay = 0;
        this.pos = owner.getTargetPos();
        this.state = GardenerEndermanEntity.checkPos(level, pos);
        this.smallestDistance = Double.MAX_VALUE;
        this.resetTicks = 0;
    }

    /**
     * Gets called when the canContinueToUse() returns false and allows for final adjustments of the variables before
     * the goal is finished.
     */
    @Override
    public void stop() {
        this.state = GardenerEndermanEntity.GardeningState.NONE;
        this.owner.setGardening(false);
        this.owner.setTargetPos(null);
        this.smallestDistance = Double.MAX_VALUE;
        this.currentDistance = 0;
        this.forgivenessTicks = 0;
    }

    /**
     * Gets called every server tick as long as canContinueToUse() return true but is guaranteed to get called at least
     * once after the initial start()
     */
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

    //TODO: Fix this shitty check or delete all together.

    //Shittier version of the stuck check in the path navigator because somehow mine works better for this usecase.
    private void checkStuck(){
        if(currentDistance >= smallestDistance){
            forgivenessTicks++;
        }else{
            resetTicks++;
            if(resetTicks > 50){
                resetTicks = 0;
                forgivenessTicks = 0;
            }
        }
        if(forgivenessTicks > 50){
            this.owner.getNavigation().stop();
        }
    }

    /**
     * Used to get a path to a block near the target because the enderman is going to kneel towards the target so it makes
     * more sense to make it kneel beside it and not on it.
     * @param pos The target block position
     * @return A path to the block beside the target
     */
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

    /**
     * Random check for a position close to the given position that this entity can teleport to.
     * @param pos The block position to test for nearby position.
     * @return A block position that this enderman can teleport to or null if there is none possible.
     */
    @Nullable
    private BlockPos getCanTeleportToNearby(BlockPos pos){
        for(int i = 0; i < 10; i++){
            BlockPos newPos = pos.offset((random.nextBoolean() ? -1 : 1) * (random.nextInt(7) + 3), 0,
                    (random.nextBoolean() ? -1 : 1) * (random.nextInt(7) + 3));
            if(this.owner.canTeleport(newPos.getX(), newPos.getY(), newPos.getZ())){
                return newPos;
            }
        }
        return null;
    }

}
