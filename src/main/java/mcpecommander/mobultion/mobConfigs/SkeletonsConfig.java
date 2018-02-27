package mcpecommander.mobultion.mobConfigs;

import javax.annotation.Nullable;

import mcpecommander.mobultion.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID, category = "mobs", name = "Mobultion/Skeletons")
public class SkeletonsConfig {

	@Config.Comment("Skeletons' Configs")
	public static final Skeletons skeletons = new Skeletons();

	public static class Skeletons {

		@Config.Comment("Corrupted Skeleton")
		public Corrupted corrupted = new Corrupted();

		public static class Corrupted {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeInt(min = 20, max = 1000)
			@Config.Comment("How long(in ticks) the mining fatigue effect will last")
			public int miningFatigue = 100;

			@Config.RangeInt(min = 10, max = 1000)
			@Config.Comment("How long (in ticks) is the slowness effect going to be")
			public int slowness = 100;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 1, 4, "desert", "desert_hills", "savanna_rock", "mutated_desert",
					"mutated_savanna", "mutated_savanna_rock");
			
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
//			public String[] spawnLocations = { "desert", "desert_hills", "savanna_rock", "mutated_desert",
//					"mutated_savanna", "mutated_savanna_rock" };

			@Config.Comment("If this skeleton should spawn" + " from the desert temple loot chest (base chance is 2%)")
			public boolean spawnFromLootChests = true;

			@Config.Comment("If this skeleton should spawn" + " in the temples")
			public boolean templeSpawn = true;

		}

		@Config.Comment("Joker Skeleton")
		public Joker joker = new Joker();

		public static class Joker {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeDouble(min = 0.01D, max = 10D)
			@Config.Comment("How much this arrow deals damage + a hardcoded calculated damage from the velocity")
			public double arrowDamage = 0.1D;

			@Config.Comment("Stop the Laughing sound, please.")
			public boolean laughSound = true;

			@Config.RangeInt(min = 10, max = 1000)
			@Config.Comment("How long (in ticks) is the jokerness effect going to be (from the arrow)")
			public int jokerness = 200;

			@Config.RangeDouble(min = 0D, max = 30D)
			@Config.Comment("How much is the inaccuracy (0 is a perfect sniper shot)")
			public double inaccuracy = 14D;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 3, 7, "mutated_plains", "mutated_desert", "mutated_extreme_hills",
					"mutated_forest", "mutated_taiga", "mutated_swampland", "mutated_ice_flats", "mutated_jungle",
					"mutated_jungle_edge", "mutated_birch_forest", "mutated_birch_forest_hills",
					"mutated_roofed_forest", "mutated_taiga_cold", "mutated_redwood_taiga",
					"mutated_redwood_taiga_hills", "mutated_extreme_hills_with_trees", "mutated_savanna",
					"mutated_savanna_rock", "mutated_mesa", "mutated_mesa_rock", "mutated_mesa_clear_rock");
			
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
//			public String[] spawnLocations = { "mutated_plains", "mutated_desert", "mutated_extreme_hills",
//					"mutated_forest", "mutated_taiga", "mutated_swampland", "mutated_ice_flats", "mutated_jungle",
//					"mutated_jungle_edge", "mutated_birch_forest", "mutated_birch_forest_hills",
//					"mutated_roofed_forest", "mutated_taiga_cold", "mutated_redwood_taiga",
//					"mutated_redwood_taiga_hills", "mutated_extreme_hills_with_trees", "mutated_savanna",
//					"mutated_savanna_rock", "mutated_mesa", "mutated_mesa_rock", "mutated_mesa_clear_rock" };

		}

		@Config.Comment("Magma Skeleton")
		public Magma magma = new Magma();

		public static class Magma {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.Comment("Can the arrow make glass when hitting sand")
			public boolean glassMaking = true;

			@Config.Comment("Can the arrow make cobble when hitting or passing water")
			public boolean cobbleMaking = true;

			@Config.Comment("Should this mob get hurt from water or rain")
			public boolean wetDamage = true;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(80, 3, 4, "desert", "desert_hills", "savanna_rock", "mutated_desert",
					"mutated_savanna", "mutated_savanna_rock", "hell");
			
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
//			public String[] spawnLocations = { "desert", "desert_hills", "savanna_rock", "mutated_desert",
//					"mutated_savanna", "mutated_savanna_rock", "hell" };

		}

		@Config.Comment("Shaman Skeleton")
		public Shaman shaman = new Shaman();

		public static class Shaman {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeInt(min = 1, max = 1200)
			@Config.Comment("How many ticks does this mob add to the graves "
					+ "(max 1200 because the grave respawns at 1200)")
			public int reviveAmount = 400;

			@Config.RangeDouble(min = 0.1D, max = 80D)
			@Config.Comment("How much health does this mob heal other skeletons")
			public double healAmount = 8D;

			@Config.RangeDouble(min = 0D, max = 1D)
			@Config.Comment("What is the chance to drop its wand (where 1 is 100%)")
			public double wandDropChance = 0d;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 1, 1, "extreme_hills", "forest", "swampland", "mushroom_island",
					"mushroom_island_shore", "jungle", "forest_hills", "jungle_hills", "jungle_edge", "birch_forest",
					"birch_forest_hills", "roofed_forest", "mutated_forest", "mutated_jungle", "mutated_swampland",
					"mutated_birch_forest");
			
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
//			public String[] spawnLocations = { "extreme_hills", "forest", "swampland", "mushroom_island",
//					"mushroom_island_shore", "jungle", "forest_hills", "jungle_hills", "jungle_edge", "birch_forest",
//					"birch_forest_hills", "roofed_forest", "mutated_forest", "mutated_jungle", "mutated_swampland",
//					"mutated_birch_forest" };

		}

		@Config.Comment("Sniper Skeleton")
		public Sniper sniper = new Sniper();

		public static class Sniper {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeInt(min = 5, max = 50)
			@Config.Comment("The tree serach radius, Warning: extremly heavy on the system,"
					+ " only increase if you are not using this mob too much")
			public int radius = 20;

			@Config.RangeDouble(min = 0D, max = 30D)
			@Config.Comment("How much is the inaccuracy (0 is a perfect sniper shot)")
			public double inaccuracy = 0D;

			@Config.RangeDouble(min = 0D, max = 1D)
			@Config.Comment("What is the chance to drop its wand (where 1 is 100%)")
			public double bowDropChance = 0.0885d;

			@Config.RangeInt(min = 10, max = 1000)
			@Config.Comment("How long (in ticks) is the poison effect going to be (from the arrow)")
			public int poison = 100;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(50, 1, 1, "forest", "birch_forest", "roofed_forest", "birch_forest_hills",
					"mutated_forest", "mutated_birch_forest", "mutated_roofed_forest");
			
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
//			public String[] spawnLocations = { "forest", "birch_forest", "roofed_forest", "birch_forest_hills",
//					"mutated_forest", "mutated_birch_forest", "mutated_roofed_forest" };

		}

		@Config.Comment("Withering Skeleton")
		public Withering withering = new Withering();

		public static class Withering {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RangeInt(min = 10, max = 1000)
			@Config.Comment("How long (in ticks) is the wither effect going to be (from the arrow)")
			public int wither = 100;

			@Config.Comment("Should this spider spawn on the nether fortress")
			public boolean netherFortressSpawn = true;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 1, 3, "sky");
			
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
			

		}
		
		@Config.Comment("Vampire Skeleton")
		public Vampire vampire = new Vampire();

		public static class Vampire {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 1, 3, "jungle", "jungle_hills", "jungle_edge", "mutated_jungle", "mutated_jungle_edge");
			
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
			

		}
	}
}
