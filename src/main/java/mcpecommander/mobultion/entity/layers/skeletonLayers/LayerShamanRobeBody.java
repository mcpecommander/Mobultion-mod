package mcpecommander.mobultion.entity.layers.skeletonLayers;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.skeletons.EntityShamanSkeleton;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderShamanSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerShamanRobeBody implements LayerRenderer<EntityShamanSkeleton>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/shaman_skeleton_overlay_body.png");
    private final RenderShamanSkeleton renderer;
    private final ModelCraftStudioSon layerModel;

    public LayerShamanRobeBody(RenderShamanSkeleton entityRender)
    {
        this.renderer = entityRender;
        this.layerModel = (ModelCraftStudioSon) this.renderer.getMainModel();
    }

    @Override
	public void doRenderLayer(EntityShamanSkeleton entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	GlStateManager.pushMatrix();
    	GlStateManager.scale(1.05F, 1.55F, 1.1F);
    	GlStateManager.translate(0, -0.03, 0);
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
        return true;
    }
}