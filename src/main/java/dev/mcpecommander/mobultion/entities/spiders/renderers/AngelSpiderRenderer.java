package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.mcpecommander.mobultion.entities.spiders.entities.AngelSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.AngelRingLayer;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderEyesLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.AngelSpiderModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import javax.annotation.Nullable;

/* Created by McpeCommander on 2021/06/18 */
public class AngelSpiderRenderer extends GeoEntityRenderer<AngelSpiderEntity> {

    public AngelSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AngelSpiderModel());
        this.shadowRadius = 0.7F;
        this.addLayer(new SpiderEyesLayer<>(this, "angelspider"));
        this.addLayer(new AngelRingLayer(this));
    }

    @Override
    protected float getDeathMaxRotation(AngelSpiderEntity entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void render(GeoModel model, AngelSpiderEntity animatable, float partialTicks, RenderType type, PoseStack stack,
                       @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                       int packedOverlayIn, float red, float green, float blue, float alpha) {
        renderEarly(animatable, stack, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);

        if (renderTypeBuffer != null) {
            vertexBuilder = renderTypeBuffer.getBuffer(type);
        }
        if(animatable.isDeadOrDying()) {
            stack.pushPose();
            stack.translate(0, animatable.deathTime/35f, 0);
        }
        //Makes it that the entity is only rendered with a red overlay when hurt but not on death.
        for (GeoBone group : model.topLevelBones) {
            renderRecursively(animatable, group, stack, vertexBuilder, packedLightIn, OverlayTexture.pack(0,
                            OverlayTexture.v(animatable.hurtTime > 0)), red, green, blue, alpha);
        }
        if(animatable.isDeadOrDying()) stack.popPose();
    }

    public void renderRecursively(AngelSpiderEntity animateble, GeoBone bone, PoseStack stack, VertexConsumer bufferIn,
                                  int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        stack.pushPose();
        RenderUtils.translate(bone, stack);
        RenderUtils.moveToPivot(bone, stack);
        RenderUtils.rotate(bone, stack);
        RenderUtils.scale(bone, stack);
        RenderUtils.moveBackFromPivot(bone, stack);

        if (!bone.isHidden) {
            for (GeoCube cube : bone.childCubes) {
                stack.pushPose();
                //A quick and dirty way to render a transparent model once dead.
                if(bone.getName().equals("All") && animateble.isDeadOrDying()){
                    renderCube(cube, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, 1f - animateble.deathTime/35f);
                }else{
                    renderCube(cube, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                }
                stack.popPose();
            }
            for (GeoBone childBone : bone.childBones) {
                //A quick and dirty way to render a transparent model once dead but exclude the ring.
                if(!childBone.getName().equals("Rings") && animateble.isDeadOrDying()){
                    renderRecursively(animateble, childBone, stack, bufferIn, packedLightIn, packedOverlayIn,
                            red, green, blue, 1f - animateble.deathTime/35f);
                }else{
                    renderRecursively(animateble, childBone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                }

            }
        }

        stack.popPose();
    }

    @Override
    public RenderType getRenderType(AngelSpiderEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
                                    int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(textureLocation);
    }
}
