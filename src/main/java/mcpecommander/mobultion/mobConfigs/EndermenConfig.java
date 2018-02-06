package mcpecommander.mobultion.mobConfigs;

import javax.annotation.Nullable;

import mcpecommander.mobultion.Reference;
import net.minecraft.init.Items;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID, category = "Endermen", name = "Endermen")
public class EndermenConfig {

	@Config.Comment("Endermen' Configs")
	public static final Endermen endermen = new Endermen();

	public static class Endermen {

		@Config.Comment("Magma Enderman")
		public Magma magma = new Magma();

		public static class Magma {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

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
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

//			@Config.Comment("Should this mob burn the target upon attack")
//			public boolean shouldIgnite = true;
//
//			@Config.RangeDouble(min = 3D, max = 1000D)
//			@Config.Comment("How far can this mob teleport to")
//			public double teleportDistance = 20D;
//			
//			@Config.RangeDouble(min = 2D, max = 100D)
//			@Config.Comment("Health")
//			public double health = 20D;

			@Config.RequiresMcRestart
			@Config.Comment("Spawn configuration")
			public Spawn spawnRates = new Spawn(70, 1, 1, "frozen_ocean", "frozen_river", "ice_flats", "ice_mountains", "cold_beach", "taiga_cold", "taiga_cold_hills", "mutated_ice_flats", "mutated_taiga_cold");
			
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
