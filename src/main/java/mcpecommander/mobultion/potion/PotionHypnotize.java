package mcpecommander.mobultion.potion;

import mcpecommander.mobultion.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionHypnotize extends Potion {

	public PotionHypnotize() {
		super(true, 0xF74BF7);
		this.setPotionName("hypnotize");
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "hypnotize_potion"));

	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
		if (entityLivingBaseIn != null && !entityLivingBaseIn.isDead) {
			if (entityLivingBaseIn.getRNG().nextInt(30) == 0) {
				double xV = 2 * entityLivingBaseIn.getRNG().nextDouble();
				double zV = 2 * entityLivingBaseIn.getRNG().nextDouble();
				entityLivingBaseIn.addVelocity((xV - 1) * ((double) amplifier + 1) / 2, 0.0D,
						(zV - 1) * ((double) amplifier + 1) / 2);
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
