package mcpecommander.mobultion.entity.renderer.spidersRenderer;

import javax.annotation.Nonnull;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.spiders.EntityMotherSpider;
import mcpecommander.mobultion.entity.layers.spiderLayers.LayerSpiderEyes;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderMotherSpider extends RenderLiving<EntityMotherSpider>{

	public ResourceLocation mobTexture = new ResourceLocation(Reference.MOD_ID ,"textures/entity/mother_spider.png");
	public static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "mother_spider", 64, 32);
	
	public RenderMotherSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, model, 0.5F);
		this.addLayer(new LayerSpiderEyes(this));
	}
	
	@Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityMotherSpider entity) {
        return mobTexture;
    }
	
    public static class Factory implements IRenderFactory<EntityMotherSpider> {

        @Override
        public Render<? super EntityMotherSpider> createRenderFor(RenderManager manager) {
            return new RenderMotherSpider(manager);
        }

    }


}
