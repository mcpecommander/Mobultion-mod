package dev.mcpecommander.mobultion.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/* McpeCommander created on 09/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class FireSwordItem extends Item {

    private final Multimap<Attribute, AttributeModifier> attributes;

    //TODO: check the damage balance
    public FireSwordItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).durability(250));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
                "Sword damage", 5D, AttributeModifier.Operation.ADDITION));
        attributes = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND ? attributes : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity hitEntity, @Nonnull LivingEntity holder) {
        stack.hurtAndBreak(1, holder,
                (livingEntity) -> livingEntity.broadcastBreakEvent(holder.getUsedItemHand()));
        hitEntity.setSecondsOnFire(7);
        return true;
    }
}
