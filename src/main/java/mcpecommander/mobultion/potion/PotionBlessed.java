package mcpecommander.mobultion.potion;

import mcpecommander.mobultion.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionBlessed extends Potion {
	
	public PotionBlessed() {
		super(false, 0xF74BF7);
		this.setPotionName(Reference.MOD_ID + ":effect.blessed");
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "blessed_potion"));

	}
	
	@Override
	public void performEffect(EntityLivingBase e, int amplifier) {
		//System.out.println(e.world.isRemote);
		if (e != null && !e.isDead && e.world.isRemote) {
			for (int i = 0; i < 360; i += 20) {
				double x = Math.sin(i);
				double z = Math.cos(i);
				e.world.spawnParticle(EnumParticleTypes.END_ROD, e.posX + (x *.2), e.posY + e.height + .5, e.posZ + (z *.2), 0, 0, 0);
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration % 20 == 0;
	}
	
	

}
