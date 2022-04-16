package dev.mcpecommander.mobultion.setup;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 16/07/2021 inside the package - dev.mcpecommander.mobultion.setup */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = MODID)
public class SpawnSetup {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerSpawns(BiomeLoadingEvent event){
        switch(event.getCategory()){
            case ICY:
                event.getSpawns().addSpawn(MobCategory.MONSTER,
                        new MobSpawnSettings.SpawnerData(Registration.ICEENDERMAN.get(), 10, 1, 4));
                return;
            case BEACH:
                return;
            case DESERT:
                return;
            case EXTREME_HILLS:
                return;
            case FOREST:
                return;
            case JUNGLE:
                return;
            case MESA:
                return;
            case MUSHROOM:
                return;
            case NETHER:
                event.getSpawns().addSpawn(MobCategory.MONSTER,
                        new MobSpawnSettings.SpawnerData(Registration.MAGMAENDERMAN.get(), 10, 1, 2));
                return;
            case OCEAN:
                return;
            case PLAINS:

                event.getSpawns().addSpawn(MobCategory.AMBIENT,
                        new MobSpawnSettings.SpawnerData(Registration.GARDENERENDERMAN.get(), 2, 1, 2));
                return;
            case RIVER:
                return;
            case SAVANNA:
                return;
            case SWAMP:
                return;
            case TAIGA:
                return;
            case THEEND:
                return;
            default:
                return;
        }
    }



}
