package mcpecommander.luggagemod;

public class Reference {
	
	public static final String MOD_ID = "mlm";
	public static final String NAME = "mcpeCommander's luggage mod";
	public static final String VERSION = "0.0.1";
	public static final String ACCEPTED_MINECRAFT_VERSIONS = "[1.12, 1.13)";
	
	public static final String CLIENT_PROXY_CLASS = "mcpecommander.luggagemod.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "mcpecommander.luggagemod.proxy.ServerProxy";
	
	//ItemNames
	
	public static enum LuggageModItems {
		CRACKER("cracker", "item_cracker"),
		CHEESE("cheese" , "item_cheese"),
		CHEESE_AND_CRACKER("cheese_and_cracker", "item_cheese_and_cracker"),
		WAND("wand", "item_wand"),
		FORESTBOW("forest_bow","item_forest_bow"),
		HEALINGWAND("healing_wand","item_healing_wand"),
		HEARTARROW("heart_arrow","item_heart_arrow"),
		HAT("hat","item_hat"),
		HAMMER("hammer","item_hammer"),
		FIRE_SWORD("fire_sword","item_fire_sword"),
		HEALTH("health", "item_health");
		
		private String unlocalizedName;
		private String registryName;
		
		private LuggageModItems(String unlocalizedName, String registryName) {
			this.unlocalizedName = unlocalizedName;
			this.registryName = registryName;
		}
		
		public String getRegistryName() {
			return registryName;
		}
		
		public String getUnlocalizedName() {
			return unlocalizedName;
		}
	}
	
	//BlocksNames
	
	public static enum LuggageModBlocks {
		CHEESE("cheese" , "block_cheese"),
		FAKECHEESE("fake_Cheese" , "block_fake_cheese"),
		JAR("jar", "block_jar"),
		COUNTER("counter", "block_counter"),
		BLINKER("blinker", "block_blinker"),
		PANELLIGHT("panel_light", "block_panel_light"),
		ATM("atm", "block_atm"),
		ANTISLIME("anti_slime", "block_anti_slime"),
		MOLTENCHEESE("molten_cheese","fluid_block_molten_cheese"),
		ROBINHAT("robin_hat", "block_robin_hat"),
		JOKERHAT("joker_hat", "block_joker_hat");
		
		private String unlocalizedName;
		private String registryName;
		
		private LuggageModBlocks(String unlocalizedName, String registryName) {
			this.unlocalizedName = unlocalizedName;
			this.registryName = registryName;
		}
		
		public String getRegistryName() {
			return registryName;
		}
		
		public String getUnlocalizedName() {
			return unlocalizedName;
		}
	}
}
