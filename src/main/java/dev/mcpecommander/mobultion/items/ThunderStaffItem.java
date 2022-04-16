package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.items.renderers.ThunderStaffRenderer;
import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items */
public class ThunderStaffItem extends Item implements IAnimatable, ISyncable {

    public AnimationFactory factory = new AnimationFactory(this);

    public ThunderStaffItem() {
        super(new Properties().stacksTo(1).tab(ModSetup.ITEM_GROUP).setNoRepair());
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new ThunderStaffRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 1, this::predicate));
    }

    @Nonnull
    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack itemStack) {
        return 200;
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack itemStack, @Nonnull Level world, @Nonnull LivingEntity holder, int charge) {
        if(getUseDuration(itemStack) - charge < 40 && !world.isClientSide){
            final int id = GeckoLibUtil.guaranteeIDForStack(itemStack, (ServerLevel) world);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF
                    .with(() -> holder);
            GeckoLibNetwork.syncAnimation(target, this, id, 1);
        }
    }

    @Override
    public void onUseTick(@Nonnull Level world, @Nonnull LivingEntity holder, @Nonnull ItemStack itemStack, int charge) {
        if(getUseDuration(itemStack) - charge > 35 && holder instanceof Player){
            if(!world.isClientSide){
                holder.stopUsingItem();
                HitResult result = getPOVHitResult(world, (Player) holder);
                if(result.getType() == HitResult.Type.MISS) return;
                LightningBolt entity = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
                if(result instanceof BlockHitResult){
                    entity.setPos(((BlockHitResult) result).getBlockPos().getX(),
                            ((BlockHitResult) result).getBlockPos().getY(),
                            ((BlockHitResult) result).getBlockPos().getZ());
                }else{
                    Vec3 pos = ((EntityHitResult)result).getEntity().position();
                    entity.setPos(pos.x, pos.y, pos.z);
                }

                world.addFreshEntity(entity);
                ((Player) holder).getCooldowns().addCooldown(this, 200);
            }


        }
    }

    /**
     * Combined from Item.getPlayerPOVHitResult and ProjectileHelper.getEntityHitResult so it can return both entity and
     * block hits.
     * @param world: the world which the player is in.
     * @param player: the player that ray tracing begins from.
     * @return BlockRayTraceResult that has a non-null block pos and the type of collision.
     */
    private static HitResult getPOVHitResult(Level world, Player player) {
        Vec3 from = player.getEyePosition(1.0F);
        Vec3 temp = player.getViewVector(1.0f);
        Vec3 to = from.add(temp.x * 60d, temp.y * 60d, temp.z * 60d);
        EntityHitResult entityRayTraceResult = ProjectileUtil.getEntityHitResult(world, player, from, to,
                player.getBoundingBox().expandTowards(temp.x * 60, temp.y * 60, temp.z * 60)
                        .inflate( 2,  2,  2),
                entity -> entity != null && !entity.isSpectator());
        BlockHitResult blockRayTraceResult = world.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE, player));
        if(entityRayTraceResult == null) {
            return blockRayTraceResult;
        }else if (player.distanceToSqr(blockRayTraceResult.getBlockPos().getX(),
                blockRayTraceResult.getBlockPos().getY(),
                blockRayTraceResult.getBlockPos().getZ()) < player.distanceToSqr(entityRayTraceResult.getEntity())) {
            return blockRayTraceResult;
        }
        return entityRayTraceResult;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if(hand == InteractionHand.OFF_HAND) return InteractionResultHolder.fail(itemstack);
        player.startUsingItem(hand);
        if(!world.isClientSide) {
            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerLevel) world);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF
                    .with(() -> player);
            GeckoLibNetwork.syncAnimation(target, this, id, 0);
        }
        return InteractionResultHolder.consume(itemstack);
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, "controller");
        if (state == 0) {
            controller.markNeedsReload();
            controller.clearAnimationCache();
            controller.setAnimation(new AnimationBuilder().addAnimation("light", false));
        }else if (state == 1){
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("reset", false));
        }
    }
}
