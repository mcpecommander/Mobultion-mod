package dev.mcpecommander.mobultion.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.mcpecommander.mobultion.setup.ModSetup;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;

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
    public InteractionResult useOn(@Nonnull UseOnContext useContext) {
        Player playerentity = useContext.getPlayer();
        Level world = useContext.getLevel();
        BlockPos blockpos = useContext.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(useContext.getClickedFace());
        if (BaseFireBlock.canBePlacedAt(world, blockpos1, useContext.getHorizontalDirection())) {
            world.playSound(playerentity, blockpos1, Registration.IGNITE_SOUND.get(), SoundSource.BLOCKS,
                    1F, world.random.nextFloat() * 0.4F + 0.8F);
            BlockState blockstate1 = BaseFireBlock.getState(world, blockpos1);
            world.setBlock(blockpos1, blockstate1, 11);
            ItemStack itemstack = useContext.getItemInHand();
            if (playerentity instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)playerentity, blockpos1, itemstack);
                itemstack.hurtAndBreak(1, playerentity, (p_219998_1_) ->
                        p_219998_1_.broadcastBreakEvent(useContext.getHand()));
            }
            return InteractionResult.sidedSuccess(world.isClientSide());
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND ? attributes : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity hitEntity, @Nonnull LivingEntity holder) {
        stack.hurtAndBreak(1, holder,
                (livingEntity) -> livingEntity.broadcastBreakEvent(holder.getUsedItemHand()));
        hitEntity.setSecondsOnFire(7);
        return true;
    }
}
