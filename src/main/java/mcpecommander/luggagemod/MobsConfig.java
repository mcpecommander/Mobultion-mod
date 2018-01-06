package mcpecommander.luggagemod;

import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("mlm.spiders_config.title")
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
			@Config.Comment("How long (in ticks) is the withering effct going to be")
			public int witheringLength = 100;
			
		}
	}
	
	@Config.Comment("Skeletons' Configs")
	public static final Skeletons skeletons = new Skeletons();
	
	public static class Skeletons{
		
	}
}
