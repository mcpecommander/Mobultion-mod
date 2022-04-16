package dev.mcpecommander.mobultion.items.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.mcpecommander.mobultion.items.HealingStaffItem;
import dev.mcpecommander.mobultion.items.models.HealingStaffModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import java.util.Collections;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.renderers */
public class HealingStaffRenderer extends GeoItemRenderer<HealingStaffItem>
{
    public HealingStaffRenderer()
    {
        super(new HealingStaffModel());
    }

    //Copied the whole thing and modified the render buffer to make sure that the gem is lit.
    @Override
    public void render(HealingStaffItem animatable, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn,
                       ItemStack itemStack) {
        this.currentItemStack = itemStack;
        GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(animatable));
        AnimationEvent<HealingStaffItem> itemEvent = new AnimationEvent<>(animatable, 0, 0,
                Minecraft.getInstance().getFrameTime(), false, Collections.singletonList(itemStack));
        modelProvider.setLivingAnimations(animatable, this.getUniqueID(animatable), itemEvent);
        stack.pushPose();
        stack.translate(0, 0.01f, 0);
        stack.translate(0.5, 0.5, 0.5);

        Minecraft.getInstance().textureManager.bindForSetup(getTextureLocation(animatable));
        renderRecursively(animatable, model.topLevelBones.get(0), stack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY,
                    1f, 1f, 1f, 1f);
        stack.popPose();
    }

    //Copied from IGeoRenderer but removed the IVertexBuffer parameter in favor of inlined one to make the gem use a different RenderType.
    public void renderRecursively(HealingStaffItem animatable, GeoBone bone, PoseStack stack, MultiBufferSource renderTypeBuffer,
                                  int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        stack.pushPose();
        RenderUtils.translate(bone, stack);
        RenderUtils.moveToPivot(bone, stack);
        RenderUtils.rotate(bone, stack);
        RenderUtils.scale(bone, stack);
        RenderUtils.moveBackFromPivot(bone, stack);
        if (renderTypeBuffer != null) {
            if (!bone.isHidden) {
                for (GeoCube cube : bone.childCubes) {
                    stack.pushPose();
                    if(bone.getName().equals("Gem")) {
                        renderCube(cube, stack, renderTypeBuffer.getBuffer(RenderType.eyes(getTextureLocation(animatable))),
                                packedLightIn, packedOverlayIn, red, green, blue, alpha);
                    }else {
                        renderCube(cube, stack, renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(animatable))),
                                packedLightIn, packedOverlayIn, red, green, blue, alpha);
                    }
                    stack.popPose();
                }
                for (GeoBone childBone : bone.childBones) {
                    renderRecursively(animatable, childBone, stack, renderTypeBuffer, packedLightIn, packedOverlayIn,
                            red, green, blue, alpha);
                }
            }
        }

        stack.popPose();
    }

}
