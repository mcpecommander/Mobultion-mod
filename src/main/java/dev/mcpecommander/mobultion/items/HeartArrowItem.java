package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.entities.skeletons.entities.HeartArrowEntity;
import dev.mcpecommander.mobultion.setup.ModSetup;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

/* McpeCommander created on 23/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HeartArrowItem extends ArrowItem {

    public HeartArrowItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP));

    }

    @Override
    public boolean isInfinite(@Nonnull ItemStack stack, @Nonnull ItemStack bow, @Nonnull Player player) {
        return false;
    }

    @Nonnull
    @Override
    public AbstractArrow createArrow(@Nonnull Level world, @Nonnull ItemStack arrowStack, LivingEntity shooter) {
        HeartArrowEntity arrow = new HeartArrowEntity(Registration.HEARTARROW.get(), world);
        arrow.setPos(shooter.getX(), shooter.getEyeY() - 0.1f, shooter.getZ());
        arrow.setOwner(shooter);
        return arrow;
    }
}
