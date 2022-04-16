package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import javax.annotation.Nonnull;
import java.util.Random;

/* McpeCommander created on 24/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class CorruptedBoneMealItem extends Item {

    public CorruptedBoneMealItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP));
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext useContext) {
        Level world = useContext.getLevel();
        BlockPos clickedBlock = useContext.getClickedPos();
        if (applyBonemeal(useContext.getItemInHand(), world, clickedBlock)) {
            if (world.isClientSide) {
                if(world.getBlockState(clickedBlock).isCollisionShapeFullBlock(world, clickedBlock)) clickedBlock = clickedBlock.above();
                for(int i = 0; i < 20; i++){
                    world.addParticle(ParticleTypes.CRIT, clickedBlock.getX() + 0.5 + (world.random.nextGaussian() - 0.5) * 0.2,
                            clickedBlock.getY(), clickedBlock.getZ() + 0.5 + (world.random.nextGaussian() - 0.5) * 0.2,
                            (world.random.nextGaussian() - 0.5)  * 0.2, world.random.nextGaussian() * 0.5,
                            (world.random.nextGaussian() - 0.5) * 0.2);
                }
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private boolean applyBonemeal(ItemStack itemStack, Level world, BlockPos applicablePos) {
        BlockState blockstate = world.getBlockState(applicablePos);
        if(performCorruption(world, applicablePos, blockstate.getBlock())){
            itemStack.shrink(1);
            return true;
        }
        return false;
    }

    private static boolean performCorruption(Level world, BlockPos applicablePos, Block block){
        if(block instanceof CropBlock){
            world.setBlock(applicablePos, ((CropBlock) block).getStateForAge(0), Block.UPDATE_ALL_IMMEDIATE);
            return true;
        }else if(block instanceof GrassBlock) {
            if (!world.isEmptyBlock(applicablePos.above())) {
                if(!performCorruption(world, applicablePos.above(), world.getBlockState(applicablePos.above()).getBlock())){
                    world.setBlock(applicablePos, Blocks.DIRT.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                }
            } else {
                world.setBlock(applicablePos, Blocks.DIRT.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            }
            return true;
        }else if(block instanceof TallGrassBlock) {
            if(!world.isClientSide) world.destroyBlock(applicablePos, false);
            return true;
        }else if(block instanceof DoublePlantBlock) {
            if (world.getBlockState(applicablePos).getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                if(!world.isClientSide) world.destroyBlock(applicablePos.below(), false);
            } else {
                if(!world.isClientSide) world.destroyBlock(applicablePos, false);
            }
            return true;
        }else if(block instanceof FlowerBlock){
            if(!world.isClientSide) world.destroyBlock(applicablePos, false);
            return true;
        }else if(block instanceof BambooBlock){
            if(world.getBlockState(applicablePos.below()).is(BlockTags.DIRT)){
                world.setBlock(applicablePos, Blocks.BAMBOO_SAPLING.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                return true;
            }
            return false;
        }else if(block instanceof NyliumBlock){
            if(world.isEmptyBlock(applicablePos.above())){
                world.setBlock(applicablePos, Blocks.NETHERRACK.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            }else{
                place(world, world.random, applicablePos.above(), Blocks.AIR.defaultBlockState());
            }
            return true;
        }else if(block instanceof SweetBerryBushBlock){
            world.setBlock(applicablePos, Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(SweetBerryBushBlock.AGE, 0),
                    Block.UPDATE_ALL_IMMEDIATE);
            return true;
        }else if(block instanceof TwistingVinesPlantBlock || block instanceof WeepingVinesPlantBlock ||
                block instanceof WeepingVinesBlock || block instanceof TwistingVinesBlock){
            BlockPos pos = applicablePos;
            while(world.getBlockState(pos.above()).is(Blocks.TWISTING_VINES_PLANT)
                    || world.getBlockState(pos.above()).is(Blocks.WEEPING_VINES_PLANT)){
                pos = pos.above();
            }
            if(!world.isClientSide) world.destroyBlock(pos, false);
            return true;
        }else if(isNetherVegetation(world.getBlockState(applicablePos))){
            if(!world.isClientSide) world.destroyBlock(applicablePos, false);
            return true;
        }

        return false;
    }

    private static boolean isNetherVegetation(BlockState state){
        return state.is(Blocks.CRIMSON_ROOTS) || state.is(Blocks.CRIMSON_FUNGUS) ||
                state.is(Blocks.WARPED_FUNGUS) || state.is(Blocks.WARPED_ROOTS) || state.is(Blocks.NETHER_SPROUTS);
    }

    private static void place(LevelAccessor world, Random random, BlockPos pos, BlockState state) {
        if (world.getBlockState(pos.below()).is(BlockTags.NYLIUM)) {
            int i = pos.getY();
            if (i >= 1 && i + 1 < 256) {

                for(int k = 0; k < 3 * 3; ++k) {
                    BlockPos blockpos = pos.offset(random.nextInt(3) - random.nextInt(3),
                            0, random.nextInt(3) - random.nextInt(3));
                    if (isNetherVegetation(world.getBlockState(blockpos)) && blockpos.getY() > 0) {
                        world.setBlock(blockpos, state, 2);
                    }
                }
            }
        }
    }
}
