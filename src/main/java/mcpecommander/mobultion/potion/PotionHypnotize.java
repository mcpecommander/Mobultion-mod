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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionHypnotize extends Potion {

	private static final ResourceLocation HYPNOTIZE = new ResourceLocation(Reference.MOD_ID, "textures/potion/hypnotize.png"); 

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
	
	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		render(x + 6, y + 7, 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
		render(x + 3, y + 3, alpha);
	}

	@SideOnly(Side.CLIENT)
	private void render(int x, int y, float alpha) {
		Minecraft.getMinecraft().renderEngine.bindTexture(HYPNOTIZE);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.color(1, 1, 1, alpha);

		buf.pos(x, (y + 18) , 0).tex(0, 1).endVertex();
		buf.pos(x + 18, y + 18, 0).tex(1,1).endVertex();
		buf.pos(x + 18, y, 0).tex(1,0).endVertex();
		buf.pos(x, y, 0).tex(0,0).endVertex();

		tessellator.draw();
	}

}
