package dev.mcpecommander.mobultion.entities.endermen.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.entities.endermen.entities.WanderingEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.layers.EndermanEyesLayer;
import dev.mcpecommander.mobultion.entities.endermen.models.WanderingEndermanModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* McpeCommander created on 25/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.renderers */
public class WanderingEndermanRenderer extends GeoEntityRenderer<WanderingEndermanEntity> {

    WanderingEndermanEntity entity;
    float partialTicks;

    public WanderingEndermanRenderer(EntityRendererManager renderManager){
        super(renderManager,new WanderingEndermanModel());
        this.shadowRadius=0.5F;
        this.addLayer(new EndermanEyesLayer<>(this, "wanderingenderman"));
    }

    @Override
    public void render(WanderingEndermanEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        this.entity = entity;
        this.partialTicks = partialTicks;
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        if (entity == null) return;
        if(bone.getName().equals("Test")){
            stack.pushPose();
            //You'll need to play around with these to get item to render in the correct orientation
            stack.mulPose(Vector3f.XP.rotationDegrees(-75));
            stack.mulPose(Vector3f.YP.rotationDegrees(0));
            stack.mulPose(Vector3f.ZP.rotationDegrees(0));
            //You'll need to play around with this to render the item in the correct spot.
            stack.translate(0.3D, 0.5D, 0.8D);
            //Sets the scaling of the item.
            stack.scale(1.0f, 2f, 1.0f);
            // Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
            Minecraft.getInstance().getItemRenderer().renderStatic(mainHand, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            stack.popPose();
            //TODO: change to a layer to make sure it doesn't break every other layer.
            bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
        }

        //Copied from the vanilla cape rendering and modified a bit.
        if(bone.getName().equals("Cape")){
            stack.translate(0.0D, 3.35D, -0.1D);
            double d0 = MathHelper.lerp(partialTicks, entity.xCloakO, entity.xCloak) - MathHelper.lerp(partialTicks, entity.xo, entity.getX());
            double d1 = MathHelper.lerp(partialTicks, entity.yCloakO, entity.yCloak) - MathHelper.lerp(partialTicks, entity.yo, entity.getY());
            double d2 = MathHelper.lerp(partialTicks, entity.zCloakO, entity.zCloak) - MathHelper.lerp(partialTicks, entity.zo, entity.getZ());
            float f = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO);
            double d3 = MathHelper.sin(f * ((float)Math.PI / 180F));
            double d4 = -MathHelper.cos(f * ((float)Math.PI / 180F));
            float f1 = (float)d1 * 10.0F;
            f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
            float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
            f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
            float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
            f3 = MathHelper.clamp(f3, -20.0F, 20.0F);
            if (f2 < 0.0F) {
                f2 = 0.0F;
            }
            stack.translate(0.0D, -1d, 0);
            stack.mulPose(Vector3f.XP.rotationDegrees(-(6.0F + f2 / 2.0F + f1)));
            stack.mulPose(Vector3f.ZP.rotationDegrees(f3 / 2.0F));
            stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - f3 / 2.0F));
            stack.translate(0.0D, +1d, -0.1);
            stack.mulPose(Vector3f.XP.rotationDegrees(-180f));
        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

}