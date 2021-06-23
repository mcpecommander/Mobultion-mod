package dev.mcpecommander.mobultion.entities.skeletons.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.entities.skeletons.entities.JokerSkeletonEntity;
import dev.mcpecommander.mobultion.entities.skeletons.models.JokerSkeletonModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* McpeCommander created on 21/06/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.renderers */
public class JokerSkeletonRenderer extends GeoEntityRenderer<JokerSkeletonEntity> {

    JokerSkeletonEntity entity;
    AnimationController controller;

    public JokerSkeletonRenderer(EntityRendererManager renderManager) {
        super(renderManager, new JokerSkeletonModel());
        this.shadowRadius = 0.5F;

    }

    @Override
    public void render(JokerSkeletonEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        this.entity = entity;
        if(controller == null){
            controller = entity.getFactory().getOrCreateAnimationData(entity.getId()).getAnimationControllers().get("controller");
        }
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        if (bone.getName().equals("Holder") && controller != null) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
            stack.pushPose();
            if(controller.getCurrentAnimation() == null || controller.getAnimationState() == AnimationState.Stopped){
                //You'll need to play around with these to get item to render in the correct orientation
                stack.mulPose(Vector3f.XP.rotationDegrees(-75));
                stack.mulPose(Vector3f.YP.rotationDegrees(0));
                stack.mulPose(Vector3f.ZP.rotationDegrees(0));
                //You'll need to play around with this to render the item in the correct spot.
                stack.translate(0.4D, 0.27D, 0.74D);
            }else{
                //You'll need to play around with these to get item to render in the correct orientation
                stack.mulPose(Vector3f.XP.rotationDegrees(-75));
                stack.mulPose(Vector3f.YP.rotationDegrees(-50));
                stack.mulPose(Vector3f.ZP.rotationDegrees(0));
                //You'll need to play around with this to render the item in the correct spot.
                stack.translate(0.85D, 0.3D, 0.25D);
            }
            //Sets the scaling of the item.
            stack.scale(1.0f, 0.8f, 1.0f);
            // Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
            Minecraft.getInstance().getItemRenderer().renderStatic(mainHand, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            stack.popPose();
            bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}