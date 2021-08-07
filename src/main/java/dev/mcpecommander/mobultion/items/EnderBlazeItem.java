package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/* McpeCommander created on 07/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class EnderBlazeItem extends Item {

    public EnderBlazeItem() {
        super(new Properties().stacksTo(16).tab(ModSetup.ITEM_GROUP));
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW,
                SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        player.getCooldowns().addCooldown(this, 10);
        if (!world.isClientSide) {
            EnderPearlEntity pearlEntity = new EnderPearlEntity(world, player){
                @Override
                protected void onHit(@Nonnull RayTraceResult result) {
                    if(result.getType() == RayTraceResult.Type.ENTITY){
                        if(((EntityRayTraceResult)result).getEntity() instanceof LivingEntity){
                            ((EntityRayTraceResult)result).getEntity().setSecondsOnFire(10);
                            this.remove();
                            return;
                        }
                    }
                    Entity entity = this.getOwner();

                    for(int i = 0; i < 32; ++i) {
                        this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D,
                                this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
                    }

                    if (!this.level.isClientSide && this.isAlive()) {
                        if (entity instanceof ServerPlayerEntity) {
                            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entity;
                            if (serverplayerentity.connection.getConnection().isConnected()
                                    && serverplayerentity.level == this.level && !serverplayerentity.isSleeping()) {
                                if (entity.isPassenger()) {
                                    entity.stopRiding();
                                }

                                entity.teleportTo(this.getX(), this.getY(), this.getZ());
                                entity.fallDistance = 0.0F;
                                entity.hurt(DamageSource.FALL, 7);

                            }
                        } else if (entity != null) {
                            entity.teleportTo(this.getX(), this.getY(), this.getZ());
                            entity.fallDistance = 0.0F;
                        }

                        this.remove();
                    }

                }
            };
            pearlEntity.setItem(itemstack);
            pearlEntity.shootFromRotation(player, player.xRot, player.yRot, random.nextInt(4) - 2, 2F, 3F);
            world.addFreshEntity(pearlEntity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.abilities.instabuild) {
            itemstack.shrink(1);
        }

        return ActionResult.sidedSuccess(itemstack, world.isClientSide());
    }
}
