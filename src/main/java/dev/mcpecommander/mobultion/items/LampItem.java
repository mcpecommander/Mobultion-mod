package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.entities.zombies.entities.MobultionZombieEntity;
import dev.mcpecommander.mobultion.items.renderers.LampRenderer;
import dev.mcpecommander.mobultion.setup.ModSetup;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/* McpeCommander created on 03/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class LampItem extends Item implements IAnimatable {

    private static final String SOUL_TAG = "mobultion:soul";
    private final AnimationFactory factory = new AnimationFactory(this);

    public LampItem() {
        super(new Properties().durability(3).tab(ModSetup.ITEM_GROUP).fireResistant());
    }

    @Override
    public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new LampRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext useContext) {
        ItemStack stack = useContext.getItemInHand();
        Level world = useContext.getLevel();
        if(useContext.getClickedFace() == Direction.UP && stack.getOrCreateTag().getBoolean(SOUL_TAG) && useContext.getPlayer() != null){
            BlockPos pos = world.getBlockState(useContext.getClickedPos()).getMaterial().blocksMotion() ?
                    useContext.getClickedPos().above() : useContext.getClickedPos();
            if(!useContext.getLevel().isClientSide){
                PathfinderMob entity = getRandomCreature(useContext.getLevel());
                entity.setPos(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
                stack.getOrCreateTag().putBoolean(SOUL_TAG, false);
                world.addFreshEntity(entity);
                stack.hurtAndBreak(1, useContext.getPlayer(),
                        (livingEntity) -> livingEntity.broadcastBreakEvent(useContext.getPlayer().getUsedItemHand()));
            }else{
                for(int i = 0; i < 8; i++) {
                    useContext.getLevel().addParticle(ParticleTypes.HAPPY_VILLAGER,
                            pos.getX() + 0.5f + world.random.nextGaussian() * 0.5f - 0.25f,
                            pos.getY() + 0.2f,
                            pos.getZ() + 0.5f + world.random.nextGaussian() * 0.5f - 0.25f,
                            world.random.nextGaussian() * 0.01f - 0.005f,
                            0.1f,
                            world.random.nextGaussian() * 0.01f - 0.005f);
                }
            }

            return InteractionResult.sidedSuccess(useContext.getLevel().isClientSide);
        }
        return InteractionResult.PASS;
    }

    //TODO: meh, not the best usage but good enough for a beta.
    private static PathfinderMob getRandomCreature(Level world){
        float rand = world.random.nextFloat();
        if(rand < 0.1f){
            return new Horse(EntityType.HORSE, world);
        }else if(rand < 0.2f){
            return new Chicken(EntityType.CHICKEN, world);
        }else if(rand < 0.3f){
            return new Fox(EntityType.FOX, world);
        }else if(rand < 0.4f){
            return new MushroomCow(EntityType.MOOSHROOM, world);
        }else if(rand < 0.5f){
            return new Panda(EntityType.PANDA, world);
        }else if(rand < 0.6f){
            return new Pig(EntityType.PIG, world);
        }else if(rand < 0.7f){
            return new Rabbit(EntityType.RABBIT, world);
        }else if(rand < 0.8f){
            return new Ocelot(EntityType.OCELOT, world);
        }else if(rand < 0.9f){
            return new Sheep(EntityType.SHEEP, world);
        }else{
            return new Wolf(EntityType.WOLF, world);
        }
    }

    @Nonnull
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, @Nonnull Player player,
                                                 @Nonnull LivingEntity interactedEntity, @Nonnull InteractionHand hand) {
        if(!stack.getOrCreateTag().getBoolean(SOUL_TAG)){
            if(interactedEntity instanceof MobultionZombieEntity &&
                    interactedEntity.getHealth() <= interactedEntity.getMaxHealth() * 0.5f){
                interactedEntity.setHealth(0);
                stack.getOrCreateTag().putBoolean(SOUL_TAG, true);
                return InteractionResult.sidedSuccess(player.level.isClientSide);
            }else{
                return InteractionResult.FAIL;
            }
        }else{
            return InteractionResult.PASS;
        }
    }

    //TODO: maybe test a workaround for the creative usage.

//    @Override
//    public boolean showDurabilityBar(ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public int getDamage(ItemStack stack) {
//        return super.getDamage(stack);
//    }

    @Override
    public void inventoryTick(@Nonnull ItemStack itemStack, @Nonnull Level world, @Nonnull Entity holder, int slot, boolean selected) {
        if(holder instanceof Player player){
            int i = 0;
            for(ItemStack item : player.getInventory().items){
                if(item.getItem() == Registration.LAMP.get()) i++;
            }
            if(i > 1){
                player.drop(itemStack, false);
                player.getInventory().setItem(slot, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(Level world, Entity oldItemEntity, ItemStack itemstack) {
        ItemEntity entity = new ItemEntity(world, oldItemEntity.getX(), oldItemEntity.getY(), oldItemEntity.getZ(), itemstack){
            @Override
            public void playerTouch(@Nonnull Player player) {
                if(player.getInventory().contains(new ItemStack(Registration.LAMP.get()))) return;
                super.playerTouch(player);
            }
        };
        entity.setDeltaMovement(oldItemEntity.getDeltaMovement());
        entity.setPickUpDelay(40);
        return entity;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
