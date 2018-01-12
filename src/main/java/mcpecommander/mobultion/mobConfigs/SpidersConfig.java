package mcpecommander.mobultion.mobConfigs;

import javax.annotation.Nullable;

import mcpecommander.mobultion.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID, category = "Spiders", name = "Spiders")
public class SpidersConfig {

	@Config.Comment("Spiders' Configs")
	public static final Spiders spiders = new Spiders();

	public static class Spiders {

		@Config.Comment("Angel Spider config")
		public Angel angel = new Angel();

		public static class Angel {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeInt(min = 0, max = 16)
			@Config.Comment("how much the Angel spider heals")
			public float healAmount = 4f;
			
			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 1, 4, "extreme_hills", "ice_mountains", "cold_beach", "mutated_ice_flats" );
			
			public static class Spawn{

				public Spawn(int weight, int min, int max, @Nullable String... biomes) {
					this.weight = weight;
					this.min = min;
					this.max = max;
					this.biomes = biomes;
				}
				
				@Config.RangeInt(min = 1, max = 500)
				@Config.Comment("Spawn weight")
				public int weight;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Minimum spawn count")
				public int min;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Maximum spawn count")
				public int max;
				
				@Config.Comment("Biomes to spawn in")
				public String[] biomes;
				
			}

//			@Config.Comment("Where this spider can spawn")
//			public String[] spawnLocations = { "extreme_hills", "ice_mountains", "cold_beach", "mutated_ice_flats" };

		}

		@Config.Comment("Hypno Spider config")
		public Hypno hypno = new Hypno();

		public static class Hypno {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeInt(min = 20, max = 600)
			@Config.Comment("how much time (in ticks) does it take for the mob to fire again")
			public int fireDelay = 100;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 3, 6, "swampland", "mushroom_island", "mushroom_island_shore", "taiga_hills",
					"mesa", "mesa_rock", "mesa_clear_rock", "mutated_desert", "mutated_swampland", "mutated_mesa",
					"mutated_mesa_rock", "mutated_mesa_clear_rock" );
			
			public static class Spawn{

				public Spawn(int weight, int min, int max, @Nullable String... biomes) {
					this.weight = weight;
					this.min = min;
					this.max = max;
					this.biomes = biomes;
				}
				
				@Config.RangeInt(min = 1, max = 500)
				@Config.Comment("Spawn weight")
				public int weight;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Minimum spawn count")
				public int min;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Maximum spawn count")
				public int max;
				
				@Config.Comment("Biomes to spawn in")
				public String[] biomes;
				
			}
			
//			@Config.Comment("Where this spider can spawn")
//			public String[] spawnLocations = { "swampland", "mushroom_island", "mushroom_island_shore", "taiga_hills",
//					"mesa", "mesa_rock", "mesa_clear_rock", "mutated_desert", "mutated_swampland", "mutated_mesa",
//					"mutated_mesa_rock", "mutated_mesa_clear_rock" };
		}

		@Config.Comment("Magma Spider config")
		public Magma magma = new Magma();

		public static class Magma {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeInt(min = 20, max = 1000)
			@Config.Comment("How much is the chance (1/this number) to spawn the spider as a spider jockey")
			public int jockeyChance = 80;

			@Config.RangeInt(min = 1, max = 10)
			@Config.Comment("How long (in seconds) is it going burn (plus a random based on the diffculty)")
			public int burningLength = 3;

			@Config.Comment("Should the mob be damaged if wet (raining or in water")
			public boolean waterDamage = true;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 3, 5, "desert", "desert_hills", "mutated_desert" );
			
			public static class Spawn{

				public Spawn(int weight, int min, int max, @Nullable String... biomes) {
					this.weight = weight;
					this.min = min;
					this.max = max;
					this.biomes = biomes;
				}
				
				@Config.RangeInt(min = 1, max = 500)
				@Config.Comment("Spawn weight")
				public int weight;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Minimum spawn count")
				public int min;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Maximum spawn count")
				public int max;
				
				@Config.Comment("Biomes to spawn in")
				public String[] biomes;
				
			}
			
//			@Config.Comment("Where this spider can spawn")
//			public String[] spawnLocations = { "desert", "desert_hills", "mutated_desert" };

			@Config.Comment("Should this spider spawn on the nether fortress")
			public boolean netherFortressSpawn = true;
		}

		@Config.Comment("Mini Spider config")
		public Mini mini = new Mini();

		public static class Mini {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeDouble(min = 0.1, max = 1)
			@Config.Comment("how fast is this mob (a zombie is 0.23)")
			public double speed = 0.52D;

		}

		@Config.Comment("Mother Spider config")
		public Mother mother = new Mother();

		public static class Mother {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeInt(min = 20, max = 1000)
			@Config.Comment("How long (in ticks) does this mob take to lay an egg")
			public int pregnancyTime = 80;

			@Config.RangeInt(min = 20, max = 1000)
			@Config.Comment("How long (in ticks) does the egg take to spawn a spider")
			public int eggHatchingTime = 100;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(60, 1, 2, "extreme_hills", "smaller_extreme_hills", "mutated_extreme_hills",
					"mutated_extreme_hills_with_trees");
			
			public static class Spawn{

				public Spawn(int weight, int min, int max, @Nullable String... biomes) {
					this.weight = weight;
					this.min = min;
					this.max = max;
					this.biomes = biomes;
				}
				
				@Config.RangeInt(min = 1, max = 500)
				@Config.Comment("Spawn weight")
				public int weight;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Minimum spawn count")
				public int min;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Maximum spawn count")
				public int max;
				
				@Config.Comment("Biomes to spawn in")
				public String[] biomes;
				
			}
			
//			@Config.Comment("Where this spider can spawn")
//			public String[] spawnLocations = { "extreme_hills", "smaller_extreme_hills", "mutated_extreme_hills",
//					"mutated_extreme_hills_with_trees" };
		}

		@Config.Comment("Sorcerer Spider config")
		public Sorcerer sorcerer = new Sorcerer();

		public static class Sorcerer {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeInt(min = 10, max = 100)
			@Config.Comment("How long (in ticks) does this mob take to cast its spell")
			public int castingTime = 40;

			@Config.RangeInt(min = 1, max = 6)
			@Config.Comment("Spawning the spiders based on the diffculty (1 and 2 will spawn vanilla spiders only,"
					+ " 6 will spawn this mod's spiders too)")
			public int mobsDiffculty = 6;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 1, 4, "forest", "forest_hills", "jungle", "jungle_hills", "jungle_edge",
					"birch_forest", "birch_forest_hills", "roofed_forest", "mutated_forest", "mutated_jungle",
					"mutated_birch_forest", "mutated_roofed_forest");
			
			public static class Spawn{

				public Spawn(int weight, int min, int max, @Nullable String... biomes) {
					this.weight = weight;
					this.min = min;
					this.max = max;
					this.biomes = biomes;
				}
				
				@Config.RangeInt(min = 1, max = 500)
				@Config.Comment("Spawn weight")
				public int weight;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Minimum spawn count")
				public int min;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Maximum spawn count")
				public int max;
				
				@Config.Comment("Biomes to spawn in")
				public String[] biomes;
				
			}
			
//			@Config.Comment("Where this spider can spawn")
//			public String[] spawnLocations = { "forest", "forest_hills", "jungle", "jungle_hills", "jungle_edge",
//					"birch_forest", "birch_forest_hills", "roofed_forest", "mutated_forest", "mutated_jungle",
//					"mutated_birch_forest", "mutated_roofed_forest" };
		}

		@Config.Comment("Speedy Spider config")
		public Speedy speedy = new Speedy();

		public static class Speedy {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeInt(min = 20, max = 1000)
			@Config.Comment("How much is the chance (1/this number) to spawn the spider as a spider jockey")
			public int jockeyChance = 200;

			@Config.RangeDouble(min = 0.35, max = 1)
			@Config.Comment("How fast is this mob (a zombie is 0.23")
			public double speed = 0.62D;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 2, 4, "ice_flats", "plains", "beaches", "mutated_plains",
					"mutated_ice_flats");
			
			public static class Spawn{

				public Spawn(int weight, int min, int max, @Nullable String... biomes) {
					this.weight = weight;
					this.min = min;
					this.max = max;
					this.biomes = biomes;
				}
				
				@Config.RangeInt(min = 1, max = 500)
				@Config.Comment("Spawn weight")
				public int weight;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Minimum spawn count")
				public int min;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Maximum spawn count")
				public int max;
				
				@Config.Comment("Biomes to spawn in")
				public String[] biomes;
				
			}
			
//			@Config.Comment("Where this will spawn")
//			public String[] spawnLocations = { "ice_flats", "plains", "beaches", "mutated_plains",
//					"mutated_ice_flats" };

		}

		@Config.Comment("Wither Spider config")
		public Wither wither = new Wither();

		public static class Wither {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeInt(min = 20, max = 1000)
			@Config.Comment("How much is the chance (1/this number) to spawn the spider as a spider jockey")
			public int jockeyChance = 200;

			@Config.RangeInt(min = 10, max = 1000)
			@Config.Comment("How long (in ticks) is the withering eeffct going to be")
			public int witheringLength = 100;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(50, 1, 2, "sky", "stone_beach");
			
			public static class Spawn{

				public Spawn(int weight, int min, int max, @Nullable String... biomes) {
					this.weight = weight;
					this.min = min;
					this.max = max;
					this.biomes = biomes;
				}
				
				@Config.RangeInt(min = 1, max = 500)
				@Config.Comment("Spawn weight")
				public int weight;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Minimum spawn count")
				public int min;
				
				@Config.RangeInt(min = 1, max = 16)
				@Config.Comment("Maximum spawn count")
				public int max;
				
				@Config.Comment("Biomes to spawn in")
				public String[] biomes;
				
			}
			
//			@Config.Comment("Where this will spawn")
//			public String[] spawnLocations = { "sky", "stone_beach" };

		}
	}

}
