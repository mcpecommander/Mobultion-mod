package mcpecommander.mobultion.entity.renderer.spidersRenderer;

import javax.annotation.Nonnull;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.spiders.EntityAngelSpider;
import mcpecommander.mobultion.entity.layers.spiderLayers.LayerAngelSpiderRing;
import mcpecommander.mobultion.entity.layers.spiderLayers.LayerSpiderEyes;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderAngelSpider extends RenderLiving<EntityAngelSpider>{

	public ResourceLocation mobTexture = new ResourceLocation(Reference.MOD_ID ,"textures/entity/angel_spider.png");
	public static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "angel_spider", 64, 32);
	
	public RenderAngelSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, model, 0.5F);
		this.addLayer(new LayerSpiderEyes(this));
		this.addLayer(new LayerAngelSpiderRing(this));
	}
	
	@Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityAngelSpider entity) {
        return mobTexture;
    }

    public static class Factory implements IRenderFactory<EntityAngelSpider> {

        @Override
        public Render<? super EntityAngelSpider> createRenderFor(RenderManager manager) {
            return new RenderAngelSpider(manager);
        }

    }


}
