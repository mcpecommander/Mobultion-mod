package mcpecommander.mobultion.entity.renderer.spidersRenderer;

import javax.annotation.Nonnull;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.spiders.EntitySorcererSpider;
import mcpecommander.mobultion.entity.layers.spiderLayers.LayerSorcererSpiderEyes;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderSorcererSpider extends RenderLiving<EntitySorcererSpider>{
	
	public ResourceLocation mobTexture = new ResourceLocation(Reference.MOD_ID ,"textures/entity/sorcerer_spider.png");
	private static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "sorcerer_spider", 64, 64);
	
	public RenderSorcererSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, model, 0.5F);
		this.addLayer(new LayerSorcererSpiderEyes(this));
	}
	
	@Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntitySorcererSpider entity) {
        return mobTexture;
    }

    public static class Factory implements IRenderFactory<EntitySorcererSpider> {

        @Override
        public Render<? super EntitySorcererSpider> createRenderFor(RenderManager manager) {
            return new RenderSorcererSpider(manager);
        }

    }
}
