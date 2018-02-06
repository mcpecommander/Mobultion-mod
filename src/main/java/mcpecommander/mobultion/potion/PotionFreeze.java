package mcpecommander.mobultion.potion;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionFreeze extends Potion{
	
	private static final ResourceLocation FREEZE = new ResourceLocation(Reference.MOD_ID, "textures/potion/freeze.png"); 

	public boolean isReady;
	public float mul = 0.0f;
	float temp;
	
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
		
		if (entityLivingBaseIn instanceof EntityPlayer
				&& entityLivingBaseIn.isPotionActive(ModPotions.potionFreeze)) {
			
			if(!isReady){
				temp = 0.98f / (float)entityLivingBaseIn.getActivePotionEffect(ModPotions.potionFreeze).getDuration();
			}
			this.isReady = true;
			this.mul += temp/2.5f;
		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	public void renderEffect(RenderGameOverlayEvent.Post e, float mul){
		GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);	        
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.99f - (mul));
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/entity/freeze.png"));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, (double)e.getResolution().getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos((double)e.getResolution().getScaledWidth(), (double)e.getResolution().getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos((double)e.getResolution().getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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
		Minecraft.getMinecraft().renderEngine.bindTexture(FREEZE);
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
