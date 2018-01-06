package mcpecommander.luggagemod.entity.renderer.spidersRenderer;

import javax.annotation.Nonnull;

import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.entity.entities.spiders.EntityMiniSpider;
import mcpecommander.luggagemod.entity.layers.spiderLayers.LayerMiniSpiderCustomHead;
import mcpecommander.luggagemod.entity.layers.spiderLayers.LayerSpiderEyes;
import mcpecommander.luggagemod.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderMiniSpider extends RenderLiving<EntityMiniSpider>{
	
	public ResourceLocation mobTexture = new ResourceLocation("mlm:textures/entity/mini_spider.png");
	private static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "mini_spider", 64, 32);
	
	public RenderMiniSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, model, 0.5F);
		this.addLayer(new LayerSpiderEyes(this));
		this.addLayer(new LayerMiniSpiderCustomHead(model.getModelRendererFromName("Head")));
	}
	
	@Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityMiniSpider entity) {
        return mobTexture;
    }
	
    public static class Factory implements IRenderFactory<EntityMiniSpider> {

        @Override
        public Render<? super EntityMiniSpider> createRenderFor(RenderManager manager) {
            return new RenderMiniSpider(manager);
        }

    }
}
