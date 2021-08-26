package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.items.renderers.ThunderStaffRenderer;
import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
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

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items */
public class ThunderStaffItem extends Item implements IAnimatable, ISyncable {

    public AnimationFactory factory = new AnimationFactory(this);

    public ThunderStaffItem() {
        super(new Properties().stacksTo(1).tab(ModSetup.ITEM_GROUP).setNoRepair().setISTER(() -> ThunderStaffRenderer::new));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 1, this::predicate));
    }

    @Nonnull
    @Override
    public UseAction getUseAnimation(@Nonnull ItemStack itemStack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack itemStack) {
        return 200;
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull LivingEntity holder, int charge) {
        if(getUseDuration(itemStack) - charge < 40 && !world.isClientSide){
            final int id = GeckoLibUtil.guaranteeIDForStack(itemStack, (ServerWorld) world);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF
                    .with(() -> holder);
            GeckoLibNetwork.syncAnimation(target, this, id, 1);
        }
    }

    @Override
    public void onUseTick(@Nonnull World world, @Nonnull LivingEntity holder, @Nonnull ItemStack itemStack, int charge) {
        if(getUseDuration(itemStack) - charge > 35 && holder instanceof PlayerEntity){
            if(!world.isClientSide){
                holder.stopUsingItem();
                RayTraceResult result = getPOVHitResult(world, (PlayerEntity) holder);
                if(result.getType() == RayTraceResult.Type.MISS) return;
                LightningBoltEntity entity = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
                if(result instanceof BlockRayTraceResult){
                    entity.setPos(((BlockRayTraceResult) result).getBlockPos().getX(),
                            ((BlockRayTraceResult) result).getBlockPos().getY(),
                            ((BlockRayTraceResult) result).getBlockPos().getZ());
                }else{
                    Vector3d pos = ((EntityRayTraceResult)result).getEntity().position();
                    entity.setPos(pos.x, pos.y, pos.z);
                }

                world.addFreshEntity(entity);
                ((PlayerEntity) holder).getCooldowns().addCooldown(this, 200);
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
    private static RayTraceResult getPOVHitResult(World world, PlayerEntity player) {
        Vector3d from = player.getEyePosition(1.0F);
        Vector3d temp = player.getViewVector(1.0f);
        Vector3d to = from.add(temp.x * 60d, temp.y * 60d, temp.z * 60d);
        EntityRayTraceResult entityRayTraceResult = ProjectileHelper.getEntityHitResult(world, player, from, to,
                player.getBoundingBox().expandTowards(temp.x * 60, temp.y * 60, temp.z * 60)
                        .inflate( 2,  2,  2),
                entity -> entity != null && !entity.isSpectator());
        BlockRayTraceResult blockRayTraceResult = world.clip(new RayTraceContext(from, to, RayTraceContext.BlockMode.OUTLINE,
                RayTraceContext.FluidMode.NONE, player));
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
    public ActionResult<ItemStack> use(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if(hand == Hand.OFF_HAND) return ActionResult.fail(itemstack);
        player.startUsingItem(hand);
        if(!world.isClientSide) {
            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerWorld) world);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF
                    .with(() -> player);
            GeckoLibNetwork.syncAnimation(target, this, id, 0);
        }
        return ActionResult.consume(itemstack);
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
