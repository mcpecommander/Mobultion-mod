package dev.mcpecommander.mobultion.events;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 18/07/2021 inside the package - dev.mcpecommander.mobultion.events */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = MODID)
public class CommonEvents {

    public static void onUseEvent(LivingEntityUseItemEvent.Finish event){
        ItemStack itemStack = event.getResultStack().copy();
        if(itemStack.isEmpty() || itemStack.getDamageValue() <= 0) return;
        if(event.getEntityLiving() instanceof Player && itemStack.isDamageableItem() &&
                checkTool(itemStack.getItem()) && event.getEntityLiving().hasEffect(MobEffects.ABSORPTION)){
            itemStack.hurtAndBreak(itemStack.getDamageValue() -
                    event.getEntityLiving().getRandom().nextInt(Math.min(itemStack.getDamageValue(), 5)), event.getEntityLiving(),
                    p -> p.broadcastBreakEvent(event.getEntityLiving().getUsedItemHand()));
            event.setResultStack(itemStack);
        }
    }

    private static boolean checkTool(Item item){
        return item instanceof SwordItem || item instanceof DiggerItem;
    }

    @SubscribeEvent
    public static void onJumpEvent(LivingEvent.LivingJumpEvent event){
        Level level = event.getEntityLiving().level;
        if(event.getEntityLiving() instanceof Player player){
            ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
            if(!head.isEmpty() && head.getItem() == Registration.JOKERHAT.get()){
                if(!level.isClientSide){
                    final int id = GeckoLibUtil.guaranteeIDForStack(head, (ServerLevel) level);
                    final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF
                            .with(() -> player);
                    GeckoLibNetwork.syncAnimation(target, Registration.JOKERHAT.get(), id, 0);
                }else{
                    player.playSound(Registration.BELLS_SOUND.get(), 0.5f, 1f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event){
        if(event.getEntityLiving() instanceof Wolf wolf && !event.getSource().isBypassInvul()){
            if(wolf.getItemBySlot(EquipmentSlot.HEAD).getItem() == Registration.HALO.get()){
                event.setCanceled(true);
                wolf.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                wolf.setHealth(20);
                wolf.revive();
                wolf.removeAllEffects();
                wolf.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 45, 1));
                wolf.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 5, 1));
                wolf.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 40, 0));
                wolf.playSound(Registration.HOLY_SOUND.get(), 1f, 1f - wolf.getRandom().nextFloat() * 0.2f);
                ((ServerLevel) wolf.level).getServer().getPlayerList().broadcastMessage( new TranslatableComponent(
                        "But " + wolf.getName().getString() + " was revived by some mystical holy powers."),
                        ChatType.CHAT, Util.NIL_UUID);

            }
        }
    }
}
