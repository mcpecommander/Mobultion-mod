package mcpecommander.mobultion.entity.renderer.endermenRenderer;

import org.apache.logging.log4j.Level;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.endermen.EntityEnderProjectile;
import mcpecommander.mobultion.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEnderProjectile <T extends EntityEnderProjectile> extends Render<T>
{

    public RenderEnderProjectile(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        this.bindTexture(getEntityTexture(entity));

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        //System.out.println(entity.getType());
        switch(entity.getType()) {
    	case (byte)0 :
    		Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.enderBlaze), ItemCameraTransforms.TransformType.GROUND);
    	break;
    	case (byte)1 : 
    		Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.enderFlake), ItemCameraTransforms.TransformType.GROUND);
    	break;
    	case (byte)2 : 
    		Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.enderGlassShot), ItemCameraTransforms.TransformType.GROUND);
    		break;
    	default:
    		MobultionMod.logger.log(Level.FATAL, "If this is printed, there is a serious problem in " + Reference.MOD_ID
    				+ ":" + this.toString() + " and you need to contact the mod author");
    		break;
    	}

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity)
    {
    	switch(entity.getType()) {
    	case (byte)0 :
    		return new ResourceLocation(Reference.MOD_ID, "textures/items/ender_blaze.png");
    	case (byte)1 : 
    		return new ResourceLocation(Reference.MOD_ID, "textures/items/ender_flake.png");
    	case (byte)2 : 
    		return new ResourceLocation(Reference.MOD_ID, "textures/items/ender_glass_shot.png");
    	}
    	
    	MobultionMod.logger.log(Level.FATAL, "If this is printed, there is a serious problem in " + Reference.MOD_ID
				+ ":" + this.toString() + " and you need to contact the mod author");
        return new ResourceLocation(Reference.MOD_ID, "textures/items/ender_blaze.png");
    }
    
    public static class Factory<T extends EntityEnderProjectile> implements IRenderFactory<T>
    {
        @Override
        public Render<? super T> createRenderFor(RenderManager manager) {
            return new RenderEnderProjectile(manager);
        }
    }
}