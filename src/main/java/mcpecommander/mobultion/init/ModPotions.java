package mcpecommander.mobultion.init;

import mcpecommander.mobultion.potion.PotionBlessed;
import mcpecommander.mobultion.potion.PotionFreeze;
import mcpecommander.mobultion.potion.PotionHypnotize;
import mcpecommander.mobultion.potion.PotionInvert;
import mcpecommander.mobultion.potion.PotionJokerness;
import mcpecommander.mobultion.potion.PotionVomit;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModPotions {
	@GameRegistry.ObjectHolder("mobultion:hypnotize_potion")
	public static PotionHypnotize potionHypnotize;
	@GameRegistry.ObjectHolder("mobultion:freeze_potion")
	public static PotionFreeze potionFreeze;
	@GameRegistry.ObjectHolder("mobultion:jokerness_potion")
	public static PotionJokerness potionJokerness;
	@GameRegistry.ObjectHolder("mobultion:vomit_potion")
	public static PotionVomit potionVomit;
	@GameRegistry.ObjectHolder("mobultion:blessed_potion")
	public static PotionBlessed potionBlessed;
	@GameRegistry.ObjectHolder("mobultion:invert_potion")
	public static PotionInvert potionInvert;
	
}
