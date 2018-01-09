package mcpecommander.mobultion;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("mmm.spiders_config.title")
public class MobsConfig {
	
	@Config.Comment("Spiders' Configs")
	public static final Spiders spiders = new Spiders();

	public static class Spiders{
		
		@Config.Comment("Angel Spider config")
		public Angel angel = new Angel();
		
		public static class Angel {
			
			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;
			
			@Config.RangeInt(min=0 , max=16)
			@Config.Comment("how much the Angel spider heals")
			public float healAmount = 4f;
			
			@Config.Comment("Where this spider can spawn")
			public String[] spawnLocations = {"extreme_hills", "ice_mountains", "cold_beach", "mutated_ice_flats"};
		
		}
		
		@Config.Comment("Hypno Spider config")
		public Hypno hypno = new Hypno();
		
		public static class Hypno {
			
			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;
			
			@Config.RangeInt(min=20 , max=600)
			@Config.Comment("how much time (in ticks) does it take for the mob to fire again")
			public int fireDelay = 100;
			
			@Config.Comment("Where this spider can spawn")
			public String[] spawnLocations = {"swampland", "mushroom_island", "mushroom_island_shore", "taiga_hills", "mesa", "mesa_rock", "mesa_clear_rock", "mutated_desert", "mutated_swampland", "mutated_mesa", "mutated_mesa_rock", "mutated_mesa_clear_rock"};
		}
		
		@Config.Comment("Magma Spider config")
		public Magma magma = new Magma();
		
		public static class Magma {
			
			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;
			
			@Config.RangeInt(min=20 , max=1000)
			@Config.Comment("How much is the chance (1/this number) to spawn the spider as a spider jockey")
			public int jockeyChance = 80;
			
			@Config.RangeInt(min=1 , max=10)
			@Config.Comment("How long (in seconds) is it going burn (plus a random based on the diffculty)")
			public int burningLength = 3;
			
			@Config.Comment("Should the mob be damaged if wet (raining or in water")
			public boolean waterDamage = true;
			
			@Config.Comment("Where this spider can spawn")
			public String[] spawnLocations = {"desert", "desert_hills", "mutated_desert"};
			
			@Config.Comment("Should this spider spawn on the nether fortress")			
			public boolean netherFortressSpawn = true;
		}
		
		@Config.Comment("Mini Spider config")
		public Mini mini = new Mini();
		
		public static class Mini {
			
			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;
			
			@Config.RangeDouble(min=0.1 , max=1)
			@Config.Comment("how fast is this mob (a zombie is 0.23)")
			public double speed = 0.52D;
			
		}
		
		@Config.Comment("Mother Spider config")
		public Mother mother = new Mother();
		
		public static class Mother {
			
			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;
			
			@Config.RangeInt(min=20 , max=1000)
			@Config.Comment("How long (in ticks) does this mob take to lay an egg")
			public int pregnancyTime = 80;
			
			@Config.RangeInt(min=20 , max=1000)
			@Config.Comment("How long (in ticks) does the egg take to spawn a spider")
			public int eggHatchingTime = 100;
			
			@Config.Comment("Where this spider can spawn")
			public String[] spawnLocations = {"extreme_hills", "smaller_extreme_hills", "mutated_extreme_hills", "mutated_extreme_hills_with_trees"};
		}
		
		@Config.Comment("Sorcerer Spider config")
		public Sorcerer sorcerer = new Sorcerer();
		
		public static class Sorcerer {
			
			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;
			
			@Config.RangeInt(min=10 , max=100)
			@Config.Comment("How long (in ticks) does this mob take to cast its spell")
			public int castingTime = 40;
			
			@Config.RangeInt(min=1 , max=6)
			@Config.Comment("Spawning the spiders based on the diffculty (1 and 2 will spawn vanilla spiders only,"
					+ " 6 will spawn this mod's spiders too)")
			public int mobsDiffculty = 6;
			
			@Config.Comment("Where this spider can spawn")
			public String[] spawnLocations = { "forest", "forest_hills", "jungle", "jungle_hills", "jungle_edge",
					"birch_forest", "birch_forest_hills", "roofed_forest", "mutated_forest", "mutated_jungle",
					"mutated_birch_forest", "mutated_roofed_forest" };
		}
		
		@Config.Comment("Speedy Spider config")
		public Speedy speedy = new Speedy();
		
		public static class Speedy {
			
			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;
			
			@Config.RangeInt(min=20 , max=1000)
			@Config.Comment("How much is the chance (1/this number) to spawn the spider as a spider jockey")
			public int jockeyChance = 200;
			
			@Config.RangeDouble(min=0.35, max=1)
			@Config.Comment("How fast is this mob (a zombie is 0.23")
			public double speed = 0.62D;
			
			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = {"ice_flats", "plains", "beaches", "mutated_plains", "mutated_ice_flats"};
			
		}
		
		@Config.Comment("Wither Spider config")
		public Wither wither = new Wither();
		
		public static class Wither {
			
			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;
			
			@Config.RangeInt(min=20 , max=1000)
			@Config.Comment("How much is the chance (1/this number) to spawn the spider as a spider jockey")
			public int jockeyChance = 200;
			
			@Config.RangeInt(min=10, max=1000)
			@Config.Comment("How long (in ticks) is the withering eeffct going to be")
			public int witheringLength = 100;
			
			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = {"sky", "stone_beach"};
			
		}
	}
	
	@Config.Comment("Skeletons' Configs")
	public static final Skeletons skeletons = new Skeletons();
	
	public static class Skeletons{
		
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

			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = { "desert", "desert_hills", "savanna_rock", "mutated_desert", "mutated_savanna", "mutated_savanna_rock" };
			
			@Config.Comment("If this skeleton should spawn"
					+ " from the desert temple loot chest (base chance is 2%)")
			public boolean spawnFromLootChests = true;

		}
		
		public Joker joker = new Joker();
		
		public static class Joker{

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

			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = { "mutated_plains", "mutated_desert", "mutated_extreme_hills",
					"mutated_forest", "mutated_taiga", "mutated_swampland", "mutated_ice_flats", "mutated_jungle",
					"mutated_jungle_edge", "mutated_birch_forest", "mutated_birch_forest_hills",
					"mutated_roofed_forest", "mutated_taiga_cold", "mutated_redwood_taiga",
					"mutated_redwood_taiga_hills", "mutated_extreme_hills_with_trees", "mutated_savanna",
					"mutated_savanna_rock", "mutated_mesa", "mutated_mesa_rock", "mutated_mesa_clear_rock" };

		}
		
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

			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = { "desert", "desert_hills", "savanna_rock", "mutated_desert", "mutated_savanna", "mutated_savanna_rock", "hell" };

		}
		
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
			public double wandDropChance = 0.0885d;

			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = { "extreme_hills", "forest", "swampland", "mushroom_island",
					"mushroom_island_shore", "jungle", "forest_hills", "jungle_hills", "jungle_edge", "birch_forest",
					"birch_forest_hills", "roofed_forest", "mutated_forest", "mutated_jungle", "mutated_swampland",
					"mutated_birch_forest" };

		}
		
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

			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = { "forest", "birch_forest", "roofed_forest", "birch_forest_hills",
					"mutated_forest", "mutated_birch_forest", "mutated_roofed_forest" };

		}
		
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

			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = { "sky" };

		}
	}
	
	@Config.Comment("Zombies' Configs")
	public static final Zombies zombies = new Zombies();
	
	public static class Zombies{
		
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

			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = { "desert", "desert_hills", "savanna_rock", "mutated_desert", "mutated_savanna", "mutated_savanna_rock", "hell" };

		}
		
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

			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = { "all", "m" };

		}
		
		public Worker worker = new Worker();
		
		public static class Worker {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = { "extreme_hills", "smaller_extreme_hills", "extreme_hills_with_trees", "mutated_extreme_hills", "mutated_extreme_hills_with_trees" };

		}
		
		public Knight knight = new Knight();
		
		public static class Knight {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = { "extreme_hills", "smaller_extreme_hills", "extreme_hills_with_trees", "mutated_extreme_hills", "mutated_extreme_hills_with_trees" };

		}
		
		public Goro goro = new Goro();
		
		public static class Goro {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

			@Config.Comment("Where this will spawn")
			public String[] spawnLocations = { "extreme_hills", "smaller_extreme_hills", "extreme_hills_with_trees", "mutated_extreme_hills", "mutated_extreme_hills_with_trees" };

		}
	}
}
