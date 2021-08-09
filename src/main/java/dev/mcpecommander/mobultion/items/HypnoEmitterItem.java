package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.entities.spiders.entities.HypnoWaveEntity;
import dev.mcpecommander.mobultion.items.renderers.HypnoEmitterRenderer;
import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
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

/* McpeCommander created on 08/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HypnoEmitterItem extends Item implements IAnimatable, ISyncable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public HypnoEmitterItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).durability(150).setISTER(() -> HypnoEmitterRenderer::new));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if(!world.isClientSide){
            HypnoWaveEntity hypnoWave = new HypnoWaveEntity(player);
            hypnoWave.shootFromRotation(player, player.xRot, player.yRot, 0, 0.4F, 0);
            world.addFreshEntity(hypnoWave);
        }
        player.getCooldowns().addCooldown(this, 30);

        if (!player.abilities.instabuild) {
            player.getItemInHand(hand).hurtAndBreak(1, player,
                    (livingEntity) -> livingEntity.broadcastBreakEvent(player.getUsedItemHand()));;
        }
        return ActionResult.sidedSuccess(player.getItemInHand(hand), world.isClientSide);
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
    public void inventoryTick(ItemStack itemStack, World world, @Nonnull Entity holder, int slotNumber, boolean isSelected) {
        boolean started = itemStack.getOrCreateTag().getBoolean("Started");
        if(!world.isClientSide){
            final int id = GeckoLibUtil.guaranteeIDForStack(itemStack, (ServerWorld) world);
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