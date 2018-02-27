package mcpecommander.mobultion.potion.potionTypes;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModPotions;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class PotionStrongHypnotizeType extends PotionType{
	
	public PotionStrongHypnotizeType(Potion potion) {
		super("type.strong_hypnotize", new PotionEffect[] {new PotionEffect(potion, 1800, 1)});
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "strong_hypnotize_type"));
	}

}
