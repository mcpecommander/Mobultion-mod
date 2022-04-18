package dev.mcpecommander.mobultion.entities.skeletons.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import dev.mcpecommander.mobultion.entities.skeletons.entities.MiniLightningEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

/* McpeCommander created on 28/08/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.renderers */
public class MiniLightningRenderer extends EntityRenderer<MiniLightningEntity> {

    public MiniLightningRenderer(EntityRendererProvider.Context rendererManager) {
        super(rendererManager);
    }

    //TODO: should probably fix this but the entity get rendered so rarely that it shouldn't be a problem for now.
    @Override
    public boolean shouldRender(@Nonnull MiniLightningEntity entity, @Nonnull Frustum clippingHelper, double x,
                                double y, double z) {
        return true;
    }

    /**
     * Gets called every client tick which can be up to the fps setting.
     * @param entity The entity being rendered.
     * @param entityYaw The rotation yaw of the entity.
     * @param partialTicks The partial ticks used to interpolate the 20 server ticks into the fps-based client ticks.
     * @param matrixStack The matrix stack instance used for poses, rotations and translations.
     * @param renderTypeBuffer The render buffer
     * @param packedLight The light level using Minecraft magic light numbers.
     */
    @Override
    public void render(@Nonnull MiniLightningEntity entity, float entityYaw, float partialTicks,
                       @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource renderTypeBuffer, int packedLight) {

        matrixStack.pushPose();
        //Rotate the whole thing constantly.
        matrixStack.mulPose(Vector3f.YP.rotation(entity.tickCount * 0.08f));
        //Set the render type to vanilla lightning and get the vertex builder.
        VertexConsumer ivertexbuilder = renderTypeBuffer.getBuffer(RenderType.lightning());
        Matrix4f matrix4f = matrixStack.last().pose();
        //Render a quad and then rotate by 30 degrees and repeat until a spiky circle is formed.
        for(int i = 0; i < 12; i++){
            quad(matrix4f, ivertexbuilder, entity.tickCount/20f);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(30));
        }
        matrixStack.popPose();
    }

    private static void quad(Matrix4f matrix4f, VertexConsumer vertexBuilder, float color) {
        //Front
        vertexBuilder.vertex(matrix4f, -0.6f, 0,    -0.6f).color(1 - color, color * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f, -0.6f, 0.4f, -0.6f).color(1 - color, color * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f,  0.6f, 0.4f, -0.6f).color(1 - color, color * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f,  0.6f, 0,    -0.6f).color(1 - color, color * 0.8f, 0.3f, 0.3F).endVertex();

        //Back
        vertexBuilder.vertex(matrix4f,  0.6f, 0,    -0.6f).color(1 - color, color * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f,  0.6f, 0.4f, -0.6f).color(1 - color, color * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f, -0.6f, 0.4f, -0.6f).color(1 - color, color * 0.8f, 0.3f, 0.3F).endVertex();
        vertexBuilder.vertex(matrix4f, -0.6f, 0,    -0.6f).color(1 - color, color * 0.8f, 0.3f, 0.3F).endVertex();
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull MiniLightningEntity p_110775_1_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}