package dev.mcpecommander.mobultion.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 13/07/2021 inside the package - dev.mcpecommander.mobultion.client */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void renderDebug(RenderWorldLastEvent event){

    }

}
