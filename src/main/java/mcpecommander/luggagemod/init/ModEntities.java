package mcpecommander.luggagemod.init;

import mcpecommander.luggagemod.LuggageMod;
import mcpecommander.luggagemod.MobsConfig;
import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.entity.entities.skeletons.EntityCorruptedSkeleton;
import mcpecommander.luggagemod.entity.entities.skeletons.EntityHeartArrow;
import mcpecommander.luggagemod.entity.entities.skeletons.EntityJokerSkeleton;
import mcpecommander.luggagemod.entity.entities.skeletons.EntityMagmaArrow;
import mcpecommander.luggagemod.entity.entities.skeletons.EntityMagmaSkeleton;
import mcpecommander.luggagemod.entity.entities.skeletons.EntityShamanSkeleton;
import mcpecommander.luggagemod.entity.entities.skeletons.EntitySkeletonRemains;
import mcpecommander.luggagemod.entity.entities.skeletons.EntitySniperSkeleton;
import mcpecommander.luggagemod.entity.entities.skeletons.EntityWitheringSkeleton;
import mcpecommander.luggagemod.entity.entities.spiders.EntityAngelSpider;
import mcpecommander.luggagemod.entity.entities.spiders.EntityHypnoBall;
import mcpecommander.luggagemod.entity.entities.spiders.EntityHypnoSpider;
import mcpecommander.luggagemod.entity.entities.spiders.EntityMagmaSpider;
import mcpecommander.luggagemod.entity.entities.spiders.EntityMiniSpider;
import mcpecommander.luggagemod.entity.entities.spiders.EntityMotherSpider;
import mcpecommander.luggagemod.entity.entities.spiders.EntitySorcererSpider;
import mcpecommander.luggagemod.entity.entities.spiders.EntitySpeedySpider;
import mcpecommander.luggagemod.entity.entities.spiders.EntitySpiderEgg;
import mcpecommander.luggagemod.entity.entities.spiders.EntityWitherSpider;
import mcpecommander.luggagemod.entity.entities.zombies.EntityDoctorZombie;
import mcpecommander.luggagemod.entity.entities.zombies.EntityGoroZombie;
import mcpecommander.luggagemod.entity.entities.zombies.EntityKnightZombie;
import mcpecommander.luggagemod.entity.entities.zombies.EntityMagmaZombie;
import mcpecommander.luggagemod.entity.entities.zombies.EntityWorkerZombie;
import mcpecommander.luggagemod.entity.renderer.skeletonsRenderer.RenderCorruptedSkeleton;
import mcpecommander.luggagemod.entity.renderer.skeletonsRenderer.RenderHeartArrow;
import mcpecommander.luggagemod.entity.renderer.skeletonsRenderer.RenderJokerSkeleton;
import mcpecommander.luggagemod.entity.renderer.skeletonsRenderer.RenderMagmaArrow;
import mcpecommander.luggagemod.entity.renderer.skeletonsRenderer.RenderMagmaSkeleton;
import mcpecommander.luggagemod.entity.renderer.skeletonsRenderer.RenderShamanSkeleton;
import mcpecommander.luggagemod.entity.renderer.skeletonsRenderer.RenderSkeletonRemains;
import mcpecommander.luggagemod.entity.renderer.skeletonsRenderer.RenderSniperSkeleton;
import mcpecommander.luggagemod.entity.renderer.skeletonsRenderer.RenderWitheringSkeleton;
import mcpecommander.luggagemod.entity.renderer.spidersRenderer.RenderAngelSpider;
import mcpecommander.luggagemod.entity.renderer.spidersRenderer.RenderHypnoBall;
import mcpecommander.luggagemod.entity.renderer.spidersRenderer.RenderHypnoSpider;
import mcpecommander.luggagemod.entity.renderer.spidersRenderer.RenderMagmaSpider;
import mcpecommander.luggagemod.entity.renderer.spidersRenderer.RenderMiniSpider;
import mcpecommander.luggagemod.entity.renderer.spidersRenderer.RenderMotherSpider;
import mcpecommander.luggagemod.entity.renderer.spidersRenderer.RenderSorcererSpider;
import mcpecommander.luggagemod.entity.renderer.spidersRenderer.RenderSpeedySpider;
import mcpecommander.luggagemod.entity.renderer.spidersRenderer.RenderSpiderEgg;
import mcpecommander.luggagemod.entity.renderer.spidersRenderer.RenderWitherSpider;
import mcpecommander.luggagemod.entity.renderer.zombiesRenderer.RenderDoctorZombie;
import mcpecommander.luggagemod.entity.renderer.zombiesRenderer.RenderGoroZombie;
import mcpecommander.luggagemod.entity.renderer.zombiesRenderer.RenderKnightZombie;
import mcpecommander.luggagemod.entity.renderer.zombiesRenderer.RenderMagmaZombie;
import mcpecommander.luggagemod.entity.renderer.zombiesRenderer.RenderWorkerZombie;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntities {
	
	public static void init() {
        int id = 1;
        //MagmaSpiderInit
        ResourceLocation magmaSpider = new ResourceLocation(Reference.MOD_ID, "magma_spider");
        EntityRegistry.registerModEntity(magmaSpider, EntityMagmaSpider.class, "MagmaSpider", id++, LuggageMod.instance, 64, 3, true, 0x230E0E, 0xF01414);
        EntityRegistry.addSpawn(EntityMagmaSpider.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.MUSHROOM_ISLAND, Biomes.DESERT);
        
        //SpeedySpiderInit
        ResourceLocation speedySpider = new ResourceLocation(Reference.MOD_ID, "speedy_spider");
        EntityRegistry.registerModEntity(speedySpider, EntitySpeedySpider.class, "SpeedySpider", id++, LuggageMod.instance, 64, 3, true, 0x235E0E, 0xF01414);
        EntityRegistry.addSpawn(EntitySpeedySpider.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.BEACH, Biomes.ICE_PLAINS, Biomes.PLAINS);
        
        //WitherSpiderInit
        ResourceLocation witherSpider = new ResourceLocation(Reference.MOD_ID, "wither_spider");
        EntityRegistry.registerModEntity(witherSpider, EntityWitherSpider.class, "witherSpider", id++, LuggageMod.instance, 64, 3, true, 0x235E6E, 0xF06414);
        EntityRegistry.addSpawn(EntityWitherSpider.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //MiniSpiderInit
        ResourceLocation miniSpider = new ResourceLocation(Reference.MOD_ID, "mini_spider");
        EntityRegistry.registerModEntity(miniSpider, EntityMiniSpider.class, "miniSpider", id++, LuggageMod.instance, 64, 3, true, 0x235E6E, 0xF06414);
        EntityRegistry.addSpawn(EntityMiniSpider.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //SorcererSpiderInit
        ResourceLocation sorcererSpider = new ResourceLocation(Reference.MOD_ID, "sorcerer_spider");
        EntityRegistry.registerModEntity(sorcererSpider, EntitySorcererSpider.class, "sorcererSpider", id++, LuggageMod.instance, 64, 3, true, 0x235E6E, 0xF06414);
        EntityRegistry.addSpawn(EntitySorcererSpider.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //AngelSpiderInit
        if(MobsConfig.spiders.angel.spawn){
        	ResourceLocation angelSpider = new ResourceLocation(Reference.MOD_ID, "angel_spider");
        	EntityRegistry.registerModEntity(angelSpider, EntityAngelSpider.class, "angelSpider", id++, LuggageMod.instance, 64, 3, true, 0x235E6E, 0xF06414);
        	EntityRegistry.addSpawn(EntityAngelSpider.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        }
        
        //MotherSpiderInit and its egg
        ResourceLocation motherSpider = new ResourceLocation(Reference.MOD_ID, "mother_spider");
        ResourceLocation spiderEgg = new ResourceLocation(Reference.MOD_ID, "spider_egg");
        EntityRegistry.registerModEntity(motherSpider, EntityMotherSpider.class, "motherSpider", id++, LuggageMod.instance, 64, 3, true, 0x432E6E, 0xF06414);
        EntityRegistry.addSpawn(EntityMotherSpider.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        EntityRegistry.registerModEntity(spiderEgg, EntitySpiderEgg.class, "spiderEgg", id++, LuggageMod.instance, 64, 3, true);
        
        //TricksterSpiderInit
//        ResourceLocation tricksterSpider = new ResourceLocation(Reference.MOD_ID, "trickster_spider");
//        EntityRegistry.registerModEntity(tricksterSpider, EntityTricksterSpider.class, "tricksterSpider", id++, LuggageMod.instance, 64, 3, true, 0x005E6E, 0xF06414);
//        EntityRegistry.addSpawn(EntityTricksterSpider.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //HypnoSpiderInit and his ball
        ResourceLocation HypnoSpider = new ResourceLocation(Reference.MOD_ID, "hypno_spider");
        ResourceLocation HypnoBall = new ResourceLocation(Reference.MOD_ID, "hypno_ball");
        EntityRegistry.registerModEntity(HypnoSpider, EntityHypnoSpider.class, "HypnoSpider", id++, LuggageMod.instance, 64, 3, true, 0x030E0E, 0xF01414);
        EntityRegistry.addSpawn(EntitySpeedySpider.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.SWAMPLAND, Biomes.MUTATED_SWAMPLAND);
        EntityRegistry.registerModEntity(HypnoBall, EntityHypnoBall.class, "hypnoBall", id++, LuggageMod.instance, 64, 3, true);
        
        //SkeletonRemains
        ResourceLocation skeletonRemains = new ResourceLocation(Reference.MOD_ID, "skeleton_remains");
        EntityRegistry.registerModEntity(skeletonRemains, EntitySkeletonRemains.class, "skeletonRemains", id++, LuggageMod.instance, 64, 3, true);
        
        //WitheringSkeletonInit
        ResourceLocation witheringSkeleton = new ResourceLocation(Reference.MOD_ID, "withering_skeleton");
        EntityRegistry.registerModEntity(witheringSkeleton, EntityWitheringSkeleton.class, "witheringSkeleton", id++, LuggageMod.instance, 64, 3, true, 0x235E6E, 0xF06414);
        EntityRegistry.addSpawn(EntityWitheringSkeleton.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //ShamanSkeletonInit 
        ResourceLocation shamanSkeleton = new ResourceLocation(Reference.MOD_ID, "shaman_skeleton");
        EntityRegistry.registerModEntity(shamanSkeleton, EntityShamanSkeleton.class, "shamanSkeleton", id++, LuggageMod.instance, 64, 3, true, 0x235E6E, 0xF06414);
        EntityRegistry.addSpawn(EntityShamanSkeleton.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //JokerSkeleton and its Arrow Init
        ResourceLocation jokerSkeleton = new ResourceLocation(Reference.MOD_ID, "joker_skeleton");
        ResourceLocation heartArrow = new ResourceLocation(Reference.MOD_ID, "heart_arrow");
        EntityRegistry.registerModEntity(jokerSkeleton, EntityJokerSkeleton.class, "jokerSkeleton", id++, LuggageMod.instance, 64, 3, true, 0x235E6E, 0xF06414);
        EntityRegistry.addSpawn(EntityJokerSkeleton.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        EntityRegistry.registerModEntity(heartArrow, EntityHeartArrow.class, "heartArrow", id++, LuggageMod.instance, 64, 3, true);
        
        //SniperSkeleton
        ResourceLocation sniperSkeleton = new ResourceLocation(Reference.MOD_ID, "sniper_skeleton");
        EntityRegistry.registerModEntity(sniperSkeleton, EntitySniperSkeleton.class, "sniperSkeleton", id++, LuggageMod.instance, 64, 3, true, 0x234324, 0x234324);
        EntityRegistry.addSpawn(EntitySniperSkeleton.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //MagmaSkeleton
        ResourceLocation magmaSkeleton = new ResourceLocation(Reference.MOD_ID, "magma_skeleton");
        EntityRegistry.registerModEntity(magmaSkeleton, EntityMagmaSkeleton.class, "magmaSkeleton", id++, LuggageMod.instance, 64, 3, true, 0x234324, 0x234324);
        EntityRegistry.addSpawn(EntityMagmaSkeleton.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        //Magmaarrow
        ResourceLocation magmaArrow = new ResourceLocation(Reference.MOD_ID, "magma_arrow");
        EntityRegistry.registerModEntity(magmaArrow, EntityMagmaArrow.class, "magmaArrow", id++, LuggageMod.instance, 64, 3, true);
        
        //SniperSkeleton
        ResourceLocation corruptedSkeleton = new ResourceLocation(Reference.MOD_ID, "corrupted_skeleton");
        EntityRegistry.registerModEntity(corruptedSkeleton, EntityCorruptedSkeleton.class, "corruptedSkeleton", id++, LuggageMod.instance, 64, 3, true, 0x234324, 0x234324);
        EntityRegistry.addSpawn(EntityCorruptedSkeleton.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //KnightZombie
        ResourceLocation knightZombie= new ResourceLocation(Reference.MOD_ID, "knight_zombie");
        EntityRegistry.registerModEntity(knightZombie, EntityKnightZombie.class, "knightZombie", id++, LuggageMod.instance, 64, 3, true, 0xFFFFF4, 0x234324);
        EntityRegistry.addSpawn(EntityKnightZombie.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //WorkerZombie
        ResourceLocation workerZombie= new ResourceLocation(Reference.MOD_ID, "worker_zombie");
        EntityRegistry.registerModEntity(workerZombie, EntityWorkerZombie.class, "workerZombie", id++, LuggageMod.instance, 64, 3, true, 0xCCFF00, 0x336600);
        EntityRegistry.addSpawn(EntityWorkerZombie.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //MagmaZombie
        ResourceLocation magmaZombie= new ResourceLocation(Reference.MOD_ID, "magma_zombie");
        EntityRegistry.registerModEntity(magmaZombie, EntityMagmaZombie.class, "magmaZombie", id++, LuggageMod.instance, 64, 3, true, 0xFF0303, 0xF9F919);
        EntityRegistry.addSpawn(EntityMagmaZombie.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //DoctorZombie
        ResourceLocation doctorZombie= new ResourceLocation(Reference.MOD_ID, "doctor_zombie");
        EntityRegistry.registerModEntity(doctorZombie, EntityDoctorZombie.class, "doctorZombie", id++, LuggageMod.instance, 64, 3, true, 0xFF0303, 0xF9F919);
        EntityRegistry.addSpawn(EntityDoctorZombie.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
        
        //GoroZombie
        ResourceLocation goroZombie= new ResourceLocation(Reference.MOD_ID, "goro_zombie");
        EntityRegistry.registerModEntity(goroZombie, EntityGoroZombie.class, "goroZombie", id++, LuggageMod.instance, 64, 3, true, 0xFF03F3, 0xF9F919);
        EntityRegistry.addSpawn(EntityGoroZombie.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.HELL  );
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        RenderingRegistry.registerEntityRenderingHandler(EntityMagmaSpider.class, new RenderMagmaSpider.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntitySpeedySpider.class, new RenderSpeedySpider.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityHypnoSpider.class, new RenderHypnoSpider.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityHypnoBall.class, new RenderHypnoBall.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityWitherSpider.class, new RenderWitherSpider.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityMiniSpider.class, new RenderMiniSpider.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntitySorcererSpider.class, new RenderSorcererSpider.Factory());
 //       RenderingRegistry.registerEntityRenderingHandler(EntityTricksterSpider.class, new RenderTricksterSpider.Factory());
        if(MobsConfig.spiders.angel.spawn)RenderingRegistry.registerEntityRenderingHandler(EntityAngelSpider.class, new RenderAngelSpider.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityMotherSpider.class, new RenderMotherSpider.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntitySpiderEgg.class, new RenderSpiderEgg.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityWitheringSkeleton.class, new RenderWitheringSkeleton.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonRemains.class, new RenderSkeletonRemains.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityMagmaArrow.class, new RenderMagmaArrow.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityShamanSkeleton.class, new RenderShamanSkeleton.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityJokerSkeleton.class, new RenderJokerSkeleton.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityHeartArrow.class, new RenderHeartArrow.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntitySniperSkeleton.class, new RenderSniperSkeleton.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityMagmaSkeleton.class, new RenderMagmaSkeleton.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityCorruptedSkeleton.class, new RenderCorruptedSkeleton.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityKnightZombie.class, new RenderKnightZombie.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityWorkerZombie.class, new RenderWorkerZombie.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityMagmaZombie.class, new RenderMagmaZombie.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityDoctorZombie.class, new RenderDoctorZombie.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityGoroZombie.class, new RenderGoroZombie.Factory());
    }
}
