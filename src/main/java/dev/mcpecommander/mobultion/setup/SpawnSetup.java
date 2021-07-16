package dev.mcpecommander.mobultion.setup;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.MobSpawnInfo;
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
                event.getSpawns().addSpawn(EntityClassification.MONSTER,
                        new MobSpawnInfo.Spawners(Registration.ICEENDERMAN.get(), 10, 1, 4));
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
                event.getSpawns().addSpawn(EntityClassification.MONSTER,
                        new MobSpawnInfo.Spawners(Registration.MAGMAENDERMAN.get(), 10, 1, 2));
                return;
            case OCEAN:
                return;
            case PLAINS:

                event.getSpawns().addSpawn(EntityClassification.AMBIENT,
                        new MobSpawnInfo.Spawners(Registration.GARDENERENDERMAN.get(), 2, 1, 2));
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
