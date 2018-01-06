package mcpecommander.luggagemod.init;

import mcpecommander.luggagemod.potion.PotionFreeze;
import mcpecommander.luggagemod.potion.PotionHypnotize;
import mcpecommander.luggagemod.potion.PotionJokerness;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModPotions {
	@GameRegistry.ObjectHolder("mlm:hypnotize_potion")
	public static PotionHypnotize potionHypnotize;
	@GameRegistry.ObjectHolder("mlm:freeze_potion")
	public static PotionFreeze potionFreeze;
	@GameRegistry.ObjectHolder("mlm:jokerness_potion")
	public static PotionJokerness potionJokerness;
	
	public static void init(){
		potionHypnotize = new PotionHypnotize();
		potionFreeze = new PotionFreeze();
		potionJokerness = new PotionJokerness();
	}
}
