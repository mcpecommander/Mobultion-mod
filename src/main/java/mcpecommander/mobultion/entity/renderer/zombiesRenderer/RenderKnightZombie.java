package mcpecommander.mobultion.entity.renderer.zombiesRenderer;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.zombies.EntityKnightZombie;
import mcpecommander.mobultion.entity.layers.skeletonLayers.LayerCustomHeadCraftstudio;
import mcpecommander.mobultion.entity.layers.skeletonLayers.LayerHeldItemCraftStudio;
import mcpecommander.mobultion.entity.layers.zombieLayers.LayerZombieArmor;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderKnightZombie<T extends EntityKnightZombie> extends RenderLiving<T>
{
    private static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "zombie", 64, 64);
    
    public RenderKnightZombie(RenderManager manager) {
        super(manager, model, 0.5F);
        this.addLayer(new LayerHeldItemCraftStudio(this));
        this.addLayer(new LayerZombieArmor(this));
        this.addLayer(new LayerCustomHeadCraftstudio(model.getModelRendererFromName("Head")));
    }
    
    @Override
    protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks,
    		float netHeadYaw, float headPitch, float scaleFactor) {
    	GlStateManager.pushMatrix();
    	if(entitylivingbaseIn.isChild()){
    		GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scaleFactor, 0.0F);
    	}
    	super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
    	GlStateManager.popMatrix();
    	
    }
	
    @Override
	public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/knight_zombie.png");
    }

    public static class Factory<T extends EntityKnightZombie> implements IRenderFactory<T>
    {
        @Override
        public Render<? super T> createRenderFor(RenderManager manager) {
            return new RenderKnightZombie(manager);
        }
    }
}