package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.mcpecommander.mobultion.entities.spiders.entities.WitheringWebEntity;
import dev.mcpecommander.mobultion.entities.spiders.models.WitheringWebModel;
import dev.mcpecommander.mobultion.utils.MathCalculations;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

/* Created by McpeCommander on 2021/06/18 */
public class WitheringWebRenderer extends GeoProjectilesRenderer<WitheringWebEntity> {

    public WitheringWebRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WitheringWebModel());
    }

    /**
     * Render the given entity, this is the bigger method that calls the actual render method after doing many calculations.
     * @param entity The entity to be rendered
     * @param entityYaw The yaw angle of the entity rotation
     * @param partialTicks The partial ticks that smoothen the transition between normal 20 ticks per second
     * @param matrixStackIn The pose stack that handles the rotation, transition and scale
     * @param bufferIn The buffer used to get the vertex builder and render types
     * @param packedLightIn The current light level
     */
    @Override
    public void render(@NotNull WitheringWebEntity entity, float entityYaw, float partialTicks,
                       @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        GeoModel model = getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(entity));
        matrixStackIn.pushPose();
        if(entity.getTarget() != null) {
            float scaleFactor = (float) (0.5f + MathCalculations.map(entity.distanceTo(entity.getTarget()),
                    entity.originalDistance, 0, 0, 1.5));
            matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
            //Copied the projectile render code to change this rotation from positive to negative. FML.
            matrixStackIn.mulPose(Vector3f.YN.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
            RenderSystem.setShaderTexture(0, getTextureLocation(entity));
            Color renderColor = getRenderColor(entity, partialTicks, matrixStackIn, bufferIn, null, packedLightIn);
            RenderType renderType = getRenderType(entity, partialTicks, matrixStackIn, bufferIn, null, packedLightIn,
                    getTextureLocation(entity));
            render(model, entity, partialTicks, renderType, matrixStackIn, bufferIn, null, packedLightIn,
                    getPackedOverlay(entity, 0), (float) renderColor.getRed() / 255f,
                    (float) renderColor.getGreen() / 255f, (float) renderColor.getBlue() / 255f,
                    (float) renderColor.getAlpha() / 255);
        }
        matrixStackIn.popPose();
    }

}
