package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/* McpeCommander created on 16/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class ForestBowItem extends BowItem {

    public ForestBowItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).stacksTo(1).durability(100));
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack bowItemStack, @Nonnull Level world, @Nonnull LivingEntity shooter, int remainingUseTicks) {
        if (shooter instanceof Player playerentity) {
            //instabuild is creative mode for fuck's sake.
            boolean infiniteArrows = playerentity.getAbilities().instabuild ||
                    EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bowItemStack) > 0;

            ItemStack itemstack = playerentity.getProjectile(bowItemStack);
            if (!itemstack.isEmpty() || infiniteArrows) {
                //In case the player is in creative mode or has infinity enchantment.
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float shootingPower = getPowerForTime(this.getUseDuration(bowItemStack) - remainingUseTicks);
                if (shootingPower > 0.1f) {
                    boolean flag1 = playerentity.getAbilities().instabuild ||
                            (itemstack.getItem() instanceof ArrowItem &&
                                    ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, bowItemStack, playerentity));
                    if (!world.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrow arrowEntity = arrowitem.createArrow(world, itemstack, playerentity);
                        arrowEntity = customArrow(arrowEntity);
                        arrowEntity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot(),
                                0.0F, shootingPower * 3.0F, 1.0F);
                        if (shootingPower == 1.0F) {
                            arrowEntity.setCritArrow(true);
                        }

                        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bowItemStack);
                        if (powerLevel > 0) {
                            arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() + (double)powerLevel * 0.5D + 0.5D);
                        }

                        int knockbackLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bowItemStack);
                        if (knockbackLevel > 0) {
                            arrowEntity.setKnockback(knockbackLevel);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bowItemStack) > 0) {
                            arrowEntity.setSecondsOnFire(100);
                        }

                        bowItemStack.hurtAndBreak(1, playerentity,
                                (livingEntity) -> livingEntity.broadcastBreakEvent(playerentity.getUsedItemHand()));
                        if (flag1 || playerentity.getAbilities().instabuild &&
                                (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            arrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        world.addFreshEntity(arrowEntity);
                    }

                    world.playSound(null, playerentity.getX(), playerentity.getY(), playerentity.getZ(),
                            SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                            1.0F, 1.0F / (world.random.nextFloat() * 0.4F + 1.2F) + shootingPower * 0.5F);
                    if (!flag1 && !playerentity.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.getInventory().removeItem(itemstack);
                        }
                    }

                    playerentity.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player shooter, @Nonnull InteractionHand hand) {
        ItemStack itemstack = shooter.getItemInHand(hand);
        if (!shooter.getAbilities().instabuild && shooter.getProjectile(itemstack).isEmpty()) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            shooter.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public static float getPowerForTime(int chargeTicks) {
        float chargeSeconds = (float)chargeTicks / 20.0F;
        chargeSeconds = (chargeSeconds * chargeSeconds + chargeSeconds * 2.0F) / 3.0F;
        return Math.min(chargeSeconds, 1f);
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack itemStack) {
        return 72000;
    }

    @Nonnull
    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Nonnull
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Nonnull
    @Override
    public AbstractArrow customArrow(@Nonnull AbstractArrow arrow) {
        return arrow;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }
}
