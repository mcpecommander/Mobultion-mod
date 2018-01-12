package mcpecommander.mobultion.entity.renderer.spidersRenderer;

import javax.annotation.Nonnull;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.spiders.EntityMagmaSpider;
import mcpecommander.mobultion.entity.layers.spiderLayers.LayerMagmaSpiderLava;
import mcpecommander.mobultion.entity.layers.spiderLayers.LayerSpiderEyes;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderMagmaSpider extends RenderLiving<EntityMagmaSpider>{

	public ResourceLocation mobTexture = new ResourceLocation(Reference.MOD_ID ,"textures/entity/magma_spider.png");
	public static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "magma_spider", 64, 32);
	
	public RenderMagmaSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, model, 0.5F);
		this.addLayer(new LayerSpiderEyes(this));
		this.addLayer(new LayerMagmaSpiderLava(this));
	}
	
	@Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityMagmaSpider entity) {
        return mobTexture;
    }
	
    public static class Factory implements IRenderFactory<EntityMagmaSpider> {

        @Override
        public Render<? super EntityMagmaSpider> createRenderFor(RenderManager manager) {
            return new RenderMagmaSpider(manager);
        }

    }


}
