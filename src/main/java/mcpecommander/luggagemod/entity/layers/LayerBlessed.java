package mcpecommander.luggagemod.entity.layers;

import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.entity.model.ModelCraftStudioSon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class LayerBlessed<T extends ModelCraftStudioSon> implements LayerRenderer<EntityLiving> {

	private RenderLiving renderer;
	private ModelCraftStudioSon model;
    private static final ResourceLocation RING = new ResourceLocation(Reference.MOD_ID, "textures/entity/angel_ring.png");
    
	public LayerBlessed(RenderLiving renderer, ModelCraftStudioSon model) {
		this.renderer = renderer;
		this.model = model;
		
	}
	
	@Override
	public void doRenderLayer(EntityLiving entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		model.setModelAttributes(this.renderer.getMainModel());
        model.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
		this.renderer.bindTexture(RING);
		GlStateManager.translate(0, -entitylivingbaseIn.height, 0);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.renderer.bindTexture(RING);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

        if (entitylivingbaseIn.isInvisible())
        {
            GlStateManager.depthMask(false);
        }
        else
        {
            GlStateManager.depthMask(true);
        }

        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color(0.88F, 1F, 0.0F, 1F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        i = entitylivingbaseIn.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        this.renderer.setLightmap(entitylivingbaseIn);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
