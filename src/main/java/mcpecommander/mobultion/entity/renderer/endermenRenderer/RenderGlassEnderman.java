package mcpecommander.mobultion.entity.renderer.endermenRenderer;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.endermen.EntityGlassEnderman;
import mcpecommander.mobultion.entity.layers.endermenLayers.LayerEndermanEyes;
import mcpecommander.mobultion.entity.layers.skeletonLayers.LayerCustomHeadCraftstudio;
import mcpecommander.mobultion.entity.layers.skeletonLayers.LayerHeldItemCraftStudio;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import mcpecommander.mobultion.mobConfigs.EndermenConfig;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderGlassEnderman<T extends EntityGlassEnderman> extends RenderLiving<T>
{
	private static final DynamicTexture TEXTURE_BRIGHTNESS = new DynamicTexture(16, 16);
    private static ModelCraftStudioSon model = new ModelCraftStudioSon(Reference.MOD_ID, "glass_enderman", 64, 32);
    private float r = 0f, g = 0f, b = 0f;
    private int mode = 0;
    
    public RenderGlassEnderman(RenderManager manager) {
        super(manager, model, 0.5F);
        this.addLayer(new LayerCustomHeadCraftstudio(model.getModelRendererFromName("Head")));
        this.addLayer(new LayerHeldItemCraftStudio(this));
        this.addLayer(new LayerEndermanEyes(this));
    }
    
    @Override
    protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks,
    		float netHeadYaw, float headPitch, float scaleFactor) {
    	GlStateManager.enableBlend();
	    	if(EndermenConfig.endermen.glass.rGB) {
	    	switch(mode) {
	    	case 0 : if(setColor(255, 0, 0)) mode = 1; // red
	    	break;
	    	case 1 : if(setColor(255, 255, 0)) mode = 2; // yellow
	    	break;
	    	case 2 : if(setColor(0, 255, 0)) mode = 3; // green
	    	break;
	    	case 3 : if(setColor(0, 255, 255)) mode = 4; // aqua
	    	break;
	    	case 4 : if(setColor(0, 0, 255)) mode = 5; // blue
	    	break;
	    	case 5 : if(setColor(80, 0, 80)) mode = 0; // purple
	    	break;
	    	}
	    	
	    	GlStateManager.color(r/255f, g/255f, b/255f);
    	}
    	super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
    	GlStateManager.disableBlend();
    }
    
    @Override
	protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
		String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());
		if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)))
        {
            GlStateManager.translate(0.0F, entityLiving.height + 0.1F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        }
		
	}
	
	@Override
	protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
		float f = entitylivingbaseIn.getBrightness();
        int i = this.getColorMultiplier((T) entitylivingbaseIn, f, partialTicks);
        boolean flag = (i >> 24 & 255) > 0;
        boolean flag1 = entitylivingbaseIn.hurtTime > 0;

        if (!flag && !flag1)
        {
            return false;
        }
        else if (!flag && !combineTextures)
        {
            return false;
        }
        else
        {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
            this.brightnessBuffer.position(0);

            if (flag1)
            {
                this.brightnessBuffer.put(1.0F);
                this.brightnessBuffer.put(0.0F);
                this.brightnessBuffer.put(0.0F);
                this.brightnessBuffer.put(0.3F);
            }
            else
            {
                float f1 = (i >> 24 & 255) / 255.0F;
                float f2 = (i >> 16 & 255) / 255.0F;
                float f3 = (i >> 8 & 255) / 255.0F;
                float f4 = (i & 255) / 255.0F;
                this.brightnessBuffer.put(f2);
                this.brightnessBuffer.put(f3);
                this.brightnessBuffer.put(f4);
                this.brightnessBuffer.put(1.0F - f1);
            }

            this.brightnessBuffer.flip();
            GlStateManager.glTexEnv(8960, 8705, this.brightnessBuffer);
            GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(TEXTURE_BRIGHTNESS.getGlTextureId());
            GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            return true;
        }
	}
    
    private boolean setColor(int red, int green, int blue) {
		if (r < red)
			r += 1;
		if (r > red)
			r -= 1;

		if (g < green)
			g += 1;
		if (g > green)
			g -= 1;

		if (b < blue)
			b += 1;
		if (b > blue) 
			b -= 1;
		
		if(r == red && g == green && b == blue) {
			return true;
		}
		return false;
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return EndermenConfig.endermen.glass.rGB ? new ResourceLocation(Reference.MOD_ID, "textures/entity/glass_enderman_test.png") : new ResourceLocation(Reference.MOD_ID, "textures/entity/glass_enderman.png");
    }

    public static class Factory<T extends EntityGlassEnderman> implements IRenderFactory<T>
    {
        @Override
        public Render<? super T> createRenderFor(RenderManager manager) {
            return new RenderGlassEnderman(manager);
        }
    }
    
    /*
     * if(entitylivingbaseIn.ticksExisted % 1 == 0) {
    		if(r < 1f && !bol) r += 0.01f;
    		if(r >= 1f && g < 1f) {
    			g += 0.01;
    			if(g >= 1f) {
    				bol = true;
    			}
    		}
    		if(bol) {
    			r-= 0.01f;
    			if(r <= 0.01f) {
    				bol = false;
    			}
    		}
    		if(r >= 1f && g >= 1f && b < 1f && !bol) {
    			b += 0.01f;
    		}
    	}
    	if(r >= 1f && g >= 1f && b >= 1f) {
    		r = 0.01f;
    		g = 0.01f;
    		b = 0.01f;
    	}
     */
}