package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.entities.endermen.entities.GlassShotEntity;
import dev.mcpecommander.mobultion.items.renderers.GlassShotRenderer;
import dev.mcpecommander.mobultion.setup.ModSetup;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
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
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/* McpeCommander created on 07/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class GlassShotItem extends Item implements IAnimatable, ISyncable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public GlassShotItem() {
        super(new Properties().stacksTo(16).tab(ModSetup.ITEM_GROUP));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new GlassShotRenderer();

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
            GlassShotEntity glassShot = new GlassShotEntity(Registration.GLASSSHOT.get(), world);
            glassShot.setPos(player.getX(), player.getEyeY() - 0.1f, player.getZ());
            glassShot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1F, 0);
            glassShot.setOwner(player);
            glassShot.setColor(Color.ofRGBA(255, 255, 255, 127));
            world.addFreshEntity(glassShot);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            player.getItemInHand(hand).shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack itemStack, Level world, @Nonnull Entity holder, int slotNumber, boolean isSelected) {
        if(!world.isClientSide){
            boolean started = itemStack.getOrCreateTag().getBoolean("Started");
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
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, "controller");
        if(state == 0){
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("spin", true));
        }else if(state == 1){
            controller.markNeedsReload();
            controller.clearAnimationCache();
        }

    }
}
