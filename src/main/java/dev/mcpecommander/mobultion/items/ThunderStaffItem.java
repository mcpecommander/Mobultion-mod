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
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items */
public class ThunderStaffItem extends Item implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);

    public ThunderStaffItem() {
        super(new Properties().stacksTo(1).tab(ModSetup.ITEM_GROUP).setNoRepair().setISTER(() -> ThunderStaffRenderer::new));
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
    }

    @Override
    public UseAction getUseAnimation(ItemStack itemStack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 200;
    }


    @Override
    public void onUseTick(World world, LivingEntity holder, ItemStack itemStack, int charge) {
        if(getUseDuration(itemStack) - charge > 30 && holder instanceof PlayerEntity){
            if(!world.isClientSide){
                holder.stopUsingItem();
                RayTraceResult result = getPOVHitResult(world, (PlayerEntity) holder, RayTraceContext.FluidMode.ANY);
                System.out.println(result.getType());
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
                ((PlayerEntity) holder).getCooldowns().addCooldown(this, 400);
            }


        }
    }

    /**
     * Combined from Item.getPlayerPOVHitResult and ProjectileHelper.getEntityHitResult so it can return both entity and
     * block hits.
     * @param world: the world which the player is in.
     * @param player: the player that ray tracing begins from.
     * @param fluidMode: what liquids to collide with.
     * @return BlockRayTraceResult that has a non-null block pos and the type of collision.
     */
    private static RayTraceResult getPOVHitResult(World world, PlayerEntity player, RayTraceContext.FluidMode fluidMode) {
        Vector3d from = player.getEyePosition(1.0F);
        Vector3d temp = player.getViewVector(1.0f);
        Vector3d to = from.add(temp.x * 60d, temp.y * 60d, temp.z * 60d);
        EntityRayTraceResult entityRayTraceResult = ProjectileHelper.getEntityHitResult(world, player, from, to,
                player.getBoundingBox().expandTowards(temp.x * 60, temp.y * 60, temp.z * 60)
                        .inflate( 2,  2,  2),
                entity -> entity != null && !entity.isSpectator());
        if(entityRayTraceResult == null) {
            return world.clip(new RayTraceContext(from, to, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
        }else{
            return entityRayTraceResult;
        }
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if(world.isClientSide) {
            AnimationController controller = GeckoLibUtil.getControllerForStack(this.factory, itemstack, "controller");
            if(controller.getAnimationState() == AnimationState.Stopped) controller.clearAnimationCache();
            controller.setAnimation(new AnimationBuilder().addAnimation("light", false));
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
}
