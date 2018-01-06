package mcpecommander.luggagemod.entity.renderer.spidersRenderer;

import org.lwjgl.opengl.GL11;

import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.entity.entities.spiders.EntitySpiderEgg;
import mcpecommander.luggagemod.entity.model.ModelCraftStudioSon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderSpiderEgg extends RenderLivingBase<EntitySpiderEgg>{
	public ResourceLocation mobTexture = new ResourceLocation("mlm:textures/entity/blank.png");
	private static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "egg", 64, 32);

	protected RenderSpiderEgg(RenderManager renderManager) {
		super(renderManager, model, 0.5f);
	}
	
	@Override
	protected void renderModel(EntitySpiderEgg entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 1.3 - entitylivingbaseIn.getHealth()/35d, 0);
		GlStateManager.scale(0.1d + entitylivingbaseIn.getHealth()/50d, 0.1d + entitylivingbaseIn.getHealth()/50d, 0.1d + entitylivingbaseIn.getHealth()/50d);
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		GlStateManager.popMatrix();
	}
	
	@Override
	protected boolean canRenderName(EntitySpiderEgg entity) {
		return false;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySpiderEgg entity) {
		return mobTexture;
	}
	
	public static class Factory implements IRenderFactory<EntitySpiderEgg> {

        @Override
        public Render<? super EntitySpiderEgg> createRenderFor(RenderManager manager) {
            return new RenderSpiderEgg(manager);
        }

    }

}
