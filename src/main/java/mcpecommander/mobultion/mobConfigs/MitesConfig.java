package mcpecommander.mobultion.mobConfigs;

import javax.annotation.Nullable;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.mobConfigs.EndermenConfig.Endermen;
import mcpecommander.mobultion.mobConfigs.EndermenConfig.Endermen.Magma;
import mcpecommander.mobultion.mobConfigs.EndermenConfig.Endermen.Magma.Spawn;
import net.minecraftforge.common.config.Config;

@Config(modid=Reference.MOD_ID, name="Mites", category="Mites")
public class MitesConfig {

	@Config.Comment("Mites' Configs")
	public static final Mites mites = new Mites();

	public static class Mites {

		@Config.Comment("WoodMite")
		public Wood wood = new Wood();

		public static class Wood {

			@Config.RequiresMcRestart
			@Config.Comment("Do you want to unregister this mob?")
			public boolean spawn = true;

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
