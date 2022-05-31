package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.mcpecommander.mobultion.entities.spiders.entities.HypnoWaveEntity;
import dev.mcpecommander.mobultion.entities.spiders.models.HypnoWaveModel;
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
public class HypnoWaveRenderer extends GeoProjectilesRenderer<HypnoWaveEntity> {

    public HypnoWaveRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HypnoWaveModel());
    }

    @Override
    public void render(@NotNull HypnoWaveEntity entity, float entityYaw, float partialTicks,
                       @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        GeoModel model = getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(entity));
        matrixStackIn.pushPose();
        matrixStackIn.scale(1.5f, 1.5f, 1.5f);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180));
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
        matrixStackIn.popPose();

    }
}
