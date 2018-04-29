package mcpecommander.mobultion.entity.layers.spiderLayers;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.spiders.EntityAngelSpider;
import mcpecommander.mobultion.entity.renderer.spidersRenderer.RenderAngelSpider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerAngelSpiderRing<T extends EntityAngelSpider> implements LayerRenderer<T>
{
    private static final ResourceLocation SPIDER_RING = new ResourceLocation(Reference.MOD_ID, "textures/entity/angel_ring.png");
    private final RenderAngelSpider renderAngelSpider;

    public LayerAngelSpiderRing(RenderAngelSpider renderAngelSpider)
    {
        this.renderAngelSpider = renderAngelSpider;
    }

    @Override
	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.renderAngelSpider.bindTexture(SPIDER_RING);
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
        GlStateManager.color(0.88F, 1F, 0.0F, 1F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.renderAngelSpider.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        i = entitylivingbaseIn.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
        this.renderAngelSpider.setLightmap(entitylivingbaseIn);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
	public boolean shouldCombineTextures()
    {
        return false;
    }
}
