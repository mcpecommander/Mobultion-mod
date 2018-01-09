package mcpecommander.mobultion.entity.renderer.spidersRenderer;

import javax.annotation.Nonnull;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.spiders.EntityWitherSpider;
import mcpecommander.mobultion.entity.layers.spiderLayers.LayerSpiderEyes;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderWitherSpider extends RenderLiving<EntityWitherSpider>{
	
	public ResourceLocation mobTexture = new ResourceLocation(Reference.MOD_ID ,"textures/entity/wither_spider.png");
	public static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "wither_spider", 64, 32);
	
	public RenderWitherSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, model, 0.5F);
		this.addLayer(new LayerSpiderEyes(this));
	}

	@Override
	protected void renderModel(EntityWitherSpider entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		if(entitylivingbaseIn.getHealth() < 20f){
			((ModelCraftStudioSon) this.getMainModel()).getModelRendererFromName("Head1").showModel = false;
			((ModelCraftStudioSon) this.getMainModel()).getModelRendererFromName("HeadBase1").showModel = false;
		}
		if(entitylivingbaseIn.getHealth() < 10f){
			((ModelCraftStudioSon) this.getMainModel()).getModelRendererFromName("Head2").showModel = false;
			((ModelCraftStudioSon) this.getMainModel()).getModelRendererFromName("HeadBase2").showModel = false;
		}
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
	}

	
	@Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityWitherSpider entity) {
        return mobTexture;
    }
	
    public static class Factory implements IRenderFactory<EntityWitherSpider> {

        @Override
        public Render<? super EntityWitherSpider> createRenderFor(RenderManager manager) {
            return new RenderWitherSpider(manager);
        }

    }
}
