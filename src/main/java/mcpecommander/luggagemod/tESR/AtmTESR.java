package mcpecommander.luggagemod.tESR;

import org.lwjgl.opengl.GL11;

import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.tileEntity.TileEntityAtm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AtmTESR extends TileEntitySpecialRenderer<TileEntityAtm>{
	
	private IModel model;

	@Override
	public void renderTileEntityFast(TileEntityAtm te, double x, double y, double z, float partialTicks,
			int destroyStage, float partial, BufferBuilder buffer) {
		 	GlStateManager.pushAttrib();
	        GlStateManager.pushMatrix();

	        // Translate to the location of our tile entity
	        GlStateManager.translate(x, y, z);
	        GlStateManager.disableRescaleNormal();


	        // Render our item
	        renderItem(te);

	        GlStateManager.popMatrix();
	        GlStateManager.popAttrib();
	}



    private void renderItem(TileEntityAtm te) {
        ItemStack stack = te.getStack();
        if (!stack.isEmpty()) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translate(.5, 1.5, .5);
            GlStateManager.scale(.4f, .4f, .4f);
            long angle = (System.currentTimeMillis() / 10) % 360;
            GlStateManager.rotate(angle, 0, 1, 0);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }

}
