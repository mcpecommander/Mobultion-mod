package mcpecommander.mobultion.entity.renderer.skeletonsRenderer;

import mcpecommander.mobultion.entity.entities.skeletons.EntityMagmaArrow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderMagmaArrow extends RenderArrow<EntityMagmaArrow>{
    public static final ResourceLocation RES_TIPPED_ARROW = new ResourceLocation("textures/entity/projectiles/tipped_arrow.png");

    public RenderMagmaArrow(RenderManager manager)
    {
        super(manager);
    }

    @Override
	protected ResourceLocation getEntityTexture(EntityMagmaArrow entity)
    {
        return RES_TIPPED_ARROW;
    }

    public static class Factory implements IRenderFactory<EntityMagmaArrow> {

        @Override
        public Render<? super EntityMagmaArrow> createRenderFor(RenderManager manager) {
            return new RenderMagmaArrow(manager);
        }

    }
}
