package mcpecommander.mobultion.potion.potionTypes;

import mcpecommander.mobultion.Reference;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class PotionHypnotizeType extends PotionType{
	
	public PotionHypnotizeType(Potion potion) {
		super("type.hypnotize", new PotionEffect[] {new PotionEffect(potion, 3600)});
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "hypnotize_type"));
	}

}
