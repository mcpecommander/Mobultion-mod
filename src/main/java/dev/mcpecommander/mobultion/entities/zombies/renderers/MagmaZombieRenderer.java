package dev.mcpecommander.mobultion.entities.zombies.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.entities.zombies.entities.MagmaZombieEntity;
import dev.mcpecommander.mobultion.entities.zombies.layers.ZombieMagmaLayer;
import dev.mcpecommander.mobultion.entities.zombies.models.MagmaZombieModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* McpeCommander created on 29/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.renderers */
public class MagmaZombieRenderer extends GeoEntityRenderer<MagmaZombieEntity> {

    public MagmaZombieRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MagmaZombieModel());
        this.shadowRadius = 0.5f;
        this.addLayer(new ZombieMagmaLayer(this));
    }

    @Override
    protected float getDeathMaxRotation(MagmaZombieEntity entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void render(GeoModel model, MagmaZombieEntity animatable, float partialTicks, RenderType type, MatrixStack matrixStackIn,
                       @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn,
                       int packedOverlayIn, float red, float green, float blue, float alpha) {
        renderEarly(animatable, matrixStackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);

        if (renderTypeBuffer != null) {
            vertexBuilder = renderTypeBuffer.getBuffer(type);
        }
        //Makes it that the entity is only rendered with a red overlay when hurt but not on death.
        renderRecursively(model.topLevelBones.get(0), matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.
                pack(OverlayTexture.u(0), OverlayTexture.v(animatable.hurtTime > 0)), red, green, blue, alpha);
    }
}
