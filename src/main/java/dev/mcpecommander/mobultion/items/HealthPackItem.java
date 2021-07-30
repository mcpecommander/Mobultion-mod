package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/* McpeCommander created on 30/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HealthPackItem extends Item {

    public HealthPackItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).stacksTo(16));
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        ItemStack item = player.getItemInHand(hand);
        if(player.abilities.instabuild || player.getHealth() == player.getMaxHealth()) return ActionResult.pass(item);
        player.startUsingItem(hand);
        return ActionResult.consume(item);
    }

    @Nonnull
    @Override
    public SoundEvent getEatingSound() {
        return Registration.HEALING_SOUND.get();
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack item, @Nonnull World world, @Nonnull LivingEntity holder) {
        holder.heal(20f);
        if(holder instanceof ServerPlayerEntity){
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) holder, item);
            ((ServerPlayerEntity) holder).awardStat(Stats.ITEM_USED.get(this));
        }
        if(holder instanceof PlayerEntity && !((PlayerEntity)holder).abilities.instabuild) item.shrink(1);

        return super.finishUsingItem(item, world, holder);
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack itemStack) {
        return 64;
    }

    @Nonnull
    @Override
    public UseAction getUseAnimation(@Nonnull ItemStack item) {
        return UseAction.EAT;
    }
}
