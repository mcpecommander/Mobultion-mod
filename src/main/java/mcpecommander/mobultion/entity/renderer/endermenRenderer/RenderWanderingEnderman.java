package mcpecommander.mobultion.entity.renderer.endermenRenderer;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.endermen.EntityWanderingEnderman;
import mcpecommander.mobultion.entity.layers.endermenLayers.LayerEndermanEyes;
import mcpecommander.mobultion.entity.layers.skeletonLayers.LayerCustomHeadCraftstudio;
import mcpecommander.mobultion.entity.layers.skeletonLayers.LayerHeldItemCraftStudio;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderWanderingEnderman<T extends EntityWanderingEnderman> extends RenderLiving<T>
{
    private static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "wandering_enderman", 64, 64);
    
    public RenderWanderingEnderman(RenderManager manager) {
        super(manager, model, 0.5F);
        this.addLayer(new LayerCustomHeadCraftstudio(model.getModelRendererFromName("Head")));
        this.addLayer(new LayerHeldItemCraftStudio(this));
        this.addLayer(new LayerEndermanEyes(this));
    }
    
    @Override
    protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks,
    		float netHeadYaw, float headPitch, float scaleFactor) {
    	if(!entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()){
    		CSModelRenderer part = model.getModelRendererFromName("RightArm1");
			part.setInitialRotationMatrix(-70, 0, 0);
			CSModelRenderer part1 = model.getModelRendererFromName("RightArm");
			part1.setInitialRotationMatrix(-10, 0, 0);
    	}else{
    		CSModelRenderer part = model.getModelRendererFromName("RightArm1");
			part.setInitialRotationMatrix(0, 0, 0);
			CSModelRenderer part1 = model.getModelRendererFromName("RightArm");
			part1.setInitialRotationMatrix(0, 0, 0);
    	}
    	super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/wandering_enderman.png");
    }

    public static class Factory<T extends EntityWanderingEnderman> implements IRenderFactory<T>
    {
        @Override
        public Render<? super T> createRenderFor(RenderManager manager) {
            return new RenderWanderingEnderman(manager);
        }
    }
}