package mcpecommander.mobultion.entity.renderer.endermenRenderer;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.endermen.EntityGardenerEnderman;
import mcpecommander.mobultion.entity.entities.endermen.EntityGlassShot;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderGlassShot <T extends EntityGlassShot> extends Render<T>{
	
	private static ModelCraftStudioSon model ;

	protected RenderGlassShot(RenderManager renderManager, ModelCraftStudioSon model) {
		super(renderManager);
		this.model = model;
	}
	
	private float rotLerp(float p_188347_1_, float p_188347_2_, float p_188347_3_)
    {
        float f;

        for (f = p_188347_2_ - p_188347_1_; f < -180.0F; f += 360.0F)
        {
            ;
        }

        while (f >= 180.0F)
        {
            f -= 360.0F;
        }

        return p_188347_1_ + p_188347_3_ * f;
    }
	
	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
        float f = this.rotLerp(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
        float f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        float f2 = (float)entity.ticksExisted + partialTicks;
        GlStateManager.translate((float)x, (float)y -2.2, (float)z);
        float f3 = 0.03125F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(3.5F, 3.5F, 3.5F);
        this.bindEntityTexture(entity);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        this.model.render(entity, 0.0F, 0.0F, 0.0F, f, f1, 0.03125F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return new ResourceLocation(Reference.MOD_ID, "textures/entity/glass_shot.png");
	}
	
	public static class Factory<T extends EntityGlassShot> implements IRenderFactory<T>
    {
        @Override
        public Render<? super T> createRenderFor(RenderManager manager) {
            return new RenderGlassShot(manager, new ModelCraftStudioSon(Reference.MOD_ID, "glass_shot", 16, 16));
        }
    }

}
