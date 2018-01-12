package mcpecommander.mobultion.mobConfigs;

import javax.annotation.Nullable;

import mcpecommander.mobultion.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID, category = "Zombies", name = "Zombies")
public class ZombiesConfig {
	
	@Config.Comment("Zombies' Configs")
	public static final Zombies zombies = new Zombies();

	public static class Zombies {

		@Config.Comment("Magma Zombie")
		public Magma magma = new Magma();

		public static class Magma {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.Comment("Will this mob spawn lava upno death from the lava sword")
			public boolean lavaMaking = true;

			@Config.Comment("The fire sword will always make lava upon killing entities")
			public boolean alwaysLava = false;

			@Config.Comment("Should this mob get hurt from water or rain")
			public boolean wetDamage = true;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 2, 4, "desert", "desert_hills", "savanna_rock", "mutated_desert",
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

		@Config.Comment("Doctor Zombie")
		public Doctor doctor = new Doctor();

		public static class Doctor {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.Comment("Should this mob extinguish burning zombies")
			public boolean shouldExtinguish = true;

			@Config.RangeDouble(min = 0.1D, max = 20D)
			@Config.Comment("The fire sword will always make lava upon killing entities")
			public double healAmount = 2D;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(50, 1, 2, "all");
			
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
//			public String[] spawnLocations = { "all", "m" };

		}

		@Config.Comment("Worker Zombie")
		public Worker worker = new Worker();

		public static class Worker {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 2, 4, "extreme_hills", "smaller_extreme_hills", "extreme_hills_with_trees",
					"mutated_extreme_hills", "mutated_extreme_hills_with_trees");
			
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
//			public String[] spawnLocations = { "extreme_hills", "smaller_extreme_hills", "extreme_hills_with_trees",
//					"mutated_extreme_hills", "mutated_extreme_hills_with_trees" };
			
			@Config.Comment("Should this monster hammer the player into the ground "
					+ "(cancel this feature on skyblock maps)")
			public boolean hammerAttack = true;

		}

		@Config.Comment("Knight Zombie")
		public Knight knight = new Knight();

		public static class Knight {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 2, 4, "extreme_hills", "smaller_extreme_hills", "extreme_hills_with_trees",
					"mutated_extreme_hills", "mutated_extreme_hills_with_trees");
			
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
//			public String[] spawnLocations = { "extreme_hills", "smaller_extreme_hills", "extreme_hills_with_trees",
//					"mutated_extreme_hills", "mutated_extreme_hills_with_trees" };

		}

		@Config.Comment("Goro Zombie")
		public Goro goro = new Goro();

		public static class Goro {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(40, 1, 2, "extreme_hills", "smaller_extreme_hills", "extreme_hills_with_trees",
					"mutated_extreme_hills", "mutated_extreme_hills_with_trees");
			
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
//			public String[] spawnLocations = { "extreme_hills", "smaller_extreme_hills", "extreme_hills_with_trees",
//					"mutated_extreme_hills", "mutated_extreme_hills_with_trees" };

		}
	}

}
