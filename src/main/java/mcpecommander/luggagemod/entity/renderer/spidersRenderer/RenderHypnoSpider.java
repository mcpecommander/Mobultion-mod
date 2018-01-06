package mcpecommander.luggagemod.entity.renderer.spidersRenderer;

import javax.annotation.Nonnull;

import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.entity.entities.spiders.EntityHypnoSpider;
import mcpecommander.luggagemod.entity.layers.spiderLayers.LayerSpiderEyes;
import mcpecommander.luggagemod.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderHypnoSpider extends RenderLiving<EntityHypnoSpider>{

	public ResourceLocation mobTexture = new ResourceLocation("mlm:textures/entity/hypno_spider.png");
	public static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "hypno_spider", 64, 32);
	
	public RenderHypnoSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, model, 0.5F);
		this.addLayer(new LayerSpiderEyes(this));
		//this.addLayer(new LayerCustomHeadCraftstudio(model.getModelRendererFromName("Head")));
	}
	@Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityHypnoSpider entity) {
        return mobTexture;
    }
	
	

    public static class Factory implements IRenderFactory<EntityHypnoSpider> {

        @Override
        public Render<? super EntityHypnoSpider> createRenderFor(RenderManager manager) {
            return new RenderHypnoSpider(manager);
        }

    }


}
