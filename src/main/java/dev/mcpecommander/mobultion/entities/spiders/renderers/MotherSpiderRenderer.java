package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.entities.spiders.entities.MotherSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderEyesLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.MotherSpiderModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.renderers */
public class MotherSpiderRenderer extends GeoEntityRenderer<MotherSpiderEntity> {

    public MotherSpiderRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MotherSpiderModel());
        this.shadowRadius = 0.7F;
        this.addLayer(new SpiderEyesLayer<>(this, "motherspider"));
    }

    @Override
    public void render(GeoModel model, MotherSpiderEntity animatable, float partialTicks, RenderType type, MatrixStack matrixStackIn, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
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
    protected float getDeathMaxRotation(MotherSpiderEntity entityLivingBaseIn) {
        return 0;
    }
}
