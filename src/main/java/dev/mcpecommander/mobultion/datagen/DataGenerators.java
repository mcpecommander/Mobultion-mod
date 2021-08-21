package dev.mcpecommander.mobultion.datagen;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 26/07/2021 inside the package - dev.mcpecommander.mobultion.datagen */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    //This method gets called from runData and the includeServer and includeClient are kind of useless since I have --All
    //argument in the build gradle.
    //Here is where to add different data generators to be called when generating data.
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        if(event.includeServer()){
            event.getGenerator().addProvider(new RecipesGen(event.getGenerator()));
            event.getGenerator().addProvider(new LootTablesGen(event.getGenerator()));
        }
        if(event.includeClient()){
            event.getGenerator().addProvider(new SoundsGen(event.getGenerator(), event.getExistingFileHelper()));
            event.getGenerator().addProvider(new LanguageGenEN(event.getGenerator()));
        }
    }
}
