package mcpecommander.mobultion;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Reference {
	
	public static final String MOD_ID = "mobultion";
	public static final String NAME = "mcpeCommander's mobultion mod";
	public static final String VERSION = "0.3";
	public static final String ACCEPTED_MINECRAFT_VERSIONS = "[1.12, 1.13)";
	
	public static final String CLIENT_PROXY_CLASS = "mcpecommander.mobultion.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "mcpecommander.mobultion.proxy.ServerProxy";
	
	
	
	public static boolean removeEnchant(int id, ItemStack itemStack){
		if(itemStack.isEmpty()){
			return false;
		}
		for(int i = 0; i < itemStack.getEnchantmentTagList().tagCount(); i++){
			if(itemStack.getEnchantmentTagList().getCompoundTagAt(i).getShort("id") == id){
				itemStack.getEnchantmentTagList().removeTag(i);
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
		ENDERGLASSSHOT("ender_glass_shot","item_ender_glass_shot"),
		CORRUPTEDBONE("corrupted_bone","item_corrupted_bone"),
		CORRUPTEDBONEMEAL("corrupted_bonemeal","item_corrupted_bonemeal"),
		HOLYSHARD("holy_shard","item_holy_shard"),
		HYPNOBALL("hypno_ball","item_hypno_ball"),
		FANGNECKLACE("fang_necklace","item_fang_necklace"),
		FANG("fang", "item_fang"),
		MAGMAARROW("magma_arrow", "item_magma_arrow"),
		MAGICTOUCH("magic_touch", "item_magic_touch"),
		SORCERERBREATH("sorcerer_breath", "item_sorcerer_breath"),
		WITHERSPINE("wither_spine", "item_wither_spine"),
		SPINEASH("spine_ash", "item_spine_ash"),
		FLAMINGCHIP("flaming_chip", "item_flaming_chip"),
		NETHERRUBY("nether_ruby", "item_nether_ruby"),
		PIGMANFLESH("pigman_flesh", "item_pigman_flesh"),
		PIGSHEATHTUNIC("pigsheath_tunic", "item_pigsheath_tunic"),
		PIGSHEATHHELMET("pigsheath_helmet", "item_pigsheath_helmet"),
		PIGSHEATHLEGGINGS("pigsheath_leggings", "item_pigsheath_leggings"),
		PIGSHEATHBOOTS("pigsheath_boots", "item_pigsheath_boots"),
		THUNDERWAND("thunder_wand", "item_thunder_wand"),
		HAYHAT("hay_hat", "item_hay_hat"),
		SPAWNCHANGER("spawn_changer", "item_spawn_changer");
		
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
		ANGELSPIDER(new ResourceLocation(MOD_ID, "angel_spider"), "angel_spider", "mobultion:angel_spider.info"),
		HYPNOSPIDER(new ResourceLocation(MOD_ID, "hypno_spider"), "hypno_spider", "mobultion:hypno_spider.info"),
		MAGMASPIDER(new ResourceLocation(MOD_ID, "magma_spider"), "magma_spider", "mobultion:magma_spider.info"),
		MINISPIDER(new ResourceLocation(MOD_ID, "mini_spider"), "mini_spider", "mobultion:mini_spider.info"),
		MOTHERSPIDER(new ResourceLocation(MOD_ID, "mother_spider"), "mother_spider", "mobultion:mother_spider.info"),
		SORCERERSPIDER(new ResourceLocation(MOD_ID, "sorcerer_spider"), "sorcerer_spider", "mobultion:sorcerer_spider.info"),
		SPEEDYSPIDER(new ResourceLocation(MOD_ID, "speedy_spider"), "speedy_spider", "mobultion:speedy_spider.info"),
		WITHERSPIDER(new ResourceLocation(MOD_ID, "wither_spider"), "wither_spider", "mobultion:wither_spider.info"),
		
		SPIDEREGG(new ResourceLocation(MOD_ID, "spider_egg"), "spider_egg", ""),
		HYPNOBALL(new ResourceLocation(MOD_ID, "hypno_ball"), "hypno_ball", ""),
		
		//Skeletons
		CORRUPTEDSKELETON(new ResourceLocation(MOD_ID, "corrupted_skeleton"), "corrupted_skeleton", "mobultion:corrupted_skeleton.info"),
		JOKERSKELETON(new ResourceLocation(MOD_ID, "joker_skeleton"), "joker_skeleton", "mobultion:joker_skeleton.info"),
		MAGMASKELETON(new ResourceLocation(MOD_ID, "magma_skeleton"), "magma_skeleton", "mobultion:magma_skeleton.info"),
		SHAMANSKELETON(new ResourceLocation(MOD_ID, "shaman_skeleton"), "shaman_skeleton", "mobultion:shaman_skeleton.info"),
		SNIPERSKELETON(new ResourceLocation(MOD_ID, "sniper_skeleton"), "sniper_skeleton", "mobultion:sniper_skeleton.info"),
		WITHERINGSKELETON(new ResourceLocation(MOD_ID, "withering_skeleton"), "withering_skeleton", "mobultion:withering_skeleton.info"),
		VAMPIRESKELETON(new ResourceLocation(MOD_ID, "vampire_skeleton"), "vampire_skeleton", "mobultion:vampire_skeleton.info"),
		
		SKELETONREMAINS(new ResourceLocation(MOD_ID, "skeleton_remains"), "skeleton_remains", ""),
		HEARTARROW(new ResourceLocation(MOD_ID, "heart_arrow"), "heart_arrow", ""),
		MAGMAARROW(new ResourceLocation(MOD_ID, "magma_arrow"), "magma_arrow", ""),
		
		//Zombies
		DOCTORZOMBIE(new ResourceLocation(MOD_ID, "doctor_zombie"), "doctor_zombie", "mobultion:doctor_zombie.info"),
		GOROZOMBIE(new ResourceLocation(MOD_ID, "goro_zombie"), "goro_zombie", "mobultion:goro_zombie.info"),
		KNIGHTZOMBIE(new ResourceLocation(MOD_ID, "knight_zombie"), "knight_zombie", "mobultion:knight_zombie.info"),
		MAGMAZOMBIE(new ResourceLocation(MOD_ID, "magma_zombie"), "magma_zombie", "mobultion:magma_zombie.info"),
		WORKERZOMBIE(new ResourceLocation(MOD_ID, "worker_zombie"), "worker_zombie", "mobultion:worker_zombie.info"),
		RAVENOUSZOMBIE(new ResourceLocation(MOD_ID, "ravenous_zombie"), "ravenous_zombie", "mobultion:ravenous_zombie.info"),
		
		//Endermen
		MAGMAENDERMAN(new ResourceLocation(MOD_ID, "magma_enderman"), "magma_enderman", "mobultion:magma_enderman.info"),
		ICEENDERMAN(new ResourceLocation(MOD_ID, "ice_enderman"), "ice_enderman", "mobultion:ice_enderman.info"),
		WANDERINGENDERMAN(new ResourceLocation(MOD_ID, "wandering_enderman"), "wandering_enderman", "mobultion:wandering_enderman.info"),
		GARDENERENDERMAN(new ResourceLocation(MOD_ID, "gardener_enderman"), "gardener_enderman", "mobultion:gardener_enderman.info"),
		GLASSENDERMAN(new ResourceLocation(MOD_ID, "glass_enderman"), "glass_enderman", "mobultion:glass_enderman.info"),
		ENDERPROJECTILE(new ResourceLocation(MOD_ID, "ender_projectile"), "ender_projectile", ""),
		GLASSSHOT(new ResourceLocation(MOD_ID, "glass_shot"), "glass_shot", ""),
		
		//Mites
		WOODMITE(new ResourceLocation(MOD_ID, "woodmite"), "woodmite", "mobultion:woodmite.info")
		
		
		;
		
		
		
		
		private ResourceLocation registryName;
		private String unlocalizedName;
		private String info;
		
		private MobultionEntities(ResourceLocation registryName, String unlocalizedName, String info){
			this.unlocalizedName = unlocalizedName;
			this.registryName = registryName;
			this.info = info;
		}
		
		public String getUnlocalizedName(){
			return registryName.toString();
		}
		
		public ResourceLocation getRegistryName(){
			return registryName;
		}
		
		public String getInfo(){
			return info;
		}
		
		public static MobultionEntities getEnumByRegistery(ResourceLocation registry){
			for(MobultionEntities value : MobultionEntities.values()){
				if(value.registryName.equals(registry)){
					return value;
				}
			}
			return null;
		}
		
	}
	
	
	public static class LootTables{
		
		public static final ResourceLocation ENTITYANGELSPIDER = new ResourceLocation(Reference.MOD_ID, "spiders/angel_spider");
		public static final ResourceLocation ENTITYHYPNOSPIDER = new ResourceLocation(Reference.MOD_ID, "spiders/hypno_spider");
		public static final ResourceLocation ENTITYWITHERSPIDER = new ResourceLocation(Reference.MOD_ID, "spiders/wither_spider");
		public static final ResourceLocation ENTITYSORCERERSPIDER = new ResourceLocation(Reference.MOD_ID, "spiders/sorcerer_spider");
		public static final ResourceLocation ENTITYMAGMASPIDER = new ResourceLocation(Reference.MOD_ID, "spiders/magma_spider");
		
		public static final ResourceLocation ENTITYCORRUPTEDSKELETON = new ResourceLocation(Reference.MOD_ID, "skeletons/corrupted_skeleton");
		public static final ResourceLocation ENTITYJOKERSKELETON = new ResourceLocation(Reference.MOD_ID, "skeletons/joker_skeleton");
		public static final ResourceLocation ENTITYMAGMASKELETON = new ResourceLocation(Reference.MOD_ID, "skeletons/magma_skeleton");
		public static final ResourceLocation ENTITYSHAMANSKELETON = new ResourceLocation(Reference.MOD_ID, "skeletons/shaman_skeleton");
		public static final ResourceLocation ENTITYVAMPIRESKELETON = new ResourceLocation(Reference.MOD_ID, "skeletons/vampire_skeleton");
		public static final ResourceLocation ENTITYWITHERINGSKELETON = new ResourceLocation(Reference.MOD_ID, "skeletons/withering_skeleton");
		
		public static final ResourceLocation ENTITYMAGMAENDERMAN = new ResourceLocation(Reference.MOD_ID, "endermen/magma_enderman");
		public static final ResourceLocation ENTITYICEENDERMAN = new ResourceLocation(Reference.MOD_ID, "endermen/ice_enderman");
	}

}
