package mcpecommander.mobultion.init;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
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
import mcpecommander.mobultion.mobConfigs.SkeletonsConfig;
import mcpecommander.mobultion.mobConfigs.SpidersConfig;
import mcpecommander.mobultion.mobConfigs.ZombiesConfig;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModEntities {
	private static Biome[] getBiomes(String[] string) {
		List<Biome> list = new ArrayList();
		Biome[] biome = {};
		if (string == null) {
			return null;
		}
		if (string[0].equals("all")) {
			for (int i = 1; i <= 39; i++) {
				list.add(Biome.getBiome(i));
			}
			// if(string[1] != null && string[1].equals("m")){
			// for(int i = 129; i <= 167; i++){
			// list.add(Biome.getBiome(i));
			// }
			// }
			return list.toArray(biome);
		}
		for (String id : string) {

			Biome add = Biome.REGISTRY.getObject(new ResourceLocation(id));
			// System.out.println(id + " " + add);
			if (add != null) {
				list.add(add);
			} else {
				MobultionMod.logger.log(Level.ERROR,
						"NPE, The id " + id + " is probably misswritten, PS:The biomes do not support non-vanilla yet");
			}
		}
		if (!list.isEmpty()) {
			return list.toArray(biome);
		}

		return biome;

	}

	@SubscribeEvent
	public static void init(RegistryEvent.Register<EntityEntry> event) {
		// MagmaSpiderInit
		if (SpidersConfig.spiders.magma.spawn) {
			EntityEntry magma = EntityEntryBuilder.create().entity(EntityMagmaSpider.class)
					.id(Reference.MobultionEntities.MAGMASPIDER.getRegistryName(), 1)
					.name(Reference.MobultionEntities.MAGMASPIDER.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x230E0E, 0xF01414)
					.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.magma.spawnRates.weight,
							SpidersConfig.spiders.magma.spawnRates.min, SpidersConfig.spiders.magma.spawnRates.max,
							getBiomes(SpidersConfig.spiders.magma.spawnRates.biomes))
					.build();
			event.getRegistry().register(magma);
		}

		// SpeedySpiderInit
		if (SpidersConfig.spiders.speedy.spawn) {
			EntityEntry speedy = EntityEntryBuilder.create().entity(EntitySpeedySpider.class)
					.id(Reference.MobultionEntities.SPEEDYSPIDER.getRegistryName(), 2)
					.name(Reference.MobultionEntities.SPEEDYSPIDER.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x0C0CDB, 0x6666D0)
					.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.speedy.spawnRates.weight,
							SpidersConfig.spiders.speedy.spawnRates.min, SpidersConfig.spiders.speedy.spawnRates.max,
							getBiomes(SpidersConfig.spiders.speedy.spawnRates.biomes))
					.build();
			event.getRegistry().register(speedy);
		}

		// WitherSpiderInit
		if (SpidersConfig.spiders.wither.spawn) {
			EntityEntry wither = EntityEntryBuilder.create().entity(EntityWitherSpider.class)
					.id(Reference.MobultionEntities.WITHERSPIDER.getRegistryName(), 3)
					.name(Reference.MobultionEntities.WITHERSPIDER.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x666666, 0x444444)
					.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.wither.spawnRates.weight,
							SpidersConfig.spiders.wither.spawnRates.min, SpidersConfig.spiders.wither.spawnRates.max,
							getBiomes(SpidersConfig.spiders.wither.spawnRates.biomes))
					.build();
			event.getRegistry().register(wither);
		}

		// MiniSpiderInit
		if (SpidersConfig.spiders.mini.spawn) {
			EntityEntry mini = EntityEntryBuilder.create().entity(EntityMiniSpider.class)
					.id(Reference.MobultionEntities.MINISPIDER.getRegistryName(), 4)
					.name(Reference.MobultionEntities.MINISPIDER.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0xF3F3F3, 0xF4CCCC).build();
			event.getRegistry().register(mini);
		}

		// SorcererSpiderInit
		if (SpidersConfig.spiders.sorcerer.spawn) {
			EntityEntry sorcerer = EntityEntryBuilder.create().entity(EntitySorcererSpider.class)
					.id(Reference.MobultionEntities.SORCERERSPIDER.getRegistryName(), 5)
					.name(Reference.MobultionEntities.SORCERERSPIDER.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x6AA84F, 0x12436F)
					.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.sorcerer.spawnRates.weight,
							SpidersConfig.spiders.sorcerer.spawnRates.min,
							SpidersConfig.spiders.sorcerer.spawnRates.max,
							getBiomes(SpidersConfig.spiders.sorcerer.spawnRates.biomes))
					.build();
			event.getRegistry().register(sorcerer);
		}

		// AngelSpiderInit
		if (SpidersConfig.spiders.angel.spawn) {
			EntityEntry angel = EntityEntryBuilder.create().entity(EntityAngelSpider.class)
					.id(Reference.MobultionEntities.ANGELSPIDER.getRegistryName(), 6)
					.name(Reference.MobultionEntities.ANGELSPIDER.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0xFFFFFF, 0xFFFF53)
					.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.angel.spawnRates.weight,
							SpidersConfig.spiders.angel.spawnRates.min, SpidersConfig.spiders.angel.spawnRates.max,
							getBiomes(SpidersConfig.spiders.angel.spawnRates.biomes))
					.build();
			event.getRegistry().register(angel);
		}

		// MotherSpiderInit and its egg
		if (SpidersConfig.spiders.mother.spawn) {
			EntityEntry mother = EntityEntryBuilder.create().entity(EntityMotherSpider.class)
					.id(Reference.MobultionEntities.MOTHERSPIDER.getRegistryName(), 7)
					.name(Reference.MobultionEntities.MOTHERSPIDER.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x444444, 0x9D8888)
					.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.mother.spawnRates.weight,
							SpidersConfig.spiders.mother.spawnRates.min, SpidersConfig.spiders.mother.spawnRates.max,
							getBiomes(SpidersConfig.spiders.mother.spawnRates.biomes))
					.build();
			EntityEntry egg = EntityEntryBuilder.create().entity(EntitySpiderEgg.class)
					.id(Reference.MobultionEntities.SPIDEREGG.getRegistryName(), 8)
					.name(Reference.MobultionEntities.SPIDEREGG.getUnlocalizedName()).tracker(64, 3, true).build();
			event.getRegistry().registerAll(mother, egg);
		}

		// HypnoSpiderInit and his ball
		if (SpidersConfig.spiders.hypno.spawn) {
			EntityEntry hypno = EntityEntryBuilder.create().entity(EntityHypnoSpider.class)
					.id(Reference.MobultionEntities.HYPNOSPIDER.getRegistryName(), 9)
					.name(Reference.MobultionEntities.HYPNOSPIDER.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0xDD06DD, 0xF736F7)
					.spawn(EnumCreatureType.MONSTER, SpidersConfig.spiders.hypno.spawnRates.weight,
							SpidersConfig.spiders.hypno.spawnRates.min, SpidersConfig.spiders.hypno.spawnRates.max,
							getBiomes(SpidersConfig.spiders.hypno.spawnRates.biomes))
					.build();
			EntityEntry egg = EntityEntryBuilder.create().entity(EntityHypnoBall.class)
					.id(Reference.MobultionEntities.HYPNOBALL.getRegistryName(), 10)
					.name(Reference.MobultionEntities.HYPNOBALL.getUnlocalizedName()).tracker(64, 3, true).build();
			event.getRegistry().registerAll(hypno, egg);
		}

		// SkeletonRemains
		EntityEntry egg = EntityEntryBuilder.create().entity(EntitySkeletonRemains.class)
				.id(Reference.MobultionEntities.SKELETONREMAINS.getRegistryName(), 11)
				.name(Reference.MobultionEntities.SKELETONREMAINS.getUnlocalizedName()).tracker(64, 3, true).build();
		event.getRegistry().register(egg);

		// WitheringSkeletonInit
		if (SkeletonsConfig.skeletons.withering.spawn) {
			EntityEntry withering = EntityEntryBuilder.create().entity(EntityWitheringSkeleton.class)
					.id(Reference.MobultionEntities.WITHERINGSKELETON.getRegistryName(), 12)
					.name(Reference.MobultionEntities.WITHERINGSKELETON.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x5C5151, 0xCCCCCC)
					.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.withering.spawnRates.weight,
							SkeletonsConfig.skeletons.withering.spawnRates.min,
							SkeletonsConfig.skeletons.withering.spawnRates.max,
							getBiomes(SkeletonsConfig.skeletons.withering.spawnRates.biomes))
					.build();
			event.getRegistry().register(withering);
		}

		// ShamanSkeletonInit
		if (SkeletonsConfig.skeletons.shaman.spawn) {
			EntityEntry joker = EntityEntryBuilder.create().entity(EntityShamanSkeleton.class)
					.id(Reference.MobultionEntities.SHAMANSKELETON.getRegistryName(), 13)
					.name(Reference.MobultionEntities.SHAMANSKELETON.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x050572, 0x741B47)
					.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.shaman.spawnRates.weight,
							SkeletonsConfig.skeletons.shaman.spawnRates.min,
							SkeletonsConfig.skeletons.shaman.spawnRates.max,
							getBiomes(SkeletonsConfig.skeletons.shaman.spawnRates.biomes))
					.build();
			event.getRegistry().register(joker);
		}

		// JokerSkeleton and its Arrow Init
		if (SkeletonsConfig.skeletons.joker.spawn) {
			EntityEntry joker = EntityEntryBuilder.create().entity(EntityJokerSkeleton.class)
					.id(Reference.MobultionEntities.JOKERSKELETON.getRegistryName(), 14)
					.name(Reference.MobultionEntities.JOKERSKELETON.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0xFF0000, 0xFFFF00)
					.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.joker.spawnRates.weight,
							SkeletonsConfig.skeletons.joker.spawnRates.min,
							SkeletonsConfig.skeletons.joker.spawnRates.max,
							getBiomes(SkeletonsConfig.skeletons.joker.spawnRates.biomes))
					.build();
			event.getRegistry().register(joker);
		}
		EntityEntry heartArrow = EntityEntryBuilder.create().entity(EntityJokerSkeleton.class)
				.id(Reference.MobultionEntities.HEARTARROW.getRegistryName(), 15)
				.name(Reference.MobultionEntities.HEARTARROW.getUnlocalizedName()).tracker(64, 3, true).build();
		event.getRegistry().register(heartArrow);

		// SniperSkeleton
		if (SkeletonsConfig.skeletons.sniper.spawn) {
			EntityEntry sniper = EntityEntryBuilder.create().entity(EntitySniperSkeleton.class)
					.id(Reference.MobultionEntities.SNIPERSKELETON.getRegistryName(), 16)
					.name(Reference.MobultionEntities.SNIPERSKELETON.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x38761D, 0x93C47D)
					.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.sniper.spawnRates.weight,
							SkeletonsConfig.skeletons.sniper.spawnRates.min,
							SkeletonsConfig.skeletons.sniper.spawnRates.max,
							getBiomes(SkeletonsConfig.skeletons.sniper.spawnRates.biomes))
					.build();
			event.getRegistry().register(sniper);
		}

		// MagmaSkeleton
		if (SkeletonsConfig.skeletons.magma.spawn) {
			EntityEntry magma = EntityEntryBuilder.create().entity(EntityMagmaSkeleton.class)
					.id(Reference.MobultionEntities.MAGMASKELETON.getRegistryName(), 17)
					.name(Reference.MobultionEntities.MAGMASKELETON.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x811616, 0xFD1D1D)
					.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.magma.spawnRates.weight,
							SkeletonsConfig.skeletons.magma.spawnRates.min,
							SkeletonsConfig.skeletons.magma.spawnRates.max,
							getBiomes(SkeletonsConfig.skeletons.magma.spawnRates.biomes))
					.build();
			event.getRegistry().register(magma);
		}
		// Magmaarrow
		EntityEntry magmaArrow = EntityEntryBuilder.create().entity(EntityMagmaArrow.class)
				.id(Reference.MobultionEntities.MAGMAARROW.getRegistryName(), 18)
				.name(Reference.MobultionEntities.MAGMAARROW.getUnlocalizedName()).tracker(64, 3, true).build();
		event.getRegistry().register(magmaArrow);

		// CorruptedSkeleton
		if (SkeletonsConfig.skeletons.corrupted.spawn) {
			EntityEntry corrupted = EntityEntryBuilder.create().entity(EntityCorruptedSkeleton.class)
					.id(Reference.MobultionEntities.CORRUPTEDSKELETON.getRegistryName(), 19)
					.name(Reference.MobultionEntities.CORRUPTEDSKELETON.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x745F1D, 0x927006)
					.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.corrupted.spawnRates.weight,
							SkeletonsConfig.skeletons.corrupted.spawnRates.min,
							SkeletonsConfig.skeletons.corrupted.spawnRates.max,
							getBiomes(SkeletonsConfig.skeletons.corrupted.spawnRates.biomes))
					.build();
			event.getRegistry().register(corrupted);
		}

		// KnightZombie
		if (ZombiesConfig.zombies.knight.spawn) {
			EntityEntry knight = EntityEntryBuilder.create().entity(EntityKnightZombie.class)
					.id(Reference.MobultionEntities.KNIGHTZOMBIE.getRegistryName(), 20)
					.name(Reference.MobultionEntities.KNIGHTZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0xEEEEEE, 0xD0E0E3)
					.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.knight.spawnRates.weight,
							ZombiesConfig.zombies.knight.spawnRates.min, ZombiesConfig.zombies.knight.spawnRates.max,
							getBiomes(ZombiesConfig.zombies.knight.spawnRates.biomes))
					.build();
			event.getRegistry().register(knight);
		}

		// WorkerZombie
		if (ZombiesConfig.zombies.worker.spawn) {
			EntityEntry worker = EntityEntryBuilder.create().entity(EntityWorkerZombie.class)
					.id(Reference.MobultionEntities.WORKERZOMBIE.getRegistryName(), 21)
					.name(Reference.MobultionEntities.WORKERZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0xFFE599, 0xFFFF00)
					.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.worker.spawnRates.weight,
							ZombiesConfig.zombies.worker.spawnRates.min, ZombiesConfig.zombies.worker.spawnRates.max,
							getBiomes(ZombiesConfig.zombies.worker.spawnRates.biomes))
					.build();
			event.getRegistry().register(worker);
		}

		// MagmaZombie
		if (ZombiesConfig.zombies.magma.spawn) {
			EntityEntry magma = EntityEntryBuilder.create().entity(EntityMagmaZombie.class)
					.id(Reference.MobultionEntities.MAGMAZOMBIE.getRegistryName(), 22)
					.name(Reference.MobultionEntities.MAGMAZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0xFFF144, 0xCC0000)
					.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.magma.spawnRates.weight,
							ZombiesConfig.zombies.magma.spawnRates.min, ZombiesConfig.zombies.magma.spawnRates.max,
							getBiomes(ZombiesConfig.zombies.magma.spawnRates.biomes))
					.build();
			event.getRegistry().register(magma);
		}

		// DoctorZombie
		if (ZombiesConfig.zombies.doctor.spawn) {
			EntityEntry doctor = EntityEntryBuilder.create().entity(EntityDoctorZombie.class)
					.id(Reference.MobultionEntities.DOCTORZOMBIE.getRegistryName(), 23)
					.name(Reference.MobultionEntities.DOCTORZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0xFFFFFF, 0xFD1D1D)
					.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.doctor.spawnRates.weight,
							ZombiesConfig.zombies.doctor.spawnRates.min, ZombiesConfig.zombies.doctor.spawnRates.max,
							getBiomes(ZombiesConfig.zombies.doctor.spawnRates.biomes))
					.build();
			event.getRegistry().register(doctor);
		}

		// GoroZombie
		if (ZombiesConfig.zombies.goro.spawn) {
			EntityEntry goro = EntityEntryBuilder.create().entity(EntityGoroZombie.class)
					.id(Reference.MobultionEntities.GOROZOMBIE.getRegistryName(), 24)
					.name(Reference.MobultionEntities.GOROZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x95BD84, 0xCEA937)
					.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.goro.spawnRates.weight,
							ZombiesConfig.zombies.goro.spawnRates.min, ZombiesConfig.zombies.goro.spawnRates.max,
							getBiomes(ZombiesConfig.zombies.goro.spawnRates.biomes))
					.build();
			event.getRegistry().register(goro);
		}

		// RavenousZombie
		if (ZombiesConfig.zombies.ravenous.spawn) {
			EntityEntry ravenous = EntityEntryBuilder.create().entity(EntityRavenousZombie.class)
					.id(Reference.MobultionEntities.RAVENOUSZOMBIE.getRegistryName(), 25)
					.name(Reference.MobultionEntities.RAVENOUSZOMBIE.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0x6AA84F, 0xD41A1A)
					.spawn(EnumCreatureType.MONSTER, ZombiesConfig.zombies.ravenous.spawnRates.weight,
							ZombiesConfig.zombies.ravenous.spawnRates.min, ZombiesConfig.zombies.ravenous.spawnRates.max,
							getBiomes(ZombiesConfig.zombies.ravenous.spawnRates.biomes))
					.build();
			event.getRegistry().register(ravenous);
		}
		
		// VampireSkeleton
		if (SkeletonsConfig.skeletons.vampire.spawn) {
			EntityEntry vampire = EntityEntryBuilder.create().entity(EntityVampireSkeleton.class)
					.id(Reference.MobultionEntities.VAMPIRESKELETON.getRegistryName(), 26)
					.name(Reference.MobultionEntities.VAMPIRESKELETON.getUnlocalizedName()).tracker(64, 3, true)
					.egg(0xBB8A8A, 0x540D0D)
					.spawn(EnumCreatureType.MONSTER, SkeletonsConfig.skeletons.vampire.spawnRates.weight,
							SkeletonsConfig.skeletons.vampire.spawnRates.min,
							SkeletonsConfig.skeletons.vampire.spawnRates.max,
							getBiomes(SkeletonsConfig.skeletons.vampire.spawnRates.biomes))
					.build();
			event.getRegistry().register(vampire);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		if (SpidersConfig.spiders.magma.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityMagmaSpider.class, new RenderMagmaSpider.Factory());
		if (SpidersConfig.spiders.speedy.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntitySpeedySpider.class,
					new RenderSpeedySpider.Factory());
		if (SpidersConfig.spiders.hypno.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityHypnoSpider.class, new RenderHypnoSpider.Factory());
		if (SpidersConfig.spiders.hypno.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityHypnoBall.class, new RenderHypnoBall.Factory());
		if (SpidersConfig.spiders.wither.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityWitherSpider.class,
					new RenderWitherSpider.Factory());
		if (SpidersConfig.spiders.mini.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityMiniSpider.class, new RenderMiniSpider.Factory());
		if (SpidersConfig.spiders.sorcerer.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntitySorcererSpider.class,
					new RenderSorcererSpider.Factory());
		if (SpidersConfig.spiders.angel.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityAngelSpider.class, new RenderAngelSpider.Factory());
		if (SpidersConfig.spiders.mother.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityMotherSpider.class,
					new RenderMotherSpider.Factory());
		if (SpidersConfig.spiders.mother.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntitySpiderEgg.class, new RenderSpiderEgg.Factory());
		if (SkeletonsConfig.skeletons.withering.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityWitheringSkeleton.class,
					new RenderWitheringSkeleton.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonRemains.class,
				new RenderSkeletonRemains.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityMagmaArrow.class, new RenderMagmaArrow.Factory());
		if (SkeletonsConfig.skeletons.shaman.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityShamanSkeleton.class,
					new RenderShamanSkeleton.Factory());
		if (SkeletonsConfig.skeletons.joker.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityJokerSkeleton.class,
					new RenderJokerSkeleton.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityHeartArrow.class, new RenderHeartArrow.Factory());
		if (SkeletonsConfig.skeletons.sniper.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntitySniperSkeleton.class,
					new RenderSniperSkeleton.Factory());
		if (SkeletonsConfig.skeletons.magma.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityMagmaSkeleton.class,
					new RenderMagmaSkeleton.Factory());
		if (SkeletonsConfig.skeletons.corrupted.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityCorruptedSkeleton.class,
					new RenderCorruptedSkeleton.Factory());
		if (ZombiesConfig.zombies.knight.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityKnightZombie.class,
					new RenderKnightZombie.Factory());
		if (ZombiesConfig.zombies.worker.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityWorkerZombie.class,
					new RenderWorkerZombie.Factory());
		if (ZombiesConfig.zombies.magma.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityMagmaZombie.class, new RenderMagmaZombie.Factory());
		if (ZombiesConfig.zombies.doctor.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityDoctorZombie.class,
					new RenderDoctorZombie.Factory());
		if (ZombiesConfig.zombies.goro.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityGoroZombie.class, new RenderGoroZombie.Factory());
		if (ZombiesConfig.zombies.ravenous.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityRavenousZombie.class,
					new RenderRavenousZombie.Factory());
		if (SkeletonsConfig.skeletons.vampire.spawn)
			RenderingRegistry.registerEntityRenderingHandler(EntityVampireSkeleton.class,
					new RenderVampireSkeleton.Factory());
	}
}
