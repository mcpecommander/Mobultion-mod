package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.mcpecommander.mobultion.entities.spiders.entities.MagmaSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderEyesLayer;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderMagmaLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.MagmaSpiderModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* Created by McpeCommander on 2021/06/18 */
public class MagmaSpiderRenderer extends GeoEntityRenderer<MagmaSpiderEntity> {

    private float rotationAngle;

    public MagmaSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MagmaSpiderModel());
        this.shadowRadius = 0.7F;
        this.addLayer(new SpiderMagmaLayer(this));
        this.addLayer(new SpiderEyesLayer<>(this, "magmaspider"));
    }

    /**
     * The main render method with all of its glory. Has several parts in it that are called from other method such as
     * renderLate and renderEarly
     * @param model The geckolib model
     * @param animatable The entity being rendered
     * @param partialTicks The partial ticks which are used for interpolation at different fps
     * @param type The render type, which has its own method
     * @param matrixStackIn The pose stack which controls translation, rotation and scaling
     * @param renderTypeBuffer The render buffer which way over my pay grade
     * @param vertexBuilder The vertex builder that is used to render quads
     * @param packedLightIn The light level which has some magic number calculation in it
     * @param packedOverlayIn I have no idea what this is
     * @param red The red color channel
     * @param green The green color channel
     * @param blue The blue color channel
     * @param alpha The transparency channel
     */
    @Override
    public void render(GeoModel model, MagmaSpiderEntity animatable, float partialTicks, RenderType type,
                       PoseStack matrixStackIn, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
                       int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
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

    //A solution for the spider rotating after dying and ruining my particles.
    //A better explanation in dev.mcpecommander.mobultion.entities.skeletons.renderers.BaseSkeletonRenderer.java
    @Override
    protected void applyRotations(MagmaSpiderEntity entityLiving, PoseStack matrixStackIn, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        if (!entityLiving.isDeadOrDying()){
            this.rotationAngle = rotationYaw;
        }
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, this.rotationAngle, partialTicks);
    }

    /**
     * How much the entity rotates when it dies. The default is 90 degrees like lying on the ground dead.
     * @param entity The entity that is dying.
     * @return a float of the degrees that this entity rotates on death.
     */
    @Override
    protected float getDeathMaxRotation(MagmaSpiderEntity entity) {
        return 0;
    }
}