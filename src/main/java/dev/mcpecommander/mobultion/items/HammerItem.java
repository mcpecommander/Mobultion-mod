package dev.mcpecommander.mobultion.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/* McpeCommander created on 28/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HammerItem extends Item {

    private final Multimap<Attribute, AttributeModifier> attributes;

    //TODO: check the damage balance
    public HammerItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).durability(250));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
                "Hammer damage", 5D, AttributeModifier.Operation.ADDITION));
        attributes = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND ? attributes : super.getAttributeModifiers(slot, stack);
    }
}
