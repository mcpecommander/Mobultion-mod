package mcpecommander.mobultion.entity.renderer.mites;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.mites.EntityWoodMite;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderWoodMite <T extends EntityWoodMite> extends RenderLiving<T>
{
    private static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "woodmite", 64, 32);
    
    public RenderWoodMite(RenderManager manager) {
        super(manager, model, 0.1F);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/woodmite.png");
    }

    public static class Factory<T extends EntityWoodMite> implements IRenderFactory<T>
    {
        @Override
        public Render<? super T> createRenderFor(RenderManager manager) {
            return new RenderWoodMite(manager);
        }
    }
}