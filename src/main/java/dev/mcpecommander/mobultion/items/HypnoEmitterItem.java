package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.entities.spiders.entities.HypnoWaveEntity;
import dev.mcpecommander.mobultion.items.renderers.HypnoEmitterRenderer;
import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/* McpeCommander created on 08/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HypnoEmitterItem extends Item implements IAnimatable, ISyncable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public HypnoEmitterItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).durability(150));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new HypnoEmitterRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if(!world.isClientSide){
            HypnoWaveEntity hypnoWave = new HypnoWaveEntity(player);
            hypnoWave.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 0.4F, 0);
            world.addFreshEntity(hypnoWave);
        }
        player.getCooldowns().addCooldown(this, 30);

        if (!player.getAbilities().instabuild) {
            player.getItemInHand(hand).hurtAndBreak(1, player,
                    (livingEntity) -> livingEntity.broadcastBreakEvent(player.getUsedItemHand()));
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide);
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event)
    {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController<>(this, "controller", 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, @Nonnull Entity holder, int slotNumber, boolean isSelected) {
        boolean started = itemStack.getOrCreateTag().getBoolean("Started");
        if(!world.isClientSide){
            final int id = GeckoLibUtil.guaranteeIDForStack(itemStack, (ServerLevel) world);
            if(isSelected && !started){
                final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF
                        .with(() -> holder);
                GeckoLibNetwork.syncAnimation(target, this, id, 0);
                itemStack.getOrCreateTag().putBoolean("Started", true);
            }else if(!isSelected && started){
                final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF
                        .with(() -> holder);
                GeckoLibNetwork.syncAnimation(target, this, id, 1);
                itemStack.getOrCreateTag().putBoolean("Started", false);
            }
        }
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, "controller");
        if(state == 0){
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("idle", true));
        }else if(state == 1){
            controller.markNeedsReload();
            controller.clearAnimationCache();
        }
    }
}
