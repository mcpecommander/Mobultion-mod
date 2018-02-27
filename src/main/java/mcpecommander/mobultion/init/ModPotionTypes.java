package mcpecommander.mobultion.init;

import mcpecommander.mobultion.potion.potionTypes.PotionHypnotizeType;
import mcpecommander.mobultion.potion.potionTypes.PotionJokernessType;
import mcpecommander.mobultion.potion.potionTypes.PotionLongHypnotizeType;
import mcpecommander.mobultion.potion.potionTypes.PotionLongJokernessType;
import mcpecommander.mobultion.potion.potionTypes.PotionStrongHypnotizeType;
import mcpecommander.mobultion.potion.potionTypes.PotionWitherType;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModPotionTypes {
	
	@GameRegistry.ObjectHolder("mobultion:jokerness_type")
	public static PotionJokernessType potionJokernessType;
	@GameRegistry.ObjectHolder("mobultion:long_jokerness_type")
	public static PotionLongJokernessType potionLongJokernessType;
	@GameRegistry.ObjectHolder("mobultion:hypnotize_type")
	public static PotionHypnotizeType potionHypnotizeType;
	@GameRegistry.ObjectHolder("mobultion:long_hypnotize_type")
	public static PotionLongHypnotizeType potionLongHypnotizeType;
	@GameRegistry.ObjectHolder("mobultion:strong_hypnotize_type")
	public static PotionStrongHypnotizeType potionStrongHypnotizeType;
	@GameRegistry.ObjectHolder("mobultion:wither_type")
	public static PotionWitherType potionWitherType;

}
