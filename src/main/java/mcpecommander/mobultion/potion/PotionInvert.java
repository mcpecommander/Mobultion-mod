package mcpecommander.mobultion.potion;

import mcpecommander.mobultion.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionInvert extends Potion{

	boolean flag = false;
	public PotionInvert() {
		super(true, 0xF74BF7);
		this.setPotionName(Reference.MOD_ID + ":effect.invert");
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "invert_potion"));
	}
	
	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
//		if(entityLivingBaseIn instanceof EntityPlayerSP && !flag){
//			Minecraft.getMinecraft().entityRenderer.loadEntityShader(new EntityEnderman(entityLivingBaseIn.world));
//			flag = true;
//		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
