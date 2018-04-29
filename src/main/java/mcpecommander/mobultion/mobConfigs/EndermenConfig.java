package mcpecommander.mobultion.mobConfigs;

import javax.annotation.Nullable;

import mcpecommander.mobultion.Reference;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Config(modid = Reference.MOD_ID, category = "mobs", name = "Mobultion/Endermen")
public class EndermenConfig {

	@Config.Comment("Endermen' Configs")
	public static final Endermen endermen = new Endermen();

	public static class Endermen {

		@Config.Comment("Magma Enderman")
		public Magma magma = new Magma();

		public static class Magma {

			@Config.Comment("Should this mob burn the target upon attack")
			public boolean shouldIgnite = true;

			@Config.RangeDouble(min = 3D, max = 1000D)
			@Config.Comment("How far can this mob teleport to")
			public double teleportDistance = 20D;
			
			@Config.RangeDouble(min = 2D, max = 100D)
			@Config.Comment("Health")
			public double health = 20D;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(70, 1, 1, "hell");
			
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
		
		@Config.Comment("Ice Enderman")
		public Ice ice = new Ice();

		public static class Ice {

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(70, 1, 1, "minecraft:frozen_ocean", "minecraft:frozen_river", "minecraft:ice_flats", "minecraft:ice_mountains", "minecraft:cold_beach", "minecraft:taiga_cold", "minecraft:taiga_cold_hills", "minecraft:mutated_ice_flats", "minecraft:mutated_taiga_cold");
			
			
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
		
		@Config.Comment("Wandering Enderman")
		public Wandering wandering = new Wandering();

		public static class Wandering {

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(70, 1, 1, "all");
			
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
		
		@Config.Comment("Gardener Enderman")
		public Gardener gardener = new Gardener();

		public static class Gardener {

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(85, 1, 1, "minecraft:plains", "minecraft:mutated_plains", "minecraft:mutated_forest");
			
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
		
		@Config.Comment("Glass Enderman")
		public Glass glass = new Glass();

		public static class Glass {

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(60, 1, 1, "minecraft:desert", "minecraft:desert_hills", "minecraft:beaches", "minecraft:mutated_desert");
			
			@Config.Comment("RGB colors instead of black")
			public boolean rGB = false;
			
			@Config.Comment("Chance for the ender glass shot to break")
			@Config.RangeDouble(min=0.0, max=1.0)
			public double breakChance = 0.5d;
			
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
