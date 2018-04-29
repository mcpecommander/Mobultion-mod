package mcpecommander.mobultion.entity.layers.skeletonLayers;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.skeletons.EntityVampireSkeleton;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderVampireSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerVampireSuit implements LayerRenderer<EntityVampireSkeleton>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/vampire_overlay.png");
    private final RenderVampireSkeleton renderer;
    private final ModelCraftStudioSon layerModel;

    public LayerVampireSuit(RenderVampireSkeleton entityRender)
    {
        this.renderer = entityRender;
        this.layerModel = (ModelCraftStudioSon) this.renderer.getMainModel();
    }

    @Override
	public void doRenderLayer(EntityVampireSkeleton entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	GlStateManager.pushMatrix();
    	GlStateManager.scale(1.01F, 1.01F, 1.01F);
    	GlStateManager.translate(0, -0.01, 0);
    	this.layerModel.setModelAttributes(this.layerModel);
        this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.renderer.bindTexture(TEXTURE);
        this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.popMatrix();     
    }

    @Override
	public boolean shouldCombineTextures()
    {
        return false;
    }
}