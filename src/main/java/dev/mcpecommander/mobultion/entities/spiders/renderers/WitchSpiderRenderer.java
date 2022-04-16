package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.mcpecommander.mobultion.entities.spiders.entities.WitchSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderEyesLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.WitchSpiderModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* Created by McpeCommander on 2021/06/18 */
public class WitchSpiderRenderer extends GeoEntityRenderer<WitchSpiderEntity> {

    public WitchSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WitchSpiderModel());
        this.addLayer(new SpiderEyesLayer<>(this, "witchspider"));
        this.shadowRadius = 0.7F;
    }

    @Override
    public void render(WitchSpiderEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        if(entity.isDeadOrDying()) this.shadowRadius = 0f;
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public void render(GeoModel model, WitchSpiderEntity animatable, float partialTicks, RenderType type, PoseStack matrixStackIn, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        renderEarly(animatable, matrixStackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);

        if (renderTypeBuffer != null) {
            vertexBuilder = renderTypeBuffer.getBuffer(type);
        }
        // Render all top level bones
        for (GeoBone group : model.topLevelBones) {
            renderRecursively(group, matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.pack(0,
                            OverlayTexture.v(animatable.hurtTime > 0)), red, green, blue, alpha);
        }
    }

    @Override
    protected float getDeathMaxRotation(WitchSpiderEntity entityLivingBaseIn) {
        return 0;
    }
}
