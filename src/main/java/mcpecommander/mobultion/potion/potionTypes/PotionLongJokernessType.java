package mcpecommander.mobultion.potion.potionTypes;

import mcpecommander.mobultion.Reference;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class PotionLongJokernessType extends PotionType{
	
	public PotionLongJokernessType(Potion potion) {
		super("type.long_jokerness", new PotionEffect[] {new PotionEffect(potion, 7200)});
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "long_jokerness_type"));
	}
	
	

}
