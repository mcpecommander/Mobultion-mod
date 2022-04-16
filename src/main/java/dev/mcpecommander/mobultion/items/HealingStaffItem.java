package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.items.renderers.HealingStaffRenderer;
import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
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

/* McpeCommander created on 24/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HealingStaffItem extends Item implements IAnimatable, ISyncable {

    public AnimationFactory factory = new AnimationFactory(this);

    public HealingStaffItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).setNoRepair().durability(100));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new HealingStaffRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
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
    public int getUseDuration(@Nonnull ItemStack itemStack) {
        return 80;
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
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, "controller");
        if(state == 0){
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("rotate", true));
        }else if(state == 1){
            controller.markNeedsReload();
            controller.clearAnimationCache();
        }

    }
}
