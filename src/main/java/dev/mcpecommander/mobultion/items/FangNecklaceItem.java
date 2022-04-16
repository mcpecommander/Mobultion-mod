package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

/* McpeCommander created on 08/08/2021 inside the package - dev.mcpecommander.mobultion.items */
public class FangNecklaceItem extends Item {

    public FangNecklaceItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP));
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack itemStack) {
        return 60;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
        if(player.getHealth() < player.getMaxHealth() - 5f){
            player.startUsingItem(hand);
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Nonnull
    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull LivingEntity livingEntity) {
        List<Entity> entities = livingEntity.level.getEntities(livingEntity, livingEntity.getBoundingBox().inflate(10), entity ->
            entity instanceof LivingEntity && entity.isAlive() && ((LivingEntity) entity).getHealth() > 2);
        if(!entities.isEmpty() && entities.size() > 2){
            entities.forEach(entity -> entity.hurt(DamageSource.GENERIC, 2));
            livingEntity.heal(Math.min(entities.size(), 7) * 2);
            return ItemStack.EMPTY;
        }
        return stack;
    }
}
