package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

/* McpeCommander created on 24/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class CorruptedBoneMealItem extends Item {

    public CorruptedBoneMealItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP));
    }

    @Override
    public ActionResultType useOn(ItemUseContext useContext) {
        World world = useContext.getLevel();
        BlockPos clickedBlock = useContext.getClickedPos();
        if (applyBonemeal(useContext.getItemInHand(), world, clickedBlock, useContext.getPlayer())) {
            if (world.isClientSide) {
                if(world.getBlockState(clickedBlock).isCollisionShapeFullBlock(world, clickedBlock)) clickedBlock = clickedBlock.above();
                for(int i = 0; i < 20; i++){
                    world.addParticle(ParticleTypes.CRIT, clickedBlock.getX() + 0.5 + (random.nextGaussian() - 0.5) * 0.2,
                            clickedBlock.getY(), clickedBlock.getZ() + 0.5 + (random.nextGaussian() - 0.5) * 0.2,
                            (random.nextGaussian() - 0.5)  * 0.2, random.nextGaussian() * 0.5,
                            (random.nextGaussian() - 0.5) * 0.2);
                }
            }
            return ActionResultType.sidedSuccess(world.isClientSide);
        }
        return ActionResultType.PASS;
    }

    private boolean applyBonemeal(ItemStack itemStack, World world, BlockPos applicablePos, PlayerEntity player) {
        BlockState blockstate = world.getBlockState(applicablePos);
        if(performCorruption(world, applicablePos, blockstate.getBlock())){
            SoundType type = blockstate.getSoundType(world, applicablePos, player);
            world.playSound(player, applicablePos, type.getBreakSound(), SoundCategory.BLOCKS, type.volume, type.pitch);
            itemStack.shrink(1);
            return true;
        }
        return false;
    }

    private static boolean performCorruption(World world, BlockPos applicablePos, Block block){
        if(block instanceof CropsBlock){
            world.setBlock(applicablePos, ((CropsBlock) block).getStateForAge(0), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            return true;
        }else if(block instanceof GrassBlock) {
            if (!world.isEmptyBlock(applicablePos.above())) {
                if(!performCorruption(world, applicablePos.above(), world.getBlockState(applicablePos.above()).getBlock())){
                    world.setBlock(applicablePos, Blocks.DIRT.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                }
            } else {
                world.setBlock(applicablePos, Blocks.DIRT.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }
            return true;
        }else if(block instanceof TallGrassBlock) {
            world.setBlock(applicablePos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            return true;
        }else if(block instanceof DoublePlantBlock) {
            if (world.getBlockState(applicablePos).getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                world.setBlock(applicablePos.below(), Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            } else {
                world.setBlock(applicablePos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }
            return true;
        }else if(block instanceof FlowerBlock){
            world.setBlock(applicablePos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            return true;
        }else if(block instanceof BambooBlock){
            if(world.getBlockState(applicablePos.below()).is(Tags.Blocks.DIRT)){
                world.setBlock(applicablePos, Blocks.BAMBOO_SAPLING.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                return true;
            }
            return false;
        }else if(block instanceof NyliumBlock){
            if(world.isEmptyBlock(applicablePos.above())){
                world.setBlock(applicablePos, Blocks.NETHERRACK.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }else{
                place(world, world.random, applicablePos.above(), Blocks.AIR.defaultBlockState());
            }
            return true;
        }else if(block instanceof SweetBerryBushBlock){
            world.setBlock(applicablePos, Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(SweetBerryBushBlock.AGE, 0),
                    Constants.BlockFlags.DEFAULT_AND_RERENDER);
            return true;
        }else if(block instanceof TwistingVinesBlock || block instanceof WeepingVinesBlock ||
                block instanceof WeepingVinesTopBlock || block instanceof TwistingVinesTopBlock){
            BlockPos pos = applicablePos;
            while(world.getBlockState(pos.above()).is(Blocks.TWISTING_VINES_PLANT)
                    || world.getBlockState(pos.above()).is(Blocks.WEEPING_VINES_PLANT)){
                pos = pos.above();
            }
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            return true;
        }else if(isNetherVegetation(world.getBlockState(applicablePos))){
            world.setBlock(applicablePos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            return true;
        }

        return false;
    }

    private static boolean isNetherVegetation(BlockState state){
        return state.is(Blocks.CRIMSON_ROOTS) || state.is(Blocks.CRIMSON_FUNGUS) ||
                state.is(Blocks.WARPED_FUNGUS) || state.is(Blocks.WARPED_ROOTS) || state.is(Blocks.NETHER_SPROUTS);
    }

    private static void place(IWorld world, Random random, BlockPos pos, BlockState state) {
        Block block = world.getBlockState(pos.below()).getBlock();
        if (block.is(BlockTags.NYLIUM)) {
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
