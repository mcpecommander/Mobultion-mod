package dev.mcpecommander.mobultion.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.potion.Effects;
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
}
