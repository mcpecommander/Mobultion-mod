package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* McpeCommander created on 28/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HardHatItem extends Item {

    //TODO: adjust the durability.
    public HardHatItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).durability(200));
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        EquipmentSlotType equipmentslottype = MobEntity.getEquipmentSlotForItem(itemstack);
        ItemStack currentArmour = player.getItemBySlot(equipmentslottype);
        if (currentArmour.isEmpty()) {
            player.setItemSlot(equipmentslottype, itemstack.copy());
            itemstack.setCount(0);
            return ActionResult.sidedSuccess(itemstack, world.isClientSide());
        } else {
            return ActionResult.fail(itemstack);
        }
    }

    @Nullable
    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
        return EquipmentSlotType.HEAD;
    }
}
