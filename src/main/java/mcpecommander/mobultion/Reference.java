package mcpecommander.mobultion;

import javax.annotation.Nonnull;

import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class Reference {
	
	public static final String MOD_ID = "mobultion";
	public static final String NAME = "mcpeCommander's mobultion mod";
	public static final String VERSION = "0.0.9";
	public static final String ACCEPTED_MINECRAFT_VERSIONS = "[1.12, 1.13)";
	
	public static final String CLIENT_PROXY_CLASS = "mcpecommander.mobultion.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "mcpecommander.mobultion.proxy.ServerProxy";
	
	public static boolean removeEnchant(int id, ItemStack item){
		if(item.isEmpty()){
			return false;
		}
		for(int i = 0; i < item.getEnchantmentTagList().tagCount(); i++){
			if(item.getEnchantmentTagList().getCompoundTagAt(i).getShort("id") == id){
				item.getEnchantmentTagList().removeTag(i);
				return true;
			}
		}
		return false;
	}
	
	//ItemNames
	
	public static enum MobultionItems {
		FORESTBOW("forest_bow","item_forest_bow"),
		HEALINGWAND("healing_wand","item_healing_wand"),
		HEARTARROW("heart_arrow","item_heart_arrow"),
		HAMMER("hammer","item_hammer"),
		FIRESWORD("fire_sword","item_fire_sword"),
		HEALTH("health", "item_health"),
		FORK("fork","item_fork"),
		KNIFE("knife","item_knife"),
		HAT("hat","item_hat"),
		ENDERBLAZE("ender_blaze","item_ender_blaze"),
		ENDERFLAKE("ender_flake","item_ender_flake"),
		CORRUPTEDBONE("corrupted_bone","item_corrupted_bone"),
		CORRUPTEDBONEMEAL("corrupted_bonemeal","item_corrupted_bonemeal"),
		HOLYSHARD("holy_shard","item_holy_shard"),
		HYPNOBALL("hypno_ball","item_hypno_ball"),
		FANGNECKLACE("fang_necklace","item_fang_necklace"),
		FANG("fang","item_fang"),
		MAGMAARROW("magma_arrow","item_magma_arrow");
		
		private String unlocalizedName;
		private String registryName;
		
		private MobultionItems(String unlocalizedName, String registryName) {
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
		VAMPIRESKELETON(new ResourceLocation(MOD_ID, "vampire_skeleton"), "vampire_skeleton"),
		
		SKELETONREMAINS(new ResourceLocation(MOD_ID, "skeleton_remains"), "skeleton_remains"),
		HEARTARROW(new ResourceLocation(MOD_ID, "heart_arrow"), "heart_arrow"),
		MAGMAARROW(new ResourceLocation(MOD_ID, "magma_arrow"), "magma_arrow"),
		
		//Zombies
		DOCTORZOMBIE(new ResourceLocation(MOD_ID, "doctor_zombie"), "doctor_zombie"),
		GOROZOMBIE(new ResourceLocation(MOD_ID, "goro_zombie"), "goro_zombie"),
		KNIGHTZOMBIE(new ResourceLocation(MOD_ID, "knight_zombie"), "knight_zombie"),
		MAGMAZOMBIE(new ResourceLocation(MOD_ID, "magma_zombie"), "magma_zombie"),
		WORKERZOMBIE(new ResourceLocation(MOD_ID, "worker_zombie"), "worker_zombie"),
		RAVENOUSZOMBIE(new ResourceLocation(MOD_ID, "ravenous_zombie"), "ravenous_zombie"),
		
		//Endermen
		MAGMAENDERMAN(new ResourceLocation(MOD_ID, "magma_enderman"), "magma_enderman"),
		ICEENDERMAN(new ResourceLocation(MOD_ID, "ice_enderman"), "ice_enderman"),
		ENDERBLAZE(new ResourceLocation(MOD_ID, "ender_blaze"), "ender_blaze"),
		ENDERFLAKE(new ResourceLocation(MOD_ID, "ender_flake"), "ender_flake"),
		
		//Mites
		WOODMITE(new ResourceLocation(MOD_ID, "woodmite"), "woodmite")
		
		
		;
		
		
		
		
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
	
	public static class LootTables{
		
		public static final ResourceLocation ENTITYCORRUPTEDSKELETON = new ResourceLocation(Reference.MOD_ID, "skeletons/corrupted_skeleton");
		public static final ResourceLocation ENTITYJOKERSKELETON = new ResourceLocation(Reference.MOD_ID, "skeletons/joker_skeleton");
		public static final ResourceLocation ENTITYMAGMASKELETON = new ResourceLocation(Reference.MOD_ID, "skeletons/magma_skeleton");
		public static final ResourceLocation ENTITYSHAMANSKELETON = new ResourceLocation(Reference.MOD_ID, "skeletons/shaman_skeleton");
		public static final ResourceLocation ENTITYVAMPIRESKELETON = new ResourceLocation(Reference.MOD_ID, "skeletons/vampire_skeleton");
	}

}
