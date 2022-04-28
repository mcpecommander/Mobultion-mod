package dev.mcpecommander.mobultion.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public @NotNull VoxelShape getShape(@Nonnull BlockState blockState, @Nonnull BlockGetter world, @Nonnull BlockPos pos,
                                        @Nonnull CollisionContext selectionContext) {
        return Shapes.or(C_BASE, C_TOP);
    }

}
