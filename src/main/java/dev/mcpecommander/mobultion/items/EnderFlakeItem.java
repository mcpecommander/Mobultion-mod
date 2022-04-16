package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;

/* McpeCommander created on 06/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class EnderFlakeItem extends Item {

    public EnderFlakeItem() {
        super(new Properties().stacksTo(16).tab(ModSetup.ITEM_GROUP));
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW,
                SoundSource.NEUTRAL, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
        player.getCooldowns().addCooldown(this, 20);
        if (!world.isClientSide) {
            ThrownEnderpearl pearlEntity = new ThrownEnderpearl(world, player){
                @Override
                protected void onHit(@Nonnull HitResult result) {
                    if(result.getType() == HitResult.Type.ENTITY){
                        if(((EntityHitResult)result).getEntity() instanceof LivingEntity){
                            ((LivingEntity) ((EntityHitResult)result).getEntity()).
                                    addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 5, 5));
                            this.discard();
                            return;
                        }
                    }
                    Entity entity = this.getOwner();

                    for(int i = 0; i < 32; ++i) {
                        this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D,
                                this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
                    }

                    if (!this.level.isClientSide && this.isAlive()) {
                        if (entity instanceof ServerPlayer serverplayerentity) {
                            if (serverplayerentity.connection.getConnection().isConnected()
                                    && serverplayerentity.level == this.level && !serverplayerentity.isSleeping()) {
                                if (entity.isPassenger()) {
                                    entity.stopRiding();
                                }

                                entity.teleportTo(this.getX(), this.getY(), this.getZ());
                                entity.fallDistance = 0.0F;
                                entity.hurt(DamageSource.FALL, 2);

                            }
                        } else if (entity != null) {
                            entity.teleportTo(this.getX(), this.getY(), this.getZ());
                            entity.fallDistance = 0.0F;
                        }

                        this.discard();
                    }

                }
            };
            pearlEntity.setItem(itemstack);
            pearlEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 0.6F, 0F);
            world.addFreshEntity(pearlEntity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide());
    }

}
