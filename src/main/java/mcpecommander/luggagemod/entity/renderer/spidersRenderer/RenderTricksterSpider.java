package mcpecommander.luggagemod.entity.renderer.spidersRenderer;

import javax.annotation.Nonnull;

import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.entity.entities.spiders.EntityTricksterSpider;
import mcpecommander.luggagemod.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderTricksterSpider extends RenderLiving<EntityTricksterSpider>{

	public ResourceLocation mobTexture = new ResourceLocation("mlm:textures/entity/Magma_spider.png");
	
	public RenderTricksterSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCraftStudioSon(Reference.MOD_ID, "trickster_spider", 64, 32), 0.5F);
	}
	
	@Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityTricksterSpider entity) {
        return mobTexture;
    }
	
	

    public static class Factory implements IRenderFactory<EntityTricksterSpider> {

        @Override
        public Render<? super EntityTricksterSpider> createRenderFor(RenderManager manager) {
            return new RenderTricksterSpider(manager);
        }

    }


}