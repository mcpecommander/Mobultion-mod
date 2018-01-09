package mcpecommander.mobultion.proxy;


import com.leviathanstudio.craftstudio.client.registry.CSRegistryHelper;
import com.leviathanstudio.craftstudio.client.registry.CraftStudioLoader;
import com.leviathanstudio.craftstudio.client.util.EnumRenderType;
import com.leviathanstudio.craftstudio.client.util.EnumResourceType;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModEntities;
import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.particle.TextureStitcherBreathFX;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy{
	
	@Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        ModEntities.initModels();
        MinecraftForge.EVENT_BUS.register(new TextureStitcherBreathFX());
    }


	@SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
    	
    	ModItems.initModels();
    }	

	@CraftStudioLoader
    public static void registerCraftStudioAssets() {
        CSRegistryHelper csRegistryHelper = new CSRegistryHelper(Reference.MOD_ID);
        //Skeletons
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "joker_skeleton");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "forest_skeleton");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "skeleton_remains");
        
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "skeleton_death");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "skeleton_walk");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "skeleton_holding_bow");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "skeleton_walk_hands");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "skeleton_healing");
        
        //Spiders
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "hypno_spider");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "speedy_spider");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "magma_spider");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "sorcerer_spider");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "mini_spider");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "mother_spider");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "wither_spider");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "angel_spider");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "egg");
        
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "ring");
        
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "hypno_rotate");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "spider_move");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "sorcerer_cast");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "mother_pregnant");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "egg_hatch");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "speedy_pull");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "wings_flap");
        
        //Zombies
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "zombie");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "test");
        csRegistryHelper.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "goro_zombie");
        
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "knight_slash");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "worker_hammering");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "doctor_throw");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "doctor_heal");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "goro_slash");
        csRegistryHelper.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "goro_hands");
    }

}
