package dev.mcpecommander.mobultion.datagen;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 26/07/2021 inside the package - dev.mcpecommander.mobultion.datagen */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        if(event.includeServer()){
            event.getGenerator().addProvider(new LootTablesGen(event.getGenerator()));
        }
    }
}
