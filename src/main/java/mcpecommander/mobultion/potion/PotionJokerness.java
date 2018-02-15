package mcpecommander.mobultion.potion;

import java.util.Random;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionJokerness extends Potion {
	
	private static final ResourceLocation JOKERNESS = new ResourceLocation(Reference.MOD_ID, "textures/potion/jokerness.png"); 
	protected static final ResourceLocation TEX2 = new ResourceLocation(Reference.MOD_ID, "textures/entity/white.png");
	float alpha;
	boolean flag = true;
	int rand1, rand2, rand4, rand5, rand7, rand8, rand10, rand11;
	float rand3, rand6, rand9, rand12;

	public PotionJokerness() {
		super(true, 0x75CA50);
		this.setPotionName(Reference.MOD_ID + ":effect.jokerness");
		this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "jokerness_potion"));
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
		if (entityLivingBaseIn instanceof EntityPlayer
				&& entityLivingBaseIn.isPotionActive(ModPotions.potionJokerness)) {
			this.isReady = true;
		}
	}

	public boolean isReady;

	@SideOnly(Side.CLIENT)
	public void render(Minecraft minecraft, ScaledResolution scaledRes, Random RNG) {
		if (minecraft.gameSettings.thirdPersonView == 0) {
			if (!Minecraft.getMinecraft().isGamePaused()) {
				Gui ingameGui = Minecraft.getMinecraft().ingameGUI;
				minecraft.getTextureManager().bindTexture(TEX2);
				if (flag) {
					rand1 = RNG.nextInt(scaledRes.getScaledWidth() - 16);
					rand2 = RNG.nextInt(scaledRes.getScaledHeight() - 16);
					rand3 = RNG.nextFloat();
					rand4 = RNG.nextInt(scaledRes.getScaledWidth() - 16);
					rand5 = RNG.nextInt(scaledRes.getScaledHeight() - 16);
					rand6 = RNG.nextFloat();
					rand7 = RNG.nextInt(scaledRes.getScaledWidth() - 16);
					rand8 = RNG.nextInt(scaledRes.getScaledHeight() - 16);
					rand9 = RNG.nextFloat();
					rand10 = RNG.nextInt(scaledRes.getScaledWidth() - 16);
					rand11 = RNG.nextInt(scaledRes.getScaledHeight() - 16);
					rand12 = RNG.nextFloat();
					flag = false;
					alpha = 0.99f;
				}
				GlStateManager.enableAlpha();
				GlStateManager.color(rand3, 0F, 0F, alpha -= 0.02f);
				ingameGui.drawModalRectWithCustomSizedTexture(0 + rand1, 0 + rand2, 0, 0, 16, 16, 16, 64);
				GlStateManager.color(rand6, 0F, 0F, alpha);
				ingameGui.drawModalRectWithCustomSizedTexture(0 + rand4, 0 + rand5, 0, 16, 16, 16, 16, 64);
				GlStateManager.color(rand9, 0F, 0F, alpha);
				ingameGui.drawModalRectWithCustomSizedTexture(0 + rand7, 0 + rand8, 0, 32, 16, 16, 16, 64);
				GlStateManager.color(rand12, 0F, 0F, alpha);
				ingameGui.drawModalRectWithCustomSizedTexture(0 + rand10, 0 + rand11, 0, 48, 16, 16, 16, 64);
				GlStateManager.disableAlpha();
				if (alpha <= 0) {
					flag = true;
				}
			}
		}
		GlStateManager.color(1f, 1f, 1f, 1f);
	}
	// Failed attempts

	// if (!Minecraft.getMinecraft().isGamePaused() && RNG.nextInt(10) == 0) {
	// Gui ingameGui = Minecraft.getMinecraft().ingameGUI;
	// minecraft.getTextureManager().bindTexture(TEX2);
	// GlStateManager.color(RNG.nextFloat(), 0F, 0F, 0.40F + (RNG.nextFloat() *
	// 0.6F));
	// ingameGui.drawModalRectWithCustomSizedTexture(0 +
	// RNG.nextInt(scaledRes.getScaledWidth()),
	// 0 + RNG.nextInt(scaledRes.getScaledHeight()), 0, (RNG.nextInt(3) * 16),
	// 16, 16, 16, 64);
	// }

	// if (!Minecraft.getMinecraft().isGamePaused()) {
	// Gui ingameGui = Minecraft.getMinecraft().ingameGUI;
	// minecraft.getTextureManager().bindTexture(TEX2);
	// int rand1 = RNG.nextInt(scaledRes.getScaledWidth());
	// int rand2 = RNG.nextInt(scaledRes.getScaledHeight());
	// int rand3 = (RNG.nextInt(3) * 16);
	// GlStateManager.color(RNG.nextFloat(), 0F, 0F, 0.40F + 0.2f);
	// ingameGui.drawModalRectWithCustomSizedTexture(0 + rand1, 0 + rand2, 0,
	// rand3, 16, 16, 16, 64);
	// GlStateManager.color(RNG.nextFloat(), 0F, 0F, 0.40F + 0.4f);
	// ingameGui.drawModalRectWithCustomSizedTexture(0 + (rand1 +=
	// (RNG.nextBoolean() ? 10 : -10)), 0 + (rand2 += (RNG.nextBoolean() ? 10 :
	// -10)), 0, rand3, 16, 16, 16, 64);
	// GlStateManager.color(RNG.nextFloat(), 0F, 0F, 0.40F + 0.6f);
	// ingameGui.drawModalRectWithCustomSizedTexture(0 + (rand1 +=
	// (RNG.nextBoolean() ? 10 : -10)), 0 + (rand2 += (RNG.nextBoolean() ? 10 :
	// -10)), 0, rand3, 16, 16, 16, 64);
	// GlStateManager.color(RNG.nextFloat(), 0F, 0F, 0.40F + 0.8f);
	// ingameGui.drawModalRectWithCustomSizedTexture(0 + (rand1 +=
	// (RNG.nextBoolean() ? 10 : -10)), 0 + (rand2 += (RNG.nextBoolean() ? 10 :
	// -10)), 0, rand3, 16, 16, 16, 64);
	// GlStateManager.color(RNG.nextFloat(), 0F, 0F, 0.40F + 1f);
	// ingameGui.drawModalRectWithCustomSizedTexture(0 + (rand1 +=
	// (RNG.nextBoolean() ? 10 : -10)), 0 + (rand2 += (RNG.nextBoolean() ? 10 :
	// -10)), 0, rand3, 16, 16, 16, 64);
	// }

	// Gui ingameGui = Minecraft.getMinecraft().ingameGUI;
	// minecraft.getTextureManager().bindTexture(TEX2);
	// --count;
	//
	// if(flag){
	// rand1 = RNG.nextInt(scaledRes.getScaledWidth());
	// rand2 = RNG.nextInt(scaledRes.getScaledHeight());
	// alpha = 0.99f;
	// flag = false;
	// }else{
	// GlStateManager.color(RNG.nextFloat(), 0F, 0F, alpha);
	// if(count <= 0){
	// if(rand1 > scaledRes.getScaledWidth()){
	// right = false;
	// }else if(rand1 < 0){
	// right = true;
	// }
	// if(right){
	// rand1 +=5;
	// }else{
	// rand1 -=5;
	// }
	//
	// if(rand2 > scaledRes.getScaledHeight()){
	// down = false;
	// }else if(rand2 < 0){
	// down = true;
	// }
	// if(down){
	// rand2 +=5;
	// }else{
	// rand2 -=5;
	// }
	// }
	// if(count <= 0){
	// count = 3;
	// }
	// rand3 = (RNG.nextInt(3) * 16);
	// ingameGui.drawModalRectWithCustomSizedTexture(0 + rand1, 0 + rand2, 0,
	// rand3, 16, 16, 16, 64);
	// GlStateManager.color(RNG.nextFloat(), 0F, 0F, alpha - 0.2f);
	// ingameGui.drawModalRectWithCustomSizedTexture(0 + rand1 + (right ? -16 :
	// 16), 0 + rand2 + (down ? -16 : 16), 0, rand3, 16, 16, 16, 64);
	// GlStateManager.color(RNG.nextFloat(), 0F, 0F, alpha - 0.4f);
	// ingameGui.drawModalRectWithCustomSizedTexture(0 + rand1 + (right ? -32 :
	// 32), 0 + rand2 + (down ? -32 : 32), 0, rand3, 16, 16, 16, 64);
	// GlStateManager.color(RNG.nextFloat(), 0F, 0F, alpha - 0.6f);
	// ingameGui.drawModalRectWithCustomSizedTexture(0 + rand1 + (right ? -48 :
	// 48), 0 + rand2 + (down ? -48 : 48), 0, rand3, 16, 16, 16, 64);
	// GlStateManager.color(RNG.nextFloat(), 0F, 0F, alpha - 0.8f);
	// ingameGui.drawModalRectWithCustomSizedTexture(0 + rand1 + (right ? -64 :
	// 64), 0 + rand2 + (down ? -64 : 64), 0, rand3, 16, 16, 16, 64);
	//
	// }

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
		Minecraft.getMinecraft().renderEngine.bindTexture(JOKERNESS);
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
