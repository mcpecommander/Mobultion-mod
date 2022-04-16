package dev.mcpecommander.mobultion.items;

import dev.mcpecommander.mobultion.setup.ModSetup;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

/* McpeCommander created on 30/07/2021 inside the package - dev.mcpecommander.mobultion.items */
public class HealthPackItem extends Item {

    public HealthPackItem() {
        super(new Properties().tab(ModSetup.ITEM_GROUP).stacksTo(16));
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if(player.getAbilities().instabuild || player.getHealth() == player.getMaxHealth()) return InteractionResultHolder.pass(item);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(item);
    }

    @Nonnull
    @Override
    public SoundEvent getEatingSound() {
        return Registration.HEALING_SOUND.get();
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack item, @Nonnull Level world, @Nonnull LivingEntity holder) {
        holder.heal(20f);
        if(holder instanceof ServerPlayer){
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) holder, item);
            ((ServerPlayer) holder).awardStat(Stats.ITEM_USED.get(this));
        }
        if(holder instanceof Player && !((Player)holder).getAbilities().instabuild) item.shrink(1);

        return super.finishUsingItem(item, world, holder);
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack itemStack) {
        return 64;
    }

    @Nonnull
    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack item) {
        return UseAnim.EAT;
    }
}
