package mcpecommander.luggagemod.entity.layers.skeletonLayers;

import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.entity.entities.skeletons.EntityShamanSkeleton;
import mcpecommander.luggagemod.entity.model.ModelCraftStudioSon;
import mcpecommander.luggagemod.entity.renderer.skeletonsRenderer.RenderShamanSkeleton;
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

    public void doRenderLayer(EntityShamanSkeleton entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	GlStateManager.pushMatrix();
    	GlStateManager.scale(1F, 1.55F, 1.2F);
    	this.layerModel.setModelAttributes(this.layerModel);
        this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.renderer.bindTexture(TEXTURE);
        this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.popMatrix();     
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}