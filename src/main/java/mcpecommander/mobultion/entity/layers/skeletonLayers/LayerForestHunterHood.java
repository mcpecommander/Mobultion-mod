package mcpecommander.mobultion.entity.layers.skeletonLayers;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.skeletons.EntitySniperSkeleton;
import mcpecommander.mobultion.entity.renderer.skeletonsRenderer.RenderSniperSkeleton;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerForestHunterHood implements LayerRenderer<EntitySniperSkeleton>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/forest_skeleton_overlay.png");
    private final RenderSniperSkeleton renderer;
    private final ModelBiped layerModel;

    public LayerForestHunterHood(RenderSniperSkeleton entityRender)
    {
        this.renderer = entityRender;
        this.layerModel = (ModelBiped) this.renderer.getMainModel();
    }

    @Override
	public void doRenderLayer(EntitySniperSkeleton entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	GlStateManager.pushMatrix();
    	GlStateManager.scale(1.01F, 1.01F, 1.01F);
    	this.layerModel.setModelAttributes(this.layerModel);
        this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        int color = BiomeColorHelper.getGrassColorAtPos(entitylivingbaseIn.world, entitylivingbaseIn.getPosition());
        if(color != -1){
        	double d0 = (color >> 16 & 255) / 255.0D;
            double d1 = (color >> 8 & 255) / 255.0D;
            double d2 = (color >> 0 & 255) / 255.0D;
            GlStateManager.color((float)d0, (float)d1, (float)d2, .99F);
        }else{
        	GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
        
        this.renderer.bindTexture(TEXTURE);
        this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        
        
    }

    @Override
	public boolean shouldCombineTextures()
    {
        return true;
    }
}