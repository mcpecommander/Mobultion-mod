package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.entities.spiders.entities.MagmaSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderEyesLayer;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderMagmaLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.MagmaSpiderModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* Created by McpeCommander on 2021/06/18 */
public class MagmaSpiderRenderer extends GeoEntityRenderer<MagmaSpiderEntity> {

    public MagmaSpiderRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MagmaSpiderModel());
        this.shadowRadius = 0.7F;
        this.addLayer(new SpiderMagmaLayer(this));
        this.addLayer(new SpiderEyesLayer<>(this, "magmaspider"));
    }

    @Override
    protected void applyRotations(MagmaSpiderEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        if(entityLiving.isDeadOrDying()) return;
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

    @Override
    public void render(GeoModel model, MagmaSpiderEntity animatable, float partialTicks, RenderType type, MatrixStack matrixStackIn, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
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
    protected float getDeathMaxRotation(MagmaSpiderEntity entityLivingBaseIn) {
        return 0;
    }
}