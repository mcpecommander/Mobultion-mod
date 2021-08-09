package dev.mcpecommander.mobultion.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.mcpecommander.mobultion.setup.ModSetup;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    @Nonnull
    @Override
    public ActionResultType useOn(@Nonnull ItemUseContext useContext) {
        PlayerEntity playerentity = useContext.getPlayer();
        World world = useContext.getLevel();
        BlockPos blockpos = useContext.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(useContext.getClickedFace());
        if (AbstractFireBlock.canBePlacedAt(world, blockpos1, useContext.getHorizontalDirection())) {
            world.playSound(playerentity, blockpos1, Registration.IGNITE_SOUND.get(), SoundCategory.BLOCKS,
                    1F, random.nextFloat() * 0.4F + 0.8F);
            BlockState blockstate1 = AbstractFireBlock.getState(world, blockpos1);
            world.setBlock(blockpos1, blockstate1, 11);
            ItemStack itemstack = useContext.getItemInHand();
            if (playerentity instanceof ServerPlayerEntity) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerentity, blockpos1, itemstack);
                itemstack.hurtAndBreak(1, playerentity, (p_219998_1_) -> {
                    p_219998_1_.broadcastBreakEvent(useContext.getHand());
                });
            }
            return ActionResultType.sidedSuccess(world.isClientSide());
        } else {
            return ActionResultType.FAIL;
        }
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
