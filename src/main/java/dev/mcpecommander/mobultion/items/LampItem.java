package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.entities.zombies.entities.MobultionZombieEntity;
import dev.mcpecommander.mobultion.items.renderers.LampRenderer;
import dev.mcpecommander.mobultion.setup.ModSetup;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* McpeCommander created on 03/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class LampItem extends Item implements IAnimatable {

    private static final String SOUL_TAG = "mobultion:soul";
    private final AnimationFactory factory = new AnimationFactory(this);

    public LampItem() {
        super(new Properties().durability(3).tab(ModSetup.ITEM_GROUP).fireResistant().setISTER(() -> LampRenderer::new));
    }

    @Nonnull
    @Override
    public ActionResultType useOn(ItemUseContext useContext) {
        ItemStack stack = useContext.getItemInHand();
        if(useContext.getClickedFace() == Direction.UP && stack.getOrCreateTag().getBoolean(SOUL_TAG) && useContext.getPlayer() != null){
            BlockPos pos = useContext.getLevel().getBlockState(useContext.getClickedPos()).getMaterial().blocksMotion() ?
                    useContext.getClickedPos().above() : useContext.getClickedPos();
            if(!useContext.getLevel().isClientSide){
                CreatureEntity entity = getRandomCreature(useContext.getLevel());
                entity.setPos(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
                stack.getOrCreateTag().putBoolean(SOUL_TAG, false);
                useContext.getLevel().addFreshEntity(entity);
                stack.hurtAndBreak(1, useContext.getPlayer(),
                        (livingEntity) -> livingEntity.broadcastBreakEvent(useContext.getPlayer().getUsedItemHand()));
            }else{
                for(int i = 0; i < 8; i++) {
                    useContext.getLevel().addParticle(ParticleTypes.HAPPY_VILLAGER,
                            pos.getX() + 0.5f + random.nextGaussian() * 0.5f - 0.25f,
                            pos.getY() + 0.2f,
                            pos.getZ() + 0.5f + random.nextGaussian() * 0.5f - 0.25f,
                            random.nextGaussian() * 0.01f - 0.005f,
                            0.1f,
                            random.nextGaussian() * 0.01f - 0.005f);
                }
            }

            return ActionResultType.sidedSuccess(useContext.getLevel().isClientSide);
        }
        return ActionResultType.PASS;
    }

    //TODO: meh, not the best usage but good enough for a beta.
    private static CreatureEntity getRandomCreature(World world){
        float rand = random.nextFloat();
        if(rand < 0.1f){
            return new HorseEntity(EntityType.HORSE, world);
        }else if(rand < 0.2f){
            return new ChickenEntity(EntityType.CHICKEN, world);
        }else if(rand < 0.3f){
            return new FoxEntity(EntityType.FOX, world);
        }else if(rand < 0.4f){
            return new MooshroomEntity(EntityType.MOOSHROOM, world);
        }else if(rand < 0.5f){
            return new PandaEntity(EntityType.PANDA, world);
        }else if(rand < 0.6f){
            return new PigEntity(EntityType.PIG, world);
        }else if(rand < 0.7f){
            return new RabbitEntity(EntityType.RABBIT, world);
        }else if(rand < 0.8f){
            return new OcelotEntity(EntityType.OCELOT, world);
        }else if(rand < 0.9f){
            return new SheepEntity(EntityType.SHEEP, world);
        }else{
            return new WolfEntity(EntityType.WOLF, world);
        }
    }

    @Nonnull
    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, @Nonnull PlayerEntity player,
                                                 @Nonnull LivingEntity interactedEntity, @Nonnull Hand hand) {
        if(!stack.getOrCreateTag().getBoolean(SOUL_TAG)){
            if(interactedEntity instanceof MobultionZombieEntity &&
                    interactedEntity.getHealth() <= interactedEntity.getMaxHealth() * 0.5f){
                interactedEntity.setHealth(0);
                stack.getOrCreateTag().putBoolean(SOUL_TAG, true);
                return ActionResultType.sidedSuccess(player.level.isClientSide);
            }else{
                return ActionResultType.FAIL;
            }
        }else{
            return ActionResultType.PASS;
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
    public void inventoryTick(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull Entity holder, int slot, boolean selected) {
        if(holder instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) holder;
            int i = 0;
            for(ItemStack item : player.inventory.items){
                if(item.getItem() == Registration.LAMP.get()) i++;
            }
            if(i > 1){
                player.drop(itemStack, false);
                player.inventory.setItem(slot, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity oldItemEntity, ItemStack itemstack) {
        ItemEntity entity = new ItemEntity(world, oldItemEntity.getX(), oldItemEntity.getY(), oldItemEntity.getZ(), itemstack){
            @Override
            public void playerTouch(@Nonnull PlayerEntity player) {
                if(player.inventory.contains(new ItemStack(Registration.LAMP.get()))) return;
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
