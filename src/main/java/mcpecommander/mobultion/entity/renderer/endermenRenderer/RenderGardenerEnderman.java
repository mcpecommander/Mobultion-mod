package mcpecommander.mobultion.entity.renderer.endermenRenderer;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.endermen.EntityGardenerEnderman;
import mcpecommander.mobultion.entity.layers.endermenLayers.LayerEndermanEyes;
import mcpecommander.mobultion.entity.layers.endermenLayers.LayerEndermanTongue;
import mcpecommander.mobultion.entity.layers.skeletonLayers.LayerCustomHeadCraftstudio;
import mcpecommander.mobultion.entity.layers.skeletonLayers.LayerHeldItemCraftStudio;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderGardenerEnderman<T extends EntityGardenerEnderman> extends RenderLiving<T>
{
    private static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "gardener_enderman", 64, 32);
    
    public RenderGardenerEnderman(RenderManager manager) {
        super(manager, model, 0.5F);
        this.addLayer(new LayerCustomHeadCraftstudio(model.getModelRendererFromName("Head")));
        this.addLayer(new LayerHeldItemCraftStudio(this));
        this.addLayer(new LayerEndermanEyes(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/gardener_enderman.png");
    }

    public static class Factory<T extends EntityGardenerEnderman> implements IRenderFactory<T>
    {
        @Override
        public Render<? super T> createRenderFor(RenderManager manager) {
            return new RenderGardenerEnderman(manager);
        }
    }
}