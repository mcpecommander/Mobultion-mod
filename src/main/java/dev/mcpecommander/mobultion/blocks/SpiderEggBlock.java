package dev.mcpecommander.mobultion.blocks;

import dev.mcpecommander.mobultion.blocks.tile.SpiderEggTile;
import dev.mcpecommander.mobultion.entities.spiders.entities.MobultionSpiderEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.blocks */
public class SpiderEggBlock extends Block {

    public SpiderEggBlock() {
        super(Properties.of(Material.EGG, MaterialColor.COLOR_GRAY).strength(0.5F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public void stepOn(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (entity instanceof MonsterEntity && !(entity instanceof MobultionSpiderEntity)){
            world.destroyBlock(pos, false);
        }
    }

    @Override
    public void fallOn(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Entity entity, float distance) {
        if (entity instanceof LivingEntity && !(entity instanceof MobultionSpiderEntity)){
            world.destroyBlock(pos, false);
        }
        super.fallOn(world, pos, entity, distance);
    }

    //TODO: Make it that eggs can be randomly placed within a block.
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos,
                               @Nonnull ISelectionContext selectionContext) {
        return Block.box(5, 0, 5, 11, 7, 11);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SpiderEggTile();
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderShape(@Nonnull BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }


}
