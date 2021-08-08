package dev.mcpecommander.mobultion.events;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 18/07/2021 inside the package - dev.mcpecommander.mobultion.events */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onUseEvent(LivingEntityUseItemEvent.Finish event){
        ItemStack itemStack = event.getResultStack().copy();
        if(itemStack.isEmpty() || itemStack.getDamageValue() <= 0) return;
        if(event.getEntityLiving() instanceof PlayerEntity && itemStack.isDamageableItem() &&
                checkTool(itemStack.getItem()) && event.getEntityLiving().hasEffect(Effects.ABSORPTION)){
            itemStack.hurtAndBreak(itemStack.getDamageValue() -
                    event.getEntityLiving().getRandom().nextInt(Math.min(itemStack.getDamageValue(), 5)), event.getEntityLiving(),
                    p -> p.broadcastBreakEvent(event.getEntityLiving().getUsedItemHand()));
            event.setResultStack(itemStack);
        }
    }

    private static boolean checkTool(Item item){
        return item instanceof SwordItem || item instanceof ToolItem;
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event){
        if(event.getEntityLiving() instanceof WolfEntity && !event.getSource().isBypassInvul()){
            WolfEntity wolf = (WolfEntity) event.getEntityLiving();
            if(wolf.getItemBySlot(EquipmentSlotType.HEAD).getItem() == Registration.HALO.get()){
                event.setCanceled(true);
                wolf.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                wolf.setHealth(20);
                wolf.revive();
                wolf.removeAllEffects();
                wolf.addEffect(new EffectInstance(Effects.REGENERATION, 20 * 45, 1));
                wolf.addEffect(new EffectInstance(Effects.ABSORPTION, 20 * 5, 1));
                wolf.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 20 * 40, 0));
                wolf.playSound(Registration.HOLY_SOUND.get(), 1f, 1f - wolf.getRandom().nextFloat() * 0.2f);
                ((ServerWorld) wolf.level).getServer().getPlayerList().broadcastMessage( new TranslationTextComponent(
                        "But " + wolf.getName().getString() + " was revived by some mystical holy powers."),
                        ChatType.CHAT, Util.NIL_UUID);

            }
        }
    }
}
