package dev.mcpecommander.mobultion.blocks;

import dev.mcpecommander.mobultion.blocks.tile.SpiderEggTile;
import dev.mcpecommander.mobultion.entities.spiders.entities.MiniSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.entities.MobultionSpiderEntity;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.blocks */
public class SpiderEggBlock extends Block implements EntityBlock {

    public static final VoxelShape SHAPE_1 = Block.box(4, 0, 4, 12, 7, 12);
    public static final VoxelShape SHAPE_2 = Block.box(10, 0, 0, 16, 7, 6);
    public static final VoxelShape SHAPE_3 = Block.box(2, 0, 0, 8, 7, 6);
    public static final VoxelShape SHAPE_4 = Block.box(2, 0, 10, 8, 7, 16);
    public static final VoxelShape SHAPE_5 = Block.box(10, 0, 9, 16, 7, 15);

    /**
     * Amount of eggs per block
     */
    public static final IntegerProperty EGGS = IntegerProperty.create("eggs", 1, 5);

    public SpiderEggBlock() {
        super(Properties.of(Material.EGG, MaterialColor.COLOR_GRAY).strength(0.5F).sound(SoundType.METAL).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(EGGS, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(EGGS);
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState currentState,
                              @Nullable BlockEntity blockEntity, @NotNull ItemStack itemUsedToDestroy) {
        super.playerDestroy(level, player, pos, currentState, blockEntity, itemUsedToDestroy);
        if (!level.isClientSide){
            if(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemUsedToDestroy) == 0){
                for (int i = 0; i < currentState.getValue(EGGS); i++){
                    MiniSpiderEntity spider = new MiniSpiderEntity(Registration.MINISPIDER.get(), level);
                    spider.setPos(Vec3.atCenterOf(pos).add(level.random.nextGaussian() * 0.5 - 0.5, 0,
                            level.random.nextGaussian() * 0.5 - 0.5));
                    level.addFreshEntity(spider);
                    if (blockEntity instanceof SpiderEggTile tile){
                        spider.setOwnerID(tile.getOwnerID());
                    }
                }
            }else{
                dropResources(currentState, level, pos);
            }
        }
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockstate = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());
        return blockstate.is(this) ? blockstate.setValue(EGGS, Math.min(5, blockstate.getValue(EGGS) + 1)) :
                super.getStateForPlacement(blockPlaceContext);
    }

    public boolean canBeReplaced(@NotNull BlockState currentState, BlockPlaceContext placementContext) {
        return !placementContext.isSecondaryUseActive() && placementContext.getItemInHand().getItem() == this.asItem()
                && currentState.getValue(EGGS) < 5 || super.canBeReplaced(currentState, placementContext);
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
        return switch (blockState.getValue(EGGS)) {
            case 2 -> Shapes.or(SHAPE_1, SHAPE_2);
            case 3 -> Shapes.or(SHAPE_1, SHAPE_2, SHAPE_3);
            case 4 -> Shapes.or(SHAPE_1, SHAPE_2, SHAPE_3, SHAPE_4);
            case 5 -> Shapes.or(SHAPE_1, SHAPE_2, SHAPE_3, SHAPE_4, SHAPE_5);
            default -> SHAPE_1;
        };
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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state,
                                                                  @NotNull BlockEntityType<T> type) {
        return (lvl, pos, blockState, t) -> {
            if (t instanceof SpiderEggTile tile) {
                tile.tick();
            }
        };
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
