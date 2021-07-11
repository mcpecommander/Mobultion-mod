package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.block.Block;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/* McpeCommander created on 10/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HayHatBlockItem extends BlockItem {

    public HayHatBlockItem(Block block) {
        super(block, new Item.Properties().tab(ModSetup.ITEM_GROUP).stacksTo(1).setNoRepair());
    }

    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
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
