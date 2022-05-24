package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.*;
import dev.mcpecommander.mobultion.entities.spiders.entities.RedEyeEntity;
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
    }

    @Override
    public void render(@NotNull RedEyeEntity entityIn, float entityYaw, float partialTicks,
                       @NotNull PoseStack stack, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.scale(1.2f, 1.2f, 1.2f);
        super.render(entityIn, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();

        stack.pushPose();
        if(model == null) {
            model = getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(entityIn));
            eye = model.topLevelBones.get(0).childBones.get(0).getName().equals("eye") ?
                    model.topLevelBones.get(0).childBones.get(0) : model.topLevelBones.get(0).childBones.get(1);
        }
        if(entityIn.getTarget() != null) {
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
        //Render the scan line
        if(!animatable.isLaunching()) {
            GeoBone eye = model.topLevelBones.get(0).childBones.get(0).getName().equals("eye") ?
                    model.topLevelBones.get(0).childBones.get(0) : model.topLevelBones.get(0).childBones.get(1);
            stack.pushPose();
            assert renderTypeBuffer != null;
            matrixStackFromModel(stack, eye);
            stack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(animatable.tickCount % 80 / 80f * Mth.TWO_PI) * 25));
            quad(stack.last().pose(), renderTypeBuffer.getBuffer(RenderType.lightning()));
            stack.popPose();
        }

        //Matrix3f matrix3f = pPoseStack.last().normal();
    }

    private static void quad(Matrix4f matrix4f, VertexConsumer vertexConsumer) {
        //Front
        vertexConsumer.vertex(matrix4f, 0f, 0, 0f).color(0.8f, 0.3f, 0.3f, 0.3F).endVertex();
        vertexConsumer.vertex(matrix4f, 0f, 0, 0f).color(0.8f, 0.3f, 0.3f, 0.3F).endVertex();
        vertexConsumer.vertex(matrix4f, 1.6f, 0, -3f).color(0.5f, 0.3f, 0.3f, 0.3F).endVertex();
        vertexConsumer.vertex(matrix4f, -1.6f, 0, -3f).color(0.5f, 0.3f, 0.3f, 0.3F).endVertex();

        //Back
        vertexConsumer.vertex(matrix4f, 0f, 0, 0f).color(0.8f, 0.3f, 0.3f, 0.3F).endVertex();
        vertexConsumer.vertex(matrix4f, 0f, 0, 0f).color(0.8f, 0.3f, 0.3f, 0.3F).endVertex();
        vertexConsumer.vertex(matrix4f, -1.6f, 0, -3f).color(0.5f, 0.3f, 0.3f, 0.3F).endVertex();
        vertexConsumer.vertex(matrix4f, 1.6f, 0, -3f).color(0.5f, 0.3f, 0.3f, 0.3F).endVertex();
    }

    private static void line(PoseStack stack, VertexConsumer vertexConsumer, Vec3 startPosition, Vec3 endPosition, Random random){
        Matrix4f matrix4f = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();//#001c3c
        Vec3 firstThird = endPosition.subtract(startPosition).scale(0.3).add(startPosition).add(random.nextGaussian() * 0.1 - 0.05,
                random.nextGaussian() * 0.1 - 0.05,
                random.nextGaussian() * 0.1 - 0.05);
        Vec3 secondThird = endPosition.subtract(firstThird).scale(0.5).add(firstThird).add(random.nextGaussian() * 0.3 - 0.15,
                random.nextGaussian() * 0.3 - 0.15,
                random.nextGaussian() * 0.3 - 0.15);
        vertexConsumer.vertex(matrix4f, (float) startPosition.x, (float) startPosition.y, (float) startPosition.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();
        vertexConsumer.vertex(matrix4f, (float) firstThird.x, (float) firstThird.y, (float) firstThird.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();

        vertexConsumer.vertex(matrix4f, (float) firstThird.x, (float) firstThird.y, (float) firstThird.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();
        vertexConsumer.vertex(matrix4f, (float) secondThird.x, (float) secondThird.y, (float) secondThird.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();

        vertexConsumer.vertex(matrix4f, (float) secondThird.x, (float) secondThird.y, (float) secondThird.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();
        vertexConsumer.vertex(matrix4f, (float) endPosition.x, (float) endPosition.y, (float) endPosition.z)
                .color(0, 28, 60, 255).normal(matrix3f, 0F, 2F, 0).endVertex();
        /*float firstX = (float) (endPosition.x * 1/3f * (random.nextFloat()*0.2 - 0.1d));
        float firstY = (float) (endPosition.y * 1/3f * (random.nextFloat()*0.2 - 0.1d));
        float firstZ = (float) (endPosition.z * 1/3f * (random.nextFloat()*0.2 - 0.1d));
        vertexConsumer.vertex(matrix4f, 0, 0, 0).color(0.8f, 0.3f, 0.3f, 0.9f).normal(matrix3f, 0F, 0F, 2.0F).endVertex();
        vertexConsumer.vertex(matrix4f, -firstX, firstY, -firstZ).color(0.8f, 0.3f, 0.3f, 0.9f).normal(matrix3f, 0F, 0F, 2.0F).endVertex();
        float secondX = (float) (endPosition.x * 2/3d * (random.nextFloat()*0.2 - 0.1d));
        float secondY = (float) (endPosition.y * 2/3d * (random.nextFloat()*0.2 - 0.1d));
        float secondZ = (float) (endPosition.z * 2/3d * (random.nextFloat()*0.2 - 0.1d));
        vertexConsumer.vertex(matrix4f, -firstX, firstY, -firstZ).color(0.8f, 0.3f, 0.3f, 0.9f).normal(matrix3f, 0F, 0F, 2.0F).endVertex();
        vertexConsumer.vertex(matrix4f, -secondX, secondY, -secondZ).color(0.8f, 0.3f, 0.3f, 0.9f).normal(matrix3f, 0F, 0F, 2.0F).endVertex();
        float lastX = (float) (endPosition.x);
        float lastY = (float) (endPosition.y);
        float lastZ = (float) (endPosition.z);
        vertexConsumer.vertex(matrix4f, -secondX, secondY, -secondZ).color(0.8f, 0.3f, 0.3f, 0.9f).normal(matrix3f, 0F, 0F, 2.0F).endVertex();
        vertexConsumer.vertex(matrix4f, -lastX, lastY, -lastZ).color(0.8f, 0.3f, 0.3f, 0.9f).normal(matrix3f, 0F, 0F, 2.0F).endVertex();*/

    }

    //Copied from MowziesMobs https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/render/RenderUtils.java
    public static void matrixStackFromModel(PoseStack matrixStack, GeoBone geoBone) {
        GeoBone parent = geoBone.parent;
        if (parent != null) matrixStackFromModel(matrixStack, parent);
        translateRotateGeckolib(geoBone, matrixStack);
    }

    //Copied from MowziesMobs https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/client/render/RenderUtils.java
    public static void translateRotateGeckolib(GeoBone bone, PoseStack matrixStackIn) {
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
    public static Vec3 getWorldPosFromModel(Entity entity, float entityYaw, float partialTicks, GeoBone geoBone) {
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
