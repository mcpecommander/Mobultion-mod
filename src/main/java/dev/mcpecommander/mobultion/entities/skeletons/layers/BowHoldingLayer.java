package dev.mcpecommander.mobultion.entities.skeletons.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.mcpecommander.mobultion.entities.skeletons.entities.JokerSkeletonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 19/08/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.layers */
public class BowHoldingLayer extends GeoLayerRenderer<JokerSkeletonEntity> {

    private static final ResourceLocation SKELETON_MODEL = new ResourceLocation(MODID, "geo/skeletons/jokerskeleton.json");

    private ItemStack mainHand;

    public BowHoldingLayer(IGeoRenderer<JokerSkeletonEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrix, IRenderTypeBuffer renderBuffer, int packedLight, JokerSkeletonEntity entity,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.mainHand = entity.getItemBySlot(EquipmentSlotType.MAINHAND);
        GeoModel model = this.getEntityModel().getModel(SKELETON_MODEL);
        renderRecursively(model.topLevelBones.get(0), matrix, renderBuffer, packedLight, GeoEntityRenderer.getPackedOverlay(entity, 0));
    }

    //Copied from IGeoRenderer but removed the actual rendering of cubes and only render items to prevent overriding
    //the original renderRecursively and affecting other layers that might need different RenderTypes.
    //While it might seem like a niche use, it is quite important for example with entities that have both glowing parts
    //and hold/equip items/armour like endermen.
    private void renderRecursively(GeoBone bone, MatrixStack stack, IRenderTypeBuffer renderBuffer, int packedLightIn,
                                   int packedOverlayIn) {

        stack.pushPose();
        RenderUtils.translate(bone, stack);
        RenderUtils.moveToPivot(bone, stack);
        RenderUtils.rotate(bone, stack);
        RenderUtils.scale(bone, stack);
        RenderUtils.moveBackFromPivot(bone, stack);
        if(bone.getName().equals("RightArm1")){
            stack.pushPose();
            //You'll need to play around with these to get item to render in the correct orientation
            stack.mulPose(Vector3f.XP.rotationDegrees(-75));
            stack.mulPose(Vector3f.YP.rotationDegrees(0));
            stack.mulPose(Vector3f.ZP.rotationDegrees(0));
            //You'll need to play around with this to render the item in the correct spot.
            stack.translate(0.35D, 0.3D, 0.7D);
            //Sets the scaling of the item.
            stack.scale(0.8f, 0.8f, 0.8f);
            // Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
            Minecraft.getInstance().getItemRenderer().renderStatic(mainHand, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                    packedLightIn, packedOverlayIn, stack, renderBuffer);
            stack.popPose();
            //Stops unnecessary further recursive method calling. Only works if I am rendering one thing per layer.
            stack.popPose();
            return;
        }

        if (!bone.isHidden) {
            for (GeoBone childBone : bone.childBones) {
                renderRecursively(childBone, stack, renderBuffer, packedLightIn, packedOverlayIn);
            }
        }

        stack.popPose();
    }
}
