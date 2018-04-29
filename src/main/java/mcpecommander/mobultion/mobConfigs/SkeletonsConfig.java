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

			@Config.RangeInt(min = 20, max = 1000)
			@Config.Comment("How long(in ticks) the mining fatigue effect will last")
			public int miningFatigue = 100;

			@Config.RangeInt(min = 10, max = 1000)
			@Config.Comment("How long (in ticks) is the slowness effect going to be")
			public int slowness = 100;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 1, 4, "minecraft:desert", "minecraft:desert_hills", "minecraft:savanna_rock", "minecraft:mutated_desert",
					"minecraft:mutated_savanna", "minecraft:mutated_savanna_rock");
			
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
			
			@Config.Comment("If this skeleton should spawn" + " from the desert temple loot chest (base chance is 2%)")
			public boolean spawnFromLootChests = true;

			@Config.Comment("If this skeleton should spawn" + " in the temples")
			public boolean templeSpawn = true;

		}

		@Config.Comment("Joker Skeleton")
		public Joker joker = new Joker();

		public static class Joker {

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
			public Spawn spawnRates = new Spawn(100, 3, 7, "minecraft:mutated_plains", "minecraft:mutated_desert", "minecraft:mutated_extreme_hills",
					"minecraft:mutated_forest", "minecraft:mutated_taiga", "minecraft:mutated_swampland", "minecraft:mutated_ice_flats", "minecraft:mutated_jungle",
					"minecraft:mutated_jungle_edge", "minecraft:mutated_birch_forest", "minecraft:mutated_birch_forest_hills",
					"minecraft:mutated_roofed_forest", "minecraft:mutated_taiga_cold", "minecraft:mutated_redwood_taiga",
					"minecraft:mutated_redwood_taiga_hills", "minecraft:mutated_extreme_hills_with_trees", "minecraft:mutated_savanna",
					"minecraft:mutated_savanna_rock", "minecraft:mutated_mesa", "minecraft:mutated_mesa_rock", "minecraft:mutated_mesa_clear_rock");
			
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

		@Config.Comment("Magma Skeleton")
		public Magma magma = new Magma();

		public static class Magma {

			@Config.Comment("Can the arrow make glass when hitting sand")
			public boolean glassMaking = true;

			@Config.Comment("Can the arrow make cobble when hitting or passing water")
			public boolean cobbleMaking = true;

			@Config.Comment("Should this mob get hurt from water or rain")
			public boolean wetDamage = true;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(80, 3, 4, "minecraft:desert", "minecraft:desert_hills", "minecraft:savanna_rock", "minecraft:mutated_desert",
					"minecraft:mutated_savanna", "minecraft:mutated_savanna_rock", "minecraft:hell");
			
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

		@Config.Comment("Shaman Skeleton")
		public Shaman shaman = new Shaman();

		public static class Shaman {

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
			public Spawn spawnRates = new Spawn(100, 1, 1, "minecraft:extreme_hills", "minecraft:forest", "minecraft:swampland", "minecraft:mushroom_island",
					"minecraft:mushroom_island_shore", "minecraft:jungle", "minecraft:forest_hills", "minecraft:jungle_hills", "minecraft:jungle_edge", "minecraft:birch_forest",
					"minecraft:birch_forest_hills", "minecraft:roofed_forest", "minecraft:mutated_forest", "minecraft:mutated_jungle", "minecraft:mutated_swampland",
					"minecraft:mutated_birch_forest");
			
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

		@Config.Comment("Sniper Skeleton")
		public Sniper sniper = new Sniper();

		public static class Sniper {

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
			public Spawn spawnRates = new Spawn(50, 1, 1, "minecraft:forest", "minecraft:birch_forest", "minecraft:roofed_forest", "minecraft:birch_forest_hills",
					"minecraft:mutated_forest", "minecraft:mutated_birch_forest", "minecraft:mutated_roofed_forest");
			
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

		@Config.Comment("Withering Skeleton")
		public Withering withering = new Withering();

		public static class Withering {

			@Config.RangeInt(min = 10, max = 1000)
			@Config.Comment("How long (in ticks) is the wither effect going to be (from the arrow)")
			public int wither = 100;

			@Config.Comment("Should this spider spawn on the nether fortress")
			public boolean netherFortressSpawn = true;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 1, 3, "minecraft:sky");
			
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
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 1, 3, "minecraft:jungle", "minecraft:jungle_hills", "minecraft:jungle_edge", "minecraft:mutated_jungle", "minecraft:mutated_jungle_edge");
			
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
	
	@Config.Comment("Skeleton Remains")
	public static Remains remains = new Remains();

	public static class Remains {
		
		@Config.Comment("Remains max health")
		@Config.RangeDouble(min = 1, max = 100)
		public double health = 50d;
		
		@Config.Comment("How many days does it need to reach max health")
		@Config.RangeInt(min = 1, max = 100)
		public int days = 10;
		
		@Config.Comment("How many seconds does it take to respawn")
		@Config.RangeInt(min = 5, max = 1000)
		public int respawnTime = 60;
	}
}
