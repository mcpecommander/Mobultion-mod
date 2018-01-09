package mcpecommander.mobultion.entity.renderer.skeletonsRenderer;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.skeletons.EntityHeartArrow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderHeartArrow extends RenderArrow<EntityHeartArrow>{
    public static final ResourceLocation arrow = new ResourceLocation(Reference.MOD_ID, "textures/entity/heart_arrow.png");

    public RenderHeartArrow(RenderManager manager)
    {
        super(manager);
    }

    protected ResourceLocation getEntityTexture(EntityHeartArrow entity)
    {
        return arrow;
    }

    public static class Factory implements IRenderFactory<EntityHeartArrow> {

        @Override
        public Render<? super EntityHeartArrow> createRenderFor(RenderManager manager) {
            return new RenderHeartArrow(manager);
        }

    }
}
