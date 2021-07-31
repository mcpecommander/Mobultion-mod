package dev.mcpecommander.mobultion.entities.zombies.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.mcpecommander.mobultion.entities.zombies.entities.HungryZombieEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 31/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.layers */
public class KnifeForkHoldingLayer extends GeoLayerRenderer<HungryZombieEntity> {

    private static final ResourceLocation ZOMBIE_MODEL = new ResourceLocation(MODID, "geo/zombies/hungryzombie.json");

    private ItemStack mainHand, offHand;

    public KnifeForkHoldingLayer(IGeoRenderer<HungryZombieEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrix, IRenderTypeBuffer renderBuffer, int packedLight, HungryZombieEntity entity,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.mainHand = entity.getItemBySlot(EquipmentSlotType.MAINHAND);
        this.offHand = entity.getItemBySlot(EquipmentSlotType.OFFHAND);
        GeoModel model = this.getEntityModel().getModel(ZOMBIE_MODEL);
        renderRecursively(model.topLevelBones.get(0), matrix, renderBuffer, packedLight, OverlayTexture.pack(0,
                OverlayTexture.v(entity.hurtTime > 0)));
    }

    //Copied from IGeoRenderer but removed the actual rendering of cubes and only render items to prevent overriding
    //the original renderRecursively and affecting other layers that might need different RenderTypes.
    //While it might seem like a niche use, it is quite important for example with entities that have both glowing parts
    //and hold/equip items/armour like endermen.
    private void renderRecursively(GeoBone bone, MatrixStack stack, IRenderTypeBuffer renderBuffer, int packedLightIn, int packedOverlayIn) {

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
            stack.mulPose(Vector3f.YP.rotationDegrees(-95));
            stack.mulPose(Vector3f.ZP.rotationDegrees(45));
            //You'll need to play around with this to render the item in the correct spot.
            stack.translate(0.9D, -0.4D, -0.5D);
            //Sets the scaling of the item.
            stack.scale(1f, 1f, 1f);
            // Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
            Minecraft.getInstance().getItemRenderer().renderStatic(mainHand, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                    packedLightIn, packedOverlayIn, stack, renderBuffer);
            stack.popPose();
        }
        if(bone.getName().equals("LeftArm1")){
            stack.pushPose();
            //You'll need to play around with these to get item to render in the correct orientation
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            stack.mulPose(Vector3f.YP.rotationDegrees(-90));
            stack.mulPose(Vector3f.ZP.rotationDegrees(45));
            //You'll need to play around with this to render the item in the correct spot.
            stack.translate(0.8D, -0.6D, 0.35D);
            //Sets the scaling of the item.
            stack.scale(1f, 1f, 1f);
            // Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
            Minecraft.getInstance().getItemRenderer().renderStatic(offHand, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
                    packedLightIn, packedOverlayIn, stack, renderBuffer);
            stack.popPose();
        }

        if (!bone.isHidden) {
            for (GeoBone childBone : bone.childBones) {
                renderRecursively(childBone, stack, renderBuffer, packedLightIn, packedOverlayIn);
            }
        }

        stack.popPose();
    }
}
