package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.entities.skeletons.entities.HeartArrowEntity;
import dev.mcpecommander.mobultion.setup.ModSetup;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/* McpeCommander created on 23/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HeartArrowItem extends ArrowItem {

    public HeartArrowItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP));

    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, PlayerEntity player) {
        return false;
    }

    @Override
    public AbstractArrowEntity createArrow(World world, ItemStack arrowStack, LivingEntity shooter) {
        HeartArrowEntity arrow = new HeartArrowEntity(Registration.HEARTARROW.get(), world);
        arrow.setPos(shooter.getX(), shooter.getEyeY() - 0.1f, shooter.getZ());
        arrow.setOwner(shooter);
        return arrow;
    }
}
