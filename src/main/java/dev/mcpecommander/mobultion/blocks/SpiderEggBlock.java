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

    /**
     * Gets called when an entity steps on this block or walks on it normally without jumping.
     * @param world The world instance.
     * @param pos The position of the block being stepped on.
     * @param entity The entity that stepped on this block.
     */
    @Override
    public void stepOn(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (entity instanceof MonsterEntity && !(entity instanceof MobultionSpiderEntity)){
            world.destroyBlock(pos, false);
        }
    }

    /**
     * Gets called when an entity falls on this block from a certain height. Used for example in farmland to check for
     * trampling effect.
     * @param world The world instance.
     * @param pos The position of the block being fallen onto.
     * @param entity The entity that fell onto this block.
     * @param distance How far the entity fell before hitting this block.
     */
    @Override
    public void fallOn(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Entity entity, float distance) {
        if (entity instanceof LivingEntity && !(entity instanceof MobultionSpiderEntity)){
            world.destroyBlock(pos, false);
        }
        super.fallOn(world, pos, entity, distance);
    }

    /**
     * Defines the hitbox of the block that gets shown when highlighting a block or for collision purposes. There are
     * multiple getShapes methods but this one is general purpose as I saw.
     * @param blockState The state of the current block being passed into this method.
     * @param world The world instance.
     * @param pos The position of this block.
     * @param selectionContext The selection context that has some information about the player.
     * @return The voxel shape of the hitbox.
     */
    //TODO: Make it that eggs can be randomly placed within a block.
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState blockState, @Nonnull IBlockReader world, @Nonnull BlockPos pos,
                               @Nonnull ISelectionContext selectionContext) {
        return Block.box(5, 0, 5, 11, 7, 11);
    }

    /**
     * Necessary for the game to know that it should spawn the corresponding tile entity when this block is placed.
     * @param state The state of the block where theoretically some states wouldn't have a tile entity while others have.
     * @return true if the block has a tile entity that is supposed to be attached to it.
     */
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    /**
     * A factory of sorts to create the tile entity when the block is placed.
     * @param state The state of the block that the returned tile entity will get attached to.
     * @param world The world instance.
     * @return An instance of the tile entity to be attached to this block.
     */
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SpiderEggTile();
    }

    /**
     * Used to tell the game what type of rendering system to use. Default is baked models while entity block animated
     * tells the game that it is being rendered in code instead of json.
     * @param state The block state of this block.
     * @return The BlockRenderType to use when rendering this block.
     */
    @Nonnull
    @Override
    public BlockRenderType getRenderShape(@Nonnull BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }


}
