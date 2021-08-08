package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/* McpeCommander created on 16/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class ForestBowItem extends BowItem {

    public ForestBowItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).stacksTo(1).durability(100));
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack bowItemStack, @Nonnull World world, @Nonnull LivingEntity shooter, int remainingUseTicks) {
        if (shooter instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)shooter;
            //instabuild is creative mode for fuck's sake.
            boolean infiniteArrows = playerentity.abilities.instabuild ||
                    EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bowItemStack) > 0;

            ItemStack itemstack = playerentity.getProjectile(bowItemStack);
            if (!itemstack.isEmpty() || infiniteArrows) {
                //In case the player is in creative mode or has infinity enchantment.
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float shootingPower = getPowerForTime(this.getUseDuration(bowItemStack) - remainingUseTicks);
                if (shootingPower > 0.1f) {
                    boolean flag1 = playerentity.abilities.instabuild ||
                            (itemstack.getItem() instanceof ArrowItem &&
                                    ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, bowItemStack, playerentity));
                    if (!world.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrowEntity arrowEntity = arrowitem.createArrow(world, itemstack, playerentity);
                        arrowEntity = customArrow(arrowEntity);
                        arrowEntity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot,
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
                        if (flag1 || playerentity.abilities.instabuild &&
                                (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            arrowEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }

                        world.addFreshEntity(arrowEntity);
                    }

                    world.playSound(null, playerentity.getX(), playerentity.getY(), playerentity.getZ(),
                            SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS,
                            1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + shootingPower * 0.5F);
                    if (!flag1 && !playerentity.abilities.instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.inventory.removeItem(itemstack);
                        }
                    }

                    playerentity.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, PlayerEntity shooter, @Nonnull Hand hand) {
        ItemStack itemstack = shooter.getItemInHand(hand);
        if (!shooter.abilities.instabuild && !shooter.getProjectile(itemstack).isEmpty()) {
            return ActionResult.fail(itemstack);
        } else {
            shooter.startUsingItem(hand);
            return ActionResult.consume(itemstack);
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
    public UseAction getUseAnimation(@Nonnull ItemStack itemStack) {
        return UseAction.BOW;
    }

    @Nonnull
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Nonnull
    @Override
    public AbstractArrowEntity customArrow(@Nonnull AbstractArrowEntity arrow) {
        return arrow;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }
}
