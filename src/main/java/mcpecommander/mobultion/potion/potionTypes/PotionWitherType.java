package mcpecommander.mobultion.potion.potionTypes;

import mcpecommander.mobultion.Reference;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class PotionWitherType extends PotionType{
	
	public PotionWitherType(Potion potion) {
		super("type.wither", new PotionEffect[] {new PotionEffect(potion, 3600)});
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "wither_type"));
	}

}
