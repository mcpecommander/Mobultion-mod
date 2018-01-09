package mcpecommander.mobultion.events;

import mcpecommander.mobultion.MobsConfig;
import mcpecommander.mobultion.entity.entities.skeletons.EntityWitheringSkeleton;
import mcpecommander.mobultion.entity.entities.spiders.EntityMagmaSpider;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpawnEvent {
	
	@SubscribeEvent
	public void spawnMagmaSpider(InitMapGenEvent e){
		if(e.getType() == EventType.NETHER_BRIDGE && MobsConfig.spiders.magma.netherFortressSpawn){
			MapGenNetherBridge gen = (MapGenNetherBridge) e.getNewGen();
			gen.getSpawnList().add(new SpawnListEntry(EntityMagmaSpider.class, 100, 5, 7));
		}
		if(e.getType() == EventType.NETHER_BRIDGE && MobsConfig.skeletons.withering.netherFortressSpawn){
			MapGenNetherBridge gen = (MapGenNetherBridge) e.getNewGen();
			gen.getSpawnList().add(new SpawnListEntry(EntityWitheringSkeleton.class, 100, 5, 7));
		}

	}
}
