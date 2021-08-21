package dev.mcpecommander.mobultion.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class HayHatBlock extends Block {

    protected static final VoxelShape C_BASE = Block.box(0D, 0.0D, 0D, 16D, 2.0D, 16D);
    protected static final VoxelShape C_TOP = Block.box(4D, 2.0D, 4D, 12D, 8.0D, 12D);

    public HayHatBlock() {
        super(Properties.of(Material.LEAVES).sound(SoundType.GRASS).strength(2.0f).noOcclusion());
    }

    /**
     * Defines the hitbox of the block that gets shown when highlighting a block or for collision purposes. There are
     * multiple getShapes methods but this one in combination with setting setRenderLayer() to translucent fixed the xray
     * issue. Check {@link dev.mcpecommander.mobultion.setup.ClientSetup}
     * @param blockState The state of the current block being passed into this method.
     * @param world The world instance.
     * @param pos The position of this block.
     * @param selectionContext The selection context that has some information about the player.
     * @return The voxel shape of the hitbox.
     */
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState blockState, @Nonnull IBlockReader world, @Nonnull BlockPos pos,
                               @Nonnull ISelectionContext selectionContext) {
        return VoxelShapes.or(C_BASE, C_TOP);
    }

}
