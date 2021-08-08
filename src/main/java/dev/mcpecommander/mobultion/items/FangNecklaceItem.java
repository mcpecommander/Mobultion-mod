package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

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
    public ActionResult<ItemStack> use(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {
        if(player.getHealth() < player.getMaxHealth() - 5f){
            player.startUsingItem(hand);
            return ActionResult.sidedSuccess(player.getItemInHand(hand), world.isClientSide);
        }
        return ActionResult.pass(player.getItemInHand(hand));
    }

    @Nonnull
    @Override
    public UseAction getUseAnimation(@Nonnull ItemStack stack) {
        return UseAction.BOW;
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull LivingEntity livingEntity) {
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
