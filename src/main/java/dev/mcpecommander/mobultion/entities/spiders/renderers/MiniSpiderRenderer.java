package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.mcpecommander.mobultion.entities.spiders.entities.MiniSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderEyesLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.MiniSpiderModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.renderers */
public class MiniSpiderRenderer extends GeoEntityRenderer<MiniSpiderEntity> {

    public MiniSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MiniSpiderModel());
        this.shadowRadius = 0.4F;
        this.addLayer(new SpiderEyesLayer<>(this, "minispider"));
    }

    /**
     * How much the entity rotates when it dies. The default is 90 degrees like lying on the ground dead.
     * @param entity The entity that is dying.
     * @return a float of the degrees that this entity rotates on death.
     */
    @Override
    protected float getDeathMaxRotation(MiniSpiderEntity entity) {
        return 0;
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
    public void render(GeoModel model, MiniSpiderEntity animatable, float partialTicks, RenderType type,
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

}
