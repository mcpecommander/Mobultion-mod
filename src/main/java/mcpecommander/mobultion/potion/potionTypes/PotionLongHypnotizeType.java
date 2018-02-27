package mcpecommander.mobultion.potion.potionTypes;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModPotions;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class PotionLongHypnotizeType extends PotionType{
	
	public PotionLongHypnotizeType(Potion potion) {
		super("type.long_hypnotize", new PotionEffect[] {new PotionEffect(potion, 7200)});
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "long_hypnotize_type"));
	}

}
