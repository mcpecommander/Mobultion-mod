package mcpecommander.mobultion.entity.layers.endermenLayers;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.endermen.EntityAnimatedEnderman;
import mcpecommander.mobultion.entity.entities.endermen.EntityWanderingEnderman;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerEndermanEyes<T extends EntityAnimatedEnderman> implements LayerRenderer<T>
{
    private static final ResourceLocation EYES = new ResourceLocation(Reference.MOD_ID, "textures/entity/enderman_eyes.png");
    private static final ResourceLocation EYES1 = new ResourceLocation(Reference.MOD_ID, "textures/entity/enderman_eyes_1.png");
    private final RenderLiving renderLiving;

    public LayerEndermanEyes(RenderLiving renderLiving)
    {
        this.renderLiving = renderLiving;
    }

    @Override
	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	if(entitylivingbaseIn instanceof EntityWanderingEnderman) {
    		this.renderLiving.bindTexture(EYES1);
    	}else {
    		this.renderLiving.bindTexture(EYES);
    	}
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
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
        GlStateManager.color(1F, 1F, 1F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.renderLiving.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        i = entitylivingbaseIn.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
        this.renderLiving.setLightmap(entitylivingbaseIn);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    @Override
	public boolean shouldCombineTextures()
    {
        return false;
    }
}
