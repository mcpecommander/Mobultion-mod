package mcpecommander.mobultion.potion.potionTypes;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModPotions;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class PotionJokernessType extends PotionType{
	
	public PotionJokernessType(Potion potion) {
		super("type.jokerness", new PotionEffect[] {new PotionEffect(potion, 3600)});
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "jokerness_type"));
	}

}
