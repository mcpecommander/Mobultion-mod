package dev.mcpecommander.mobultion.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class TestBlock extends Block {

    protected static final VoxelShape C_BASE = Block.box(0D, 0.0D, 0D, 16D, 2.0D, 16D);
    protected static final VoxelShape C_TOP = Block.box(4D, 2.0D, 4D, 12D, 8.0D, 12D);

    public TestBlock() {
        super(Properties.of(Material.LEAVES).sound(SoundType.GRASS).strength(2.0f).noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return VoxelShapes.or(C_BASE, C_TOP);
    }

}
