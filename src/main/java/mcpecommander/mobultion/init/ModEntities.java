package mcpecommander.mobultion.init;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.endermen.EntityEnderProjectile;
import mcpecommander.mobultion.entity.entities.endermen.EntityGardenerEnderman;
import mcpecommander.mobultion.entity.entities.endermen.EntityGlassEnderman;
import mcpecommander.mobultion.entity.entities.endermen.EntityGlassShot;
import mcpecommander.mobultion.entity.entities.endermen.EntityIceEnderman;
import mcpecommander.mobultion.entity.entities.endermen.EntityMagmaEnderman;
import mcpecommander.mobultion.entity.entities.endermen.EntityWanderingEnderman;
import mcpecommander.mobultion.entity.entities.mites.EntityWoodMite;
import mcpecommander.mobultion.entity.entities.skeletons.EntityCorruptedSkeleton;
import mcpecommander.mobultion.entity.entities.skeletons.EntityHeartArrow;
import mcpecommander.mobultion.entity.entities.skeletons.EntityJokerSkeleton;
import mcpecommander.mobultion.entity.entities.skeletons.EntityMagmaArrow;
import mcpecommander.mobultion.entity.entities.skeletons.EntityMagmaSkeleton;
import mcpecommander.mobultion.entity.entities.skeletons.EntityShamanSkeleton;
import mcpecommander.mobultion.entity.entities.skeletons.EntitySkeletonRemains;
import mcpecommander.mobultion.entity.entities.skeletons.EntitySniperSkeleton;
import mcpecommander.mobultion.entity.entities.skeletons.EntityVampireSkeleton;
import mcpecommander.mobultion.entity.entities.skeletons.EntityWitheringSkeleton;
import mcpecommander.mobultion.entity.entities.spiders.EntityAngelSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntityHypnoBall;
import mcpecommander.mobultion.entity.entities.spiders.EntityHypnoSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntityMagmaSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntityMiniSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntityMotherSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntitySorcererSpider;
import mcpecommander.mobultion.entity.entities.spiders.EntitySpeedySpider;
import mcpecommander.mobultion.entity.entities.spiders.EntitySpiderEgg;
import mcpecommander.mobultion.entity.entities.spiders.EntityWitherSpider;
import mcpecommander.mobultion.entity.entities.zombies.EntityDoctorZombie;
import mcpecommander.mobultion.entity.entities.zombies.EntityGoroZombie;
import mcpecommander.mobultion.entity.entities.zombies.EntityKnightZombie;
import mcpecommander.mobultion.entity.entities.zombies.EntityMagmaZombie;
import mcpecommander.mobultion.entity.entities.zombies.EntityRavenousZombie;
import mcpecommander.mobultion.entity.entities.zombies.EntityWorkerZombie;
import mcpecommander.mobultion.entity.renderer.endermenRenderer.RenderEnderProjectile;
import mcpecommander.mobultion.entity.renderer.endermenRenderer.RenderGardenerEnderman;
import mcpecommander.mobultion.entity.renderer.endermenRenderer.RenderGlassEnderman;
import mcpecommander.mobultion.entity.renderer.endermenRenderer.RenderGlassShot;
import mcpecommander.mobultion.entity.renderer.endermenRenderer.RenderIceEnderman;
import mcpecommander.mobultion.entity.renderer.endermenRenderer.RenderMagmaEnderman;
import mcpecommander.mobultion.entity.renderer.endermenRenderer.RenderWanderingEnderman;
import mcpecommander.mobultion.entity.renderer.mitesRenderer.RenderWoodMite;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderCorruptedSkeleton;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderHeartArrow;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderJokerSkeleton;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderMagmaArrow;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderMagmaSkeleton;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderShamanSkeleton;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderSkeletonRemains;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderSniperSkeleton;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderVampireSkeleton;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderWitheringSkeleton;
import mcpecommander.mobultion.entity.renderer.spidersRenderer.RenderAngelSpider;
import mcpecommander.mobultion.entity.renderer.spidersRenderer.RenderHypnoBall;
import mcpecommander.mobultion.entity.renderer.spidersRenderer.RenderHypnoSpider;
import mcpecommander.mobultion.entity.renderer.spidersRenderer.RenderMagmaSpider;
import mcpecommander.mobultion.entity.renderer.spidersRenderer.RenderMiniSpider;
import mcpecommander.mobultion.entity.renderer.spidersRenderer.RenderMotherSpider;
import mcpecommander.mobultion.entity.renderer.spidersRenderer.RenderSorcererSpider;
import mcpecommander.mobultion.entity.renderer.spidersRenderer.RenderSpeedySpider;
import mcpecommander.mobultion.entity.renderer.spidersRenderer.RenderSpiderEgg;
import mcpecommander.mobultion.entity.renderer.spidersRenderer.RenderWitherSpider;
import mcpecommander.mobultion.entity.renderer.zombiesRenderer.RenderDoctorZombie;
import mcpecommander.mobultion.entity.renderer.zombiesRenderer.RenderGoroZombie;
import mcpecommander.mobultion.entity.renderer.zombiesRenderer.RenderKnightZombie;
import mcpecommander.mobultion.entity.renderer.zombiesRenderer.RenderMagmaZombie;
import mcpecommander.mobultion.entity.renderer.zombiesRenderer.RenderRavenousZombie;
import mcpecommander.mobultion.entity.renderer.zombiesRenderer.RenderWorkerZombie;
import mcpecommander.mobultion.mobConfigs.EndermenConfig;
import mcpecommander.mobultion.mobConfigs.SkeletonsConfig;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import mcpecommander.mobultion.mobConfigs.ZombiesConfig;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModEntities {
	private static Biome[] getBiomes(String[] string) {
		List<Biome> list = new ArrayList();
		Biome[] biome = {};
		if (string == null || string.length == 0) {
			return biome;
		}
		if (string[0].equals("all")) {
			list.addAll(ForgeRegistries.BIOMES.getValuesCollection());
			list.remove(ForgeRegistries.BIOMES.getValue(new ResourceLocation("sky")));
			list.remove(ForgeRegistries.BIOMES.getValue(new ResourceLocation("hell")));
			return list.toArray(biome);
		}
		for (String id : string) {

			Biome add = ForgeRegistries.BIOMES.getValue(new ResourceLocation(id));
			
			if (add != null) {
				list.add(add);
			} else {
				MobultionMod.logger.log(Level.ERROR, "NPE, The id " + id
						+ " is probably misswritten, PS:Any and every biome must be written in this form MODID:BiomeRegistryName");
			}
		}
		if (!list.isEmpty()) {
			return list.toArray(biome);
		}

		return biome;

	}

	@SubscribeEvent
	public static void init(RegistryEvent.Register<EntityEntry> event) {
		int id = 0;
		// MagmaSpiderInit
		EntityEntry magma = EntityEntryBuilder.create().entity(EntityMagmaSpider.class)
				.id(Reference.MobultionEntities.MAGMASPIDER.getRegistryName(), id++)
				.name(Reference.MobultionEntities.MAGMASPIDER.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x230E0E, 0xF01414)
				.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.magma.spawnRates.weight,
						SpidersConfig.spiders.magma.spawnRates.min, SpidersConfig.spiders.magma.spawnRates.max,
						getBiomes(SpidersConfig.spiders.magma.spawnRates.biomes))
				.build();
		event.getRegistry().register(magma);

		// SpeedySpiderInit
		EntityEntry speedy = EntityEntryBuilder.create().entity(EntitySpeedySpider.class)
				.id(Reference.MobultionEntities.SPEEDYSPIDER.getRegistryName(), id++)
				.name(Reference.MobultionEntities.SPEEDYSPIDER.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x0C0CDB, 0x6666D0)
				.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.speedy.spawnRates.weight,
						SpidersConfig.spiders.speedy.spawnRates.min, SpidersConfig.spiders.speedy.spawnRates.max,
						getBiomes(SpidersConfig.spiders.speedy.spawnRates.biomes))
				.build();
		event.getRegistry().register(speedy);

		// WitherSpiderInit
		EntityEntry wither = EntityEntryBuilder.create().entity(EntityWitherSpider.class)
				.id(Reference.MobultionEntities.WITHERSPIDER.getRegistryName(), id++)
				.name(Reference.MobultionEntities.WITHERSPIDER.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x666666, 0x444444)
				.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.wither.spawnRates.weight,
						SpidersConfig.spiders.wither.spawnRates.min, SpidersConfig.spiders.wither.spawnRates.max,
						getBiomes(SpidersConfig.spiders.wither.spawnRates.biomes))
				.build();
		event.getRegistry().register(wither);

		// MiniSpiderInit
		EntityEntry mini = EntityEntryBuilder.create().entity(EntityMiniSpider.class)
				.id(Reference.MobultionEntities.MINISPIDER.getRegistryName(), id++)
				.name(Reference.MobultionEntities.MINISPIDER.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0xF3F3F3, 0xF4CCCC).build();
		event.getRegistry().register(mini);

		// SorcererSpiderInit
		EntityEntry sorcerer = EntityEntryBuilder.create().entity(EntitySorcererSpider.class)
				.id(Reference.MobultionEntities.SORCERERSPIDER.getRegistryName(), id++)
				.name(Reference.MobultionEntities.SORCERERSPIDER.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x6AA84F, 0x12436F)
				.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.sorcerer.spawnRates.weight,
						SpidersConfig.spiders.sorcerer.spawnRates.min, SpidersConfig.spiders.sorcerer.spawnRates.max,
						getBiomes(SpidersConfig.spiders.sorcerer.spawnRates.biomes))
				.build();
		event.getRegistry().register(sorcerer);

		// AngelSpiderInit
		EntityEntry angel = EntityEntryBuilder.create().entity(EntityAngelSpider.class)
				.id(Reference.MobultionEntities.ANGELSPIDER.getRegistryName(), id++)
				.name(Reference.MobultionEntities.ANGELSPIDER.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0xFFFFFF, 0xFFFF53)
				.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.angel.spawnRates.weight,
						SpidersConfig.spiders.angel.spawnRates.min, SpidersConfig.spiders.angel.spawnRates.max,
						getBiomes(SpidersConfig.spiders.angel.spawnRates.biomes))
				.build();
		event.getRegistry().register(angel);

		// MotherSpiderInit and its egg
		EntityEntry mother = EntityEntryBuilder.create().entity(EntityMotherSpider.class)
				.id(Reference.MobultionEntities.MOTHERSPIDER.getRegistryName(), id++)
				.name(Reference.MobultionEntities.MOTHERSPIDER.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x444444, 0x9D8888)
				.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.mother.spawnRates.weight,
						SpidersConfig.spiders.mother.spawnRates.min, SpidersConfig.spiders.mother.spawnRates.max,
						getBiomes(SpidersConfig.spiders.mother.spawnRates.biomes))
				.build();
		EntityEntry egg = EntityEntryBuilder.create().entity(EntitySpiderEgg.class)
				.id(Reference.MobultionEntities.SPIDEREGG.getRegistryName(), id++)
				.name(Reference.MobultionEntities.SPIDEREGG.getUnlocalizedName()).tracker(64, 3, true).build();
		event.getRegistry().registerAll(mother, egg);

		// HypnoSpiderInit and his ball
		EntityEntry hypno = EntityEntryBuilder.create().entity(EntityHypnoSpider.class)
				.id(Reference.MobultionEntities.HYPNOSPIDER.getRegistryName(), id++)
				.name(Reference.MobultionEntities.HYPNOSPIDER.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0xDD06DD, 0xF736F7)
				.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.hypno.spawnRates.weight,
						SpidersConfig.spiders.hypno.spawnRates.min, SpidersConfig.spiders.hypno.spawnRates.max,
						getBiomes(SpidersConfig.spiders.hypno.spawnRates.biomes))
				.build();
		EntityEntry ball = EntityEntryBuilder.create().entity(EntityHypnoBall.class)
				.id(Reference.MobultionEntities.HYPNOBALL.getRegistryName(), id++)
				.name(Reference.MobultionEntities.HYPNOBALL.getUnlocalizedName()).tracker(64, 3, true).build();
		event.getRegistry().registerAll(hypno, ball);

		// SkeletonRemains
		EntityEntry remains = EntityEntryBuilder.create().entity(EntitySkeletonRemains.class)
				.id(Reference.MobultionEntities.SKELETONREMAINS.getRegistryName(), id++)
				.name(Reference.MobultionEntities.SKELETONREMAINS.getUnlocalizedName()).tracker(64, 3, true).build();
		event.getRegistry().register(remains);

		// WitheringSkeletonInit
		EntityEntry withering = EntityEntryBuilder.create().entity(EntityWitheringSkeleton.class)
				.id(Reference.MobultionEntities.WITHERINGSKELETON.getRegistryName(), id++)
				.name(Reference.MobultionEntities.WITHERINGSKELETON.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x5C5151, 0xCCCCCC)
				.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.withering.spawnRates.weight,
						SkeletonsConfig.skeletons.withering.spawnRates.min,
						SkeletonsConfig.skeletons.withering.spawnRates.max,
						getBiomes(SkeletonsConfig.skeletons.withering.spawnRates.biomes))
				.build();
		event.getRegistry().register(withering);

		// ShamanSkeletonInit
		EntityEntry shaman = EntityEntryBuilder.create().entity(EntityShamanSkeleton.class)
				.id(Reference.MobultionEntities.SHAMANSKELETON.getRegistryName(), id++)
				.name(Reference.MobultionEntities.SHAMANSKELETON.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x050572, 0x741B47)
				.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.shaman.spawnRates.weight,
						SkeletonsConfig.skeletons.shaman.spawnRates.min,
						SkeletonsConfig.skeletons.shaman.spawnRates.max,
						getBiomes(SkeletonsConfig.skeletons.shaman.spawnRates.biomes))
				.build();
		event.getRegistry().register(shaman);

		// JokerSkeleton and its Arrow Init
		EntityEntry joker = EntityEntryBuilder.create().entity(EntityJokerSkeleton.class)
				.id(Reference.MobultionEntities.JOKERSKELETON.getRegistryName(), id++)
				.name(Reference.MobultionEntities.JOKERSKELETON.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0xFF0000, 0xFFFF00)
				.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.joker.spawnRates.weight,
						SkeletonsConfig.skeletons.joker.spawnRates.min, SkeletonsConfig.skeletons.joker.spawnRates.max,
						getBiomes(SkeletonsConfig.skeletons.joker.spawnRates.biomes))
				.build();
		event.getRegistry().register(joker);

		EntityEntry heartArrow = EntityEntryBuilder.create().entity(EntityHeartArrow.class)
				.id(Reference.MobultionEntities.HEARTARROW.getRegistryName(), id++)
				.name(Reference.MobultionEntities.HEARTARROW.getUnlocalizedName()).tracker(64, 3, true).build();
		event.getRegistry().register(heartArrow);

		// SniperSkeleton
		EntityEntry sniper = EntityEntryBuilder.create().entity(EntitySniperSkeleton.class)
				.id(Reference.MobultionEntities.SNIPERSKELETON.getRegistryName(), id++)
				.name(Reference.MobultionEntities.SNIPERSKELETON.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x38761D, 0x93C47D)
				.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.sniper.spawnRates.weight,
						SkeletonsConfig.skeletons.sniper.spawnRates.min,
						SkeletonsConfig.skeletons.sniper.spawnRates.max,
						getBiomes(SkeletonsConfig.skeletons.sniper.spawnRates.biomes))
				.build();
		event.getRegistry().register(sniper);

		// MagmaSkeleton
		EntityEntry skeleton_magma = EntityEntryBuilder.create().entity(EntityMagmaSkeleton.class)
				.id(Reference.MobultionEntities.MAGMASKELETON.getRegistryName(), id++)
				.name(Reference.MobultionEntities.MAGMASKELETON.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x811616, 0xFD1D1D)
				.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.magma.spawnRates.weight,
						SkeletonsConfig.skeletons.magma.spawnRates.min, SkeletonsConfig.skeletons.magma.spawnRates.max,
						getBiomes(SkeletonsConfig.skeletons.magma.spawnRates.biomes))
				.build();
		event.getRegistry().register(skeleton_magma);

		// Magmaarrow
		EntityEntry magmaArrow = EntityEntryBuilder.create().entity(EntityMagmaArrow.class)
				.id(Reference.MobultionEntities.MAGMAARROW.getRegistryName(), id++)
				.name(Reference.MobultionEntities.MAGMAARROW.getUnlocalizedName()).tracker(64, 3, true).build();
		event.getRegistry().register(magmaArrow);

		// CorruptedSkeleton
		EntityEntry corrupted = EntityEntryBuilder.create().entity(EntityCorruptedSkeleton.class)
				.id(Reference.MobultionEntities.CORRUPTEDSKELETON.getRegistryName(), id++)
				.name(Reference.MobultionEntities.CORRUPTEDSKELETON.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x745F1D, 0x927006)
				.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.corrupted.spawnRates.weight,
						SkeletonsConfig.skeletons.corrupted.spawnRates.min,
						SkeletonsConfig.skeletons.corrupted.spawnRates.max,
						getBiomes(SkeletonsConfig.skeletons.corrupted.spawnRates.biomes))
				.build();
		event.getRegistry().register(corrupted);

		// VampireSkeleton
		EntityEntry vampire = EntityEntryBuilder.create().entity(EntityVampireSkeleton.class)
				.id(Reference.MobultionEntities.VAMPIRESKELETON.getRegistryName(), id++)
				.name(Reference.MobultionEntities.VAMPIRESKELETON.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0xBB8A8A, 0x540D0D)
				.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.vampire.spawnRates.weight,
						SkeletonsConfig.skeletons.vampire.spawnRates.min,
						SkeletonsConfig.skeletons.vampire.spawnRates.max,
						getBiomes(SkeletonsConfig.skeletons.vampire.spawnRates.biomes))
				.build();
		event.getRegistry().register(vampire);

		// KnightZombie
		EntityEntry knight = EntityEntryBuilder.create().entity(EntityKnightZombie.class)
				.id(Reference.MobultionEntities.KNIGHTZOMBIE.getRegistryName(), id++)
				.name(Reference.MobultionEntities.KNIGHTZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0xEEEEEE, 0xD0E0E3)
				.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.knight.spawnRates.weight,
						ZombiesConfig.zombies.knight.spawnRates.min, ZombiesConfig.zombies.knight.spawnRates.max,
						getBiomes(ZombiesConfig.zombies.knight.spawnRates.biomes))
				.build();
		event.getRegistry().register(knight);

		// WorkerZombie
		EntityEntry worker = EntityEntryBuilder.create().entity(EntityWorkerZombie.class)
				.id(Reference.MobultionEntities.WORKERZOMBIE.getRegistryName(), id++)
				.name(Reference.MobultionEntities.WORKERZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0xFFE599, 0xFFFF00)
				.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.worker.spawnRates.weight,
						ZombiesConfig.zombies.worker.spawnRates.min, ZombiesConfig.zombies.worker.spawnRates.max,
						getBiomes(ZombiesConfig.zombies.worker.spawnRates.biomes))
				.build();
		event.getRegistry().register(worker);

		// MagmaZombie
		EntityEntry zombie_magma = EntityEntryBuilder.create().entity(EntityMagmaZombie.class)
				.id(Reference.MobultionEntities.MAGMAZOMBIE.getRegistryName(), id++)
				.name(Reference.MobultionEntities.MAGMAZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0xFFF144, 0xCC0000)
				.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.magma.spawnRates.weight,
						ZombiesConfig.zombies.magma.spawnRates.min, ZombiesConfig.zombies.magma.spawnRates.max,
						getBiomes(ZombiesConfig.zombies.magma.spawnRates.biomes))
				.build();
		event.getRegistry().register(zombie_magma);

		// DoctorZombie
		EntityEntry doctor = EntityEntryBuilder.create().entity(EntityDoctorZombie.class)
				.id(Reference.MobultionEntities.DOCTORZOMBIE.getRegistryName(), id++)
				.name(Reference.MobultionEntities.DOCTORZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0xFFFFFF, 0xFD1D1D)
				.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.doctor.spawnRates.weight,
						ZombiesConfig.zombies.doctor.spawnRates.min, ZombiesConfig.zombies.doctor.spawnRates.max,
						getBiomes(ZombiesConfig.zombies.doctor.spawnRates.biomes))
				.build();
		event.getRegistry().register(doctor);

		// GoroZombie
		EntityEntry goro = EntityEntryBuilder.create().entity(EntityGoroZombie.class)
				.id(Reference.MobultionEntities.GOROZOMBIE.getRegistryName(), id++)
				.name(Reference.MobultionEntities.GOROZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x95BD84, 0xCEA937)
				.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.goro.spawnRates.weight,
						ZombiesConfig.zombies.goro.spawnRates.min, ZombiesConfig.zombies.goro.spawnRates.max,
						getBiomes(ZombiesConfig.zombies.goro.spawnRates.biomes))
				.build();
		event.getRegistry().register(goro);

		// RavenousZombie
		EntityEntry ravenous = EntityEntryBuilder.create().entity(EntityRavenousZombie.class)
				.id(Reference.MobultionEntities.RAVENOUSZOMBIE.getRegistryName(), id++)
				.name(Reference.MobultionEntities.RAVENOUSZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x6AA84F, 0xD41A1A)
				.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.ravenous.spawnRates.weight,
						ZombiesConfig.zombies.ravenous.spawnRates.min, ZombiesConfig.zombies.ravenous.spawnRates.max,
						getBiomes(ZombiesConfig.zombies.ravenous.spawnRates.biomes))
				.build();
		event.getRegistry().register(ravenous);

		// MagmaEnderman
		EntityEntry enderman_magma = EntityEntryBuilder.create().entity(EntityMagmaEnderman.class)
				.id(Reference.MobultionEntities.MAGMAENDERMAN.getRegistryName(), id++)
				.name(Reference.MobultionEntities.MAGMAENDERMAN.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0xB40A0A, 0xE7E740)
				.spawn(EnumCreatureType.MONSTER, EndermenConfig.endermen.magma.spawnRates.weight,
						EndermenConfig.endermen.magma.spawnRates.min, EndermenConfig.endermen.magma.spawnRates.max,
						getBiomes(EndermenConfig.endermen.magma.spawnRates.biomes))
				.build();
		for (SpawnListEntry entry : Biome.getBiomeForId(8).getSpawnableList(EnumCreatureType.MONSTER)) {
			if (entry.entityClass == EntityEnderman.class) {
				Biome.getBiomeForId(8).getSpawnableList(EnumCreatureType.MONSTER).remove(entry);
				break;
			}
		}
		event.getRegistry().register(enderman_magma);

		// IceEnderman
		EntityEntry ice = EntityEntryBuilder.create().entity(EntityIceEnderman.class)
				.id(Reference.MobultionEntities.ICEENDERMAN.getRegistryName(), id++)
				.name(Reference.MobultionEntities.ICEENDERMAN.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0xC5F9F9, 0x30ABAB)
				.spawn(EnumCreatureType.MONSTER, EndermenConfig.endermen.ice.spawnRates.weight,
						EndermenConfig.endermen.ice.spawnRates.min, EndermenConfig.endermen.ice.spawnRates.max,
						getBiomes(EndermenConfig.endermen.ice.spawnRates.biomes))
				.build();
		event.getRegistry().register(ice);

		//Wandering enderman
		EntityEntry wandering = EntityEntryBuilder.create().entity(EntityWanderingEnderman.class)
				.id(Reference.MobultionEntities.WANDERINGENDERMAN.getRegistryName(), id++)
				.name(Reference.MobultionEntities.WANDERINGENDERMAN.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x422C01, 0x036303)
				 .spawn(EnumCreatureType.MONSTER,
				 EndermenConfig.endermen.wandering.spawnRates.weight,
				 EndermenConfig.endermen.wandering.spawnRates.min,
				 EndermenConfig.endermen.wandering.spawnRates.max,
				 getBiomes(EndermenConfig.endermen.wandering.spawnRates.biomes))
				.build();
		event.getRegistry().register(wandering);

		//Gardener enderman
		EntityEntry gardener = EntityEntryBuilder.create().entity(EntityGardenerEnderman.class)
				.id(Reference.MobultionEntities.GARDENERENDERMAN.getRegistryName(), id++)
				.name(Reference.MobultionEntities.GARDENERENDERMAN.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x1DE11D, 0xF97AD9)
				 .spawn(EnumCreatureType.MONSTER,
				 EndermenConfig.endermen.gardener.spawnRates.weight,
				 EndermenConfig.endermen.gardener.spawnRates.min,
				 EndermenConfig.endermen.gardener.spawnRates.max,
				 getBiomes(EndermenConfig.endermen.gardener.spawnRates.biomes))
				.build();
		event.getRegistry().register(gardener);
		
		//Glass enderman
		EntityEntry glass = EntityEntryBuilder.create().entity(EntityGlassEnderman.class)
				.id(Reference.MobultionEntities.GLASSENDERMAN.getRegistryName(), id++)
				.name(Reference.MobultionEntities.GLASSENDERMAN.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x2D2C2F, 0x535056)
				 .spawn(EnumCreatureType.MONSTER,
				 EndermenConfig.endermen.glass.spawnRates.weight,
				 EndermenConfig.endermen.glass.spawnRates.min,
				 EndermenConfig.endermen.glass.spawnRates.max,
				 getBiomes(EndermenConfig.endermen.glass.spawnRates.biomes))
				.build();
		event.getRegistry().register(glass);
		
		// Mites
		EntityEntry woodmite = EntityEntryBuilder.create().entity(EntityWoodMite.class)
				.id(Reference.MobultionEntities.WOODMITE.getRegistryName(), id++)
				.name(Reference.MobultionEntities.WOODMITE.getUnlocalizedName()).tracker(64, 3, true)
				.egg(0x996600, 0x705D21).build();
		event.getRegistry().register(woodmite);

		EntityEntry enderProjectile = EntityEntryBuilder.create().entity(EntityEnderProjectile.class)
				.id(Reference.MobultionEntities.ENDERPROJECTILE.getRegistryName(), id++)
				.name(Reference.MobultionEntities.ENDERPROJECTILE.getUnlocalizedName()).tracker(64, 3, true).build();
		event.getRegistry().register(enderProjectile);
		
		EntityEntry glassShot = EntityEntryBuilder.create().entity(EntityGlassShot.class)
				.id(Reference.MobultionEntities.GLASSSHOT.getRegistryName(), id++)
				.name(Reference.MobultionEntities.GLASSSHOT.getUnlocalizedName()).tracker(64, 3, true).build();
		event.getRegistry().register(glassShot);

	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		RenderingRegistry.registerEntityRenderingHandler(EntityMagmaSpider.class, new RenderMagmaSpider.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpeedySpider.class, new RenderSpeedySpider.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityHypnoSpider.class, new RenderHypnoSpider.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityHypnoBall.class, new RenderHypnoBall.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityWitherSpider.class, new RenderWitherSpider.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityMiniSpider.class, new RenderMiniSpider.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntitySorcererSpider.class,
				new RenderSorcererSpider.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityAngelSpider.class, new RenderAngelSpider.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityMotherSpider.class, new RenderMotherSpider.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpiderEgg.class, new RenderSpiderEgg.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityWitheringSkeleton.class,
				new RenderWitheringSkeleton.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonRemains.class,
				new RenderSkeletonRemains.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityMagmaArrow.class, new RenderMagmaArrow.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityShamanSkeleton.class,
				new RenderShamanSkeleton.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityJokerSkeleton.class, new RenderJokerSkeleton.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityHeartArrow.class, new RenderHeartArrow.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntitySniperSkeleton.class,
				new RenderSniperSkeleton.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityMagmaSkeleton.class, new RenderMagmaSkeleton.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityCorruptedSkeleton.class,
				new RenderCorruptedSkeleton.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityKnightZombie.class, new RenderKnightZombie.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityWorkerZombie.class, new RenderWorkerZombie.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityMagmaZombie.class, new RenderMagmaZombie.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityDoctorZombie.class, new RenderDoctorZombie.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityGoroZombie.class, new RenderGoroZombie.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityRavenousZombie.class,
				new RenderRavenousZombie.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityVampireSkeleton.class,
				new RenderVampireSkeleton.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityMagmaEnderman.class, new RenderMagmaEnderman.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityIceEnderman.class, new RenderIceEnderman.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityWoodMite.class, new RenderWoodMite.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityEnderProjectile.class, new RenderEnderProjectile.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityWanderingEnderman.class,
				new RenderWanderingEnderman.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityGardenerEnderman.class,
				new RenderGardenerEnderman.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityGlassEnderman.class,
				new RenderGlassEnderman.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityGlassShot.class,
				new RenderGlassShot.Factory());

	}
}
