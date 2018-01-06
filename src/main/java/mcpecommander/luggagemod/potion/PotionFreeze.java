package mcpecommander.luggagemod.potion;

import mcpecommander.luggagemod.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PotionFreeze extends Potion{

	public PotionFreeze() {
		super(true, 0x9696F1);
		this.setPotionName("freeze");
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID,  "freeze_potion"));
	}
	
	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
		entityLivingBaseIn.motionX = 0;
		entityLivingBaseIn.motionY = 0;
		entityLivingBaseIn.motionZ = 0;
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}


}
