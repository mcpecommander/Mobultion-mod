package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.*;
import dev.mcpecommander.mobultion.entities.spiders.entities.RedEyeEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.RedEyeLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.RedEyeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Random;

/* Created by McpeCommander on 2021/06/18 */
public class RedEyeRenderer extends GeoEntityRenderer<RedEyeEntity> {

    GeoModel model;
    GeoBone eye;

    public RedEyeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RedEyeModel());
        this.layerRenderers.add(new RedEyeLayer(this));
    }

    /**
     * Render the given entity, this is the bigger method that calls the actual render method after doing many calculations.
     * @param entityIn The entity to be rendered
     * @param entityYaw The yaw angle of the entity rotation
     * @param partialTicks The partial ticks that smoothen the transition between normal 20 ticks per second
     * @param stack The pose stack that handles the rotation, transition and scale
     * @param bufferIn The buffer used to get the vertex builder and render types
     * @param packedLightIn The current light level
     */
    @Override
    public void render(@NotNull RedEyeEntity entityIn, float entityYaw, float partialTicks,
                       @NotNull PoseStack stack, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        //Set the model and eye bone
        if(model == null) {
            model = getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(entityIn));
            eye = model.topLevelBones.get(0).childBones.get(0).getName().equals("eye") ?
                    model.topLevelBones.get(0).childBones.get(0) : model.topLevelBones.get(0).childBones.get(1);
        }
        stack.pushPose();
        //Scale the model up a bit
        stack.scale(1.2f, 1.2f, 1.2f);
        super.render(entityIn, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
        //Don't continue rendering if the entity is dying
        if(entityIn.isDeadOrDying()) return;
        //When the entity has a target and is attacking, draw "thunder" lines.
        stack.pushPose();
        if(entityIn.getTarget() != null && entityIn.isAggressive()) {
            for (int i = 0; i < 4; i++) {
                line(stack, bufferIn.getBuffer(RenderType.lines()), getWorldPosFromModel(entityIn, entityYaw, partialTicks, eye)
                                .subtract(entityIn.getPosition(partialTicks)).subtract(0, 1, 0),
                        entityIn.getTarget().getPosition(partialTicks).add(0, entityIn.getTarget().getBbHeight()/2d, 0)
                                .subtract(entityIn.getPosition(partialTicks)),
                        entityIn.getRandom());
            }
        }
        stack.popPose();
    }

    /**
     * The main render method with all of its glory. Has several parts in it that are called from other method such as
     * renderLate and renderEarly
     * @param model The geckolib model
     * @param animatable The entity being rendered
     * @param partialTicks The partial ticks which are used for interpolation at different fps
     * @param type The render type, which has its own method
     * @param stack The pose stack which controls translation, rotation and scaling
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
    public void render(GeoModel model, RedEyeEntity animatable, float partialTicks, RenderType type, PoseStack stack,
                       @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                       int packedOverlayIn, float red, float green, float blue, float alpha) {
        renderEarly(animatable, stack, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);

        if (renderTypeBuffer != null) {
            vertexBuilder = renderTypeBuffer.getBuffer(type);
        }
        renderLate(animatable, stack, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);
        // Render all top level bones
        renderRecursively(model.topLevelBones.get(0), stack, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);
        //Don't continue rendering if the entity is dying
        if(animatable.isDeadOrDying()) return;
        //Render the scan line
        if(!animatable.isLaunching() && animatable.getTarget() != null && !animatable.isAggressive()) {
            stack.pushPose();
            matrixStackFromModel(stack, eye);
            stack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(animatable.tickCount % 80 / 80f * Mth.TWO_PI) * 25));
            assert renderTypeBuffer != null;
            quad(stack.last().pose(), renderTypeBuffer.getBuffer(RenderType.lightning()));
            stack.popPose();
        }
    }

    /**
     * Draws a specific scan plane with fixed sizes.
     * @param matrix4f The pose matrix that has the current rotation, scale and position
     * @param vertexConsumer The vertex builder used to add vertices to build according to the render type.
     */
    private static void quad(Matrix4f matrix4f, VertexConsumer vertexConsumer) {
        //Front
        vertexConsumer.vertex(matrix4f, 0f, 0, 0f).color(0.8f, 0.3f, 0.3f, 0.3F).endVertex();
        vertexConsumer.vertex(matrix4f, 0f, 0, 0f).color(0.8f, 0.3f, 0.3f, 0.3F).endVertex();
        vertexConsumer.vertex(matrix4f, 1.6f, 0, -3f).color(0.5f, 0.3f, 0.3f, 0.1F).endVertex();
        vertexConsumer.vertex(matrix4f, -1.6f, 0, -3f).color(0.5f, 0.3f, 0.3f, 0.1F).endVertex();

        //Back
        vertexConsumer.vertex(matrix4f, 0f, 0, 0f).color(0.8f, 0.3f, 0.3f, 0.3F).endVertex();
        vertexConsumer.vertex(matrix4f, 0f, 0, 0f).color(0.8f, 0.3f, 0.3f, 0.3F).endVertex();
        vertexConsumer.vertex(matrix4f, -1.6f, 0, -3f).color(0.5f, 0.3f, 0.3f, 0.1F).endVertex();
        vertexConsumer.vertex(matrix4f, 1.6f, 0, -3f).color(0.5f, 0.3f, 0.3f, 0.1F).endVertex();
    }

    /**
     * Draws a line made of 3 parts from one position to another with some randomness added to each connection point.
     * @param stack The pose stack which handles rotation, position and scale.
     * @param vertexConsumer The vertices builder.
     * @param startPosition The start vector from where to start rendering the line.
     * @param endPosition The end position to where end the line.
     * @param random A random variable for adding the random positions.
     */
    private static void line(PoseStack stack, VertexConsumer vertexConsumer, Vec3 startPosition, Vec3 endPosition, Random random){
        Matrix4f matrix4f = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        //Calculate the first connection point at 1/3 of the distance to the target, plus some randomness.
        Vec3 firstThird = endPosition.subtract(startPosition).scale(0.3).add(startPosition).add(random.nextGaussian() * 0.1 - 0.05,
                random.nextGaussian() * 0.1 - 0.05,
                random.nextGaussian() * 0.1 - 0.05);
        //Calculate the second connection point at 2/3 of the distance to the target (or half the distance from the first
        // connection point to the target), plus some randomness.
        Vec3 secondThird = endPosition.subtract(firstThird).scale(0.5).add(firstThird).add(random.nextGaussian() * 0.3 - 0.15,
                random.nextGaussian() * 0.3 - 0.15,
                random.nextGaussian() * 0.3 - 0.15);
        //Draw the line from the eye to the first connection point.
        vertexConsumer.vertex(matrix4f, (float) startPosition.x, (float) startPosition.y, (float) startPosition.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();
        vertexConsumer.vertex(matrix4f, (float) firstThird.x, (float) firstThird.y, (float) firstThird.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();
        //Draw the line from the first connection point to the second connection point.
        vertexConsumer.vertex(matrix4f, (float) firstThird.x, (float) firstThird.y, (float) firstThird.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();
        vertexConsumer.vertex(matrix4f, (float) secondThird.x, (float) secondThird.y, (float) secondThird.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();
        //Draw the line from the second connection point to the target.
        vertexConsumer.vertex(matrix4f, (float) secondThird.x, (float) secondThird.y, (float) secondThird.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();
        vertexConsumer.vertex(matrix4f, (float) endPosition.x, (float) endPosition.y, (float) endPosition.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();
    }

    //Copied from MowziesMobs https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/render/RenderUtils.java
    private static void matrixStackFromModel(PoseStack matrixStack, GeoBone geoBone) {
        GeoBone parent = geoBone.parent;
        if (parent != null) matrixStackFromModel(matrixStack, parent);
        translateRotateGeckolib(geoBone, matrixStack);
    }

    //Copied from MowziesMobs https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/render/RenderUtils.java
    private static void translateRotateGeckolib(GeoBone bone, PoseStack matrixStackIn) {
        matrixStackIn.translate(bone.rotationPointX / 16.0F, bone.rotationPointY / 16.0F, bone.rotationPointZ / 16.0F);
        if (bone.getRotationZ() != 0.0F) {
            matrixStackIn.mulPose(Vector3f.ZP.rotation(bone.getRotationZ()));
        }

        if (bone.getRotationY() != 0.0F) {
            matrixStackIn.mulPose(Vector3f.YP.rotation(bone.getRotationY()));
        }

        if (bone.getRotationX() != 0.0F) {
            matrixStackIn.mulPose(Vector3f.XP.rotation(bone.getRotationX()));
        }

        matrixStackIn.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
    }

    //Copied from MowziesMobs https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/render/RenderUtils.java
    private static Vec3 getWorldPosFromModel(Entity entity, float entityYaw, float partialTicks, GeoBone geoBone) {
        PoseStack matrixStack = new PoseStack();
        matrixStack.translate(entity.getPosition(partialTicks).x, entity.getPosition(partialTicks).y, entity.getPosition(partialTicks).z);
        matrixStack.mulPose(new Quaternion(0, -entityYaw + 180, 0, true));
        matrixStack.scale(-1, -1, 1);
        matrixStack.translate(0, -1.5f, 0);
        matrixStackFromModel(matrixStack, geoBone);
        PoseStack.Pose matrixEntry = matrixStack.last();
        Matrix4f matrix4f = matrixEntry.pose();

        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix4f);
        return new Vec3(vec.x(), vec.y(), vec.z());
    }



}
