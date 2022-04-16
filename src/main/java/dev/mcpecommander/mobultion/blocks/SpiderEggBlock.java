package dev.mcpecommander.mobultion.blocks;

import dev.mcpecommander.mobultion.blocks.tile.SpiderEggTile;
import dev.mcpecommander.mobultion.entities.spiders.entities.MobultionSpiderEntity;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.blocks */
public class SpiderEggBlock extends Block implements EntityBlock {

    public SpiderEggBlock() {
        super(Properties.of(Material.EGG, MaterialColor.COLOR_GRAY).strength(0.5F).sound(SoundType.METAL).noOcclusion());
    }

    /**
     * Gets called when an entity steps on this block or walks on it normally without jumping.
     * @param world The world instance.
     * @param pos The position of the block being stepped on.
     * @param state The block state of the block being stepped on.
     * @param entity The entity that stepped on this block.
     */
    @Override
    public void stepOn(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nonnull Entity entity) {
        if (entity instanceof Monster && !(entity instanceof MobultionSpiderEntity)){
            world.destroyBlock(pos, false);
        }
    }

    /**
     * Gets called when an entity falls on this block from a certain height. Used for example in farmland to check for
     * trampling effect.
     * @param world The world instance.
     * @param state The state of the block being fallen onto.
     * @param pos The position of the block being fallen onto.
     * @param entity The entity that fell onto this block.
     * @param distance How far the entity fell before hitting this block.
     */
    @Override
    public void fallOn(@Nonnull Level world, @NotNull BlockState state, @Nonnull BlockPos pos, @Nonnull Entity entity, float distance) {
        if (entity instanceof LivingEntity && !(entity instanceof MobultionSpiderEntity)){
            world.destroyBlock(pos, false);
        }
        super.fallOn(world, state, pos, entity, distance);
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
    public VoxelShape getShape(@Nonnull BlockState blockState, @Nonnull BlockGetter world, @Nonnull BlockPos pos,
                               @Nonnull CollisionContext selectionContext) {
        return Block.box(5, 0, 5, 11, 7, 11);
    }

    /**
     * A factory of sorts to create the tile entity when the block is placed.
     * @param state The state of the block that the returned tile entity will get attached to.
     * @param pos The position of the block.
     * @return An instance of the tile entity to be attached to this block.
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SpiderEggTile(pos, state);
    }

    /**
     * Used to tell the game what type of rendering system to use. Default is baked models while entity block animated
     * tells the game that it is being rendered in code instead of json.
     * @param state The block state of this block.
     * @return The BlockRenderType to use when rendering this block.
     */
    @Nonnull
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }


}
