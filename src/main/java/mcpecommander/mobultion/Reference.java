package mcpecommander.mobultion;

import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class Reference {
	
	public static final String MOD_ID = "mmm";
	public static final String NAME = "mcpeCommander's mobultion mod";
	public static final String VERSION = "0.0.1";
	public static final String ACCEPTED_MINECRAFT_VERSIONS = "[1.12, 1.13)";
	
	public static final String CLIENT_PROXY_CLASS = "mcpecommander.mobultion.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "mcpecommander.mobultion.proxy.ServerProxy";
	
	//ItemNames
	
	public static enum ModItems {
		FORESTBOW("forest_bow","item_forest_bow"),
		HEALINGWAND("healing_wand","item_healing_wand"),
		HEARTARROW("heart_arrow","item_heart_arrow"),
		HAMMER("hammer","item_hammer"),
		FIRE_SWORD("fire_sword","item_fire_sword"),
		HEALTH("health", "item_health");
		
		private String unlocalizedName;
		private String registryName;
		
		private ModItems(String unlocalizedName, String registryName) {
			this.unlocalizedName = unlocalizedName;
			this.registryName = registryName;
		}
		
		public String getRegistryName() {
			return registryName;
		}
		
		public String getUnlocalizedName() {
			return MOD_ID + ":" + unlocalizedName;
		}
	}
	
	public static enum MobultionEntities{
		//Spiders
		ANGELSPIDER(new ResourceLocation(MOD_ID, "angel_spider"), "angel_spider"),
		HYPNOSPIDER(new ResourceLocation(MOD_ID, "hypno_spider"), "hypno_spider"),
		MAGMASPIDER(new ResourceLocation(MOD_ID, "magma_spider"), "magma_spider"),
		MINISPIDER(new ResourceLocation(MOD_ID, "mini_spider"), "mini_spider"),
		MOTHERSPIDER(new ResourceLocation(MOD_ID, "mother_spider"), "mother_spider"),
		SORCERERSPIDER(new ResourceLocation(MOD_ID, "sorcerer_spider"), "sorcerer_spider"),
		SPEEDYSPIDER(new ResourceLocation(MOD_ID, "speedy_spider"), "speedy_spider"),
		WITHERSPIDER(new ResourceLocation(MOD_ID, "wither_spider"), "wither_spider"),
		
		SPIDEREGG(new ResourceLocation(MOD_ID, "spider_egg"), "spider_egg"),
		HYPNOBALL(new ResourceLocation(MOD_ID, "hypno_ball"), "hypno_ball"),
		
		//Skeletons
		CORRUPTEDSKELETON(new ResourceLocation(MOD_ID, "corrupted_skeleton"), "corrupted_skeleton"),
		JOKERSKELETON(new ResourceLocation(MOD_ID, "joker_skeleton"), "joker_skeleton"),
		MAGMASKELETON(new ResourceLocation(MOD_ID, "magma_skeleton"), "magma_skeleton"),
		SHAMANSKELETON(new ResourceLocation(MOD_ID, "shaman_skeleton"), "shaman_skeleton"),
		SNIPERSKELETON(new ResourceLocation(MOD_ID, "sniper_skeleton"), "sniper_skeleton"),
		WITHERINGSKELETON(new ResourceLocation(MOD_ID, "withering_skeleton"), "withering_skeleton"),
		
		SKELETONREMAINS(new ResourceLocation(MOD_ID, "skeleton_remains"), "skeleton_remains"),
		HEARTARROW(new ResourceLocation(MOD_ID, "heart_arrow"), "heart_arrow"),
		MAGMAARROW(new ResourceLocation(MOD_ID, "magma_arrow"), "magma_arrow"),
		
		//Zombies
		DOCTORZOMBIE(new ResourceLocation(MOD_ID, "doctor_zombie"), "doctor_zombie"),
		GOROZOMBIE(new ResourceLocation(MOD_ID, "goro_zombie"), "goro_zombie"),
		KNIGHTZOMBIE(new ResourceLocation(MOD_ID, "knight_zombie"), "knight_zombie"),
		MAGMAZOMBIE(new ResourceLocation(MOD_ID, "magma_zombie"), "magma_zombie"),
		WORKERZOMBIE(new ResourceLocation(MOD_ID, "worker_zombie"), "woker_zombie");
		
		
		
		private ResourceLocation registryName;
		private String unlocalizedName;
		
		private MobultionEntities(ResourceLocation registryName, String unlocalizedName){
			this.unlocalizedName = unlocalizedName;
			this.registryName = registryName;
		}
		
		public String getUnlocalizedName(){
			return unlocalizedName;
		}
		
		public ResourceLocation getRegistryName(){
			return registryName;
		}
	}
	
	
	
	
}
