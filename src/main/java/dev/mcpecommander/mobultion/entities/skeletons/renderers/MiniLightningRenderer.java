package dev.mcpecommander.mobultion.entities.skeletons.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.entities.skeletons.entities.MiniLightningEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

/* McpeCommander created on 28/08/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.renderers */
public class MiniLightningRenderer extends EntityRenderer<MiniLightningEntity> {

    public MiniLightningRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    public boolean shouldRender(@Nonnull MiniLightningEntity entity, @Nonnull ClippingHelper clippingHelper, double x,
                                double y, double z) {
        return true;
    }

    @Override
    public void render(@Nonnull MiniLightningEntity entity, float p_225623_2_, float p_225623_3_,
                       @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer renderTypeBuffer, int p_225623_6_) {

        matrixStack.pushPose();
        matrixStack.mulPose(Vector3f.YP.rotation(entity.tickCount * 0.2f));
        IVertexBuilder ivertexbuilder = renderTypeBuffer.getBuffer(RenderType.lightning());
        Matrix4f matrix4f = matrixStack.last().pose();

        for(int i = 0; i < 12; i++){
            quad(matrix4f, ivertexbuilder, entity.tickCount/20f);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(30));
        }
        matrixStack.popPose();


    }

    private static void quad(Matrix4f matrix4f, IVertexBuilder vertexBuilder, float height) {
        //Front
        vertexBuilder.vertex(matrix4f, -0.6f, 0,          -0.6f).color(1 - height, height * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f, -0.6f, 3 * height, -0.6f).color(1 - height, height * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f,  0.6f, 3 * height, -0.6f).color(1 - height, height * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f,  0.6f, 0,          -0.6f).color(1 - height, height * 0.8f, 0.3f, 0.3F).endVertex();

        //Back
        vertexBuilder.vertex(matrix4f,  0.6f, 0,          -0.6f).color(1 - height, height * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f,  0.6f, 3 * height, -0.6f).color(1 - height, height * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f, -0.6f, 3 * height, -0.6f).color(1 - height, height * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f, -0.6f, 0,          -0.6f).color(1 - height, height * 0.8f, 0.3f, 0.3F).endVertex();
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull MiniLightningEntity p_110775_1_) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}