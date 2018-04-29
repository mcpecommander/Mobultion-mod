package mcpecommander.mobultion.mobConfigs;

import mcpecommander.mobultion.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid=Reference.MOD_ID, name="Mobultion/Mites", category="mobs")
public class MitesConfig {

	@Config.Comment("Mites' Configs")
	public static final Mites mites = new Mites();

	public static class Mites {

		@Config.Comment("WoodMite")
		public Wood wood = new Wood();

		public static class Wood {

			@Config.Comment("Should this mob take double damage from fire")
			public boolean doubleBurn = true;

			@Config.RangeInt(min = 1, max = 10000)
			@Config.Comment("Chance to spawn from breaking a log(1 out of this number)")
			public int spawnChance = 1000;
			
			@Config.RangeDouble(min = 2D, max = 100D)
			@Config.Comment("Health")
			public double health = 10D;

		}
		
	}
}
