package dev.mcpecommander.mobultion.entities.zombies.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.mcpecommander.mobultion.entities.zombies.entities.WorkerZombieEntity;
import dev.mcpecommander.mobultion.entities.zombies.layers.HardHatLayer;
import dev.mcpecommander.mobultion.entities.zombies.layers.ItemHoldingLayer;
import dev.mcpecommander.mobultion.entities.zombies.models.WorkerZombieModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* McpeCommander created on 28/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.renderers */
public class WorkerZombieRenderer extends GeoEntityRenderer<WorkerZombieEntity> {

    public WorkerZombieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WorkerZombieModel());
        this.shadowRadius = 0.5f;
        this.addLayer(new ItemHoldingLayer<>(this));
        this.addLayer(new HardHatLayer(this));
    }

    @Override
    protected float getDeathMaxRotation(WorkerZombieEntity entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void render(GeoModel model, WorkerZombieEntity animatable, float partialTicks, RenderType type, PoseStack matrixStackIn,
                       @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                       int packedOverlayIn, float red, float green, float blue, float alpha) {
        renderEarly(animatable, matrixStackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);

        if (renderTypeBuffer != null) {
            vertexBuilder = renderTypeBuffer.getBuffer(type);
        }
        //Makes it that the entity is only rendered with a red overlay when hurt but not on death.
        renderRecursively(model.topLevelBones.get(0), matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.pack(0,
                        OverlayTexture.v(animatable.hurtTime > 0)), red, green, blue, alpha);
    }
}
