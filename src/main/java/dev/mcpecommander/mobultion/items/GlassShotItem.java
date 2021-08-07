package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.entities.endermen.entities.GlassShotEntity;
import dev.mcpecommander.mobultion.items.renderers.GlassShotRenderer;
import dev.mcpecommander.mobultion.setup.ModSetup;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
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
import java.awt.*;

/* McpeCommander created on 07/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class GlassShotItem extends Item implements IAnimatable, ISyncable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public GlassShotItem() {
        super(new Properties().stacksTo(16).tab(ModSetup.ITEM_GROUP).setISTER(() -> GlassShotRenderer::new));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if(!world.isClientSide){
            GlassShotEntity glassShot = new GlassShotEntity(Registration.GLASSSHOT.get(), world);
            glassShot.setPos(player.getX(), player.getEyeY() - 0.1f, player.getZ());
            glassShot.shootFromRotation(player, player.xRot, player.yRot, 0, 1F, 0);
            glassShot.setOwner(player);
            glassShot.setColor(new Color(0x80FFFFFF, true));
            world.addFreshEntity(glassShot);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.abilities.instabuild) {
            player.getItemInHand(hand).shrink(1);
        }
        return ActionResult.sidedSuccess(player.getItemInHand(hand), world.isClientSide);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack itemStack, World world, @Nonnull Entity holder, int slotNumber, boolean isSelected) {
        if(!world.isClientSide){
            boolean started = itemStack.getOrCreateTag().getBoolean("Started");
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
