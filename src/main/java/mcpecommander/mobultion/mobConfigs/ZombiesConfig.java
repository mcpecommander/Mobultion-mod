package mcpecommander.mobultion.mobConfigs;

import javax.annotation.Nullable;

import mcpecommander.mobultion.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID, category = "mobs", name = "Mobultion/Zombies")
public class ZombiesConfig {

	@Config.Comment("Zombies' Configs")
	public static final Zombies zombies = new Zombies();

	public static class Zombies {

		@Config.Comment("Magma Zombie")
		public Magma magma = new Magma();

		public static class Magma {

			@Config.Comment("Will this mob spawn lava upno death from the lava sword")
			public boolean lavaMaking = true;

			@Config.Comment("The fire sword will always make lava upon killing entities")
			public boolean alwaysLava = false;

			@Config.Comment("Should this mob get hurt from water or rain")
			public boolean wetDamage = true;

			@Config.Comment("Is the fire sword craftable")
			public boolean craftableSword = false;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 2, 4, "minecraft:desert", "minecraft:desert_hills", "minecraft:savanna_rock", "minecraft:mutated_desert",
					"minecraft:mutated_savanna", "minecraft:mutated_savanna_rock", "minecraft:hell");

			public static class Spawn {

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

		@Config.Comment("Doctor Zombie")
		public Doctor doctor = new Doctor();

		public static class Doctor {

			@Config.Comment("Should this mob extinguish burning zombies")
			public boolean shouldExtinguish = true;

			@Config.RangeDouble(min = 0.1D, max = 20D)
			@Config.Comment("The fire sword will always make lava upon killing entities")
			public double healAmount = 2D;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(50, 1, 2, "all");

			public static class Spawn {

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

		@Config.Comment("Worker Zombie")
		public Worker worker = new Worker();

		public static class Worker {

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(70, 1, 2, "minecraft:plains", "minecraft:swampland", "minecraft:forest_hills",
					"minecraft:smaller_extreme_hills", "minecraft:birch_forest_hills", "minecraft:redwood_taiga_hills", "minecraft:extreme_hills_with_trees",
					"minecraft:mesa", "minecraft:mesa_rock", "minecraft:mesa_clear_rock", "minecraft:mutated_extreme_hills", "minecraft:mutated_birch_forest_hills",
					"minecraft:mutated_redwood_taiga_hills", "minecraft:mutated_mesa", "minecraft:mutated_mesa_rock", "minecraft:mutated_mesa_clear_rock");

			public static class Spawn {

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

			@Config.Comment("Should this monster hammer the player into the ground "
					+ "(cancel this feature on skyblock maps)")
			public boolean hammerAttack = true;

		}

		@Config.Comment("Knight Zombie")
		public Knight knight = new Knight();

		public static class Knight {

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 2, 4, "all");

			public static class Spawn {

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

		@Config.Comment("Goro Zombie")
		public Goro goro = new Goro();

		public static class Goro {

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(40, 1, 2, "minecraft:extreme_hills", "minecraft:smaller_extreme_hills",
					"minecraft:extreme_hills_with_trees", "minecraft:mutated_extreme_hills", "minecraft:mutated_extreme_hills_with_trees");

			public static class Spawn {

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

		@Config.Comment("Ravenous Zombie")
		public Ravenous ravenous = new Ravenous();

		public static class Ravenous {

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(100, 1, 2, "minecraft:plains", "minecraft:forest_hills", "minecraft:mutated_plains",
					"minecraft:mutated_birch_forest_hills");

			@Config.Comment("Will this mob explode on death, "
					+ "The explosion will not affect blocks if mob griefing is off")
			public boolean explodeOnDeath = true;

			public static class Spawn {

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
