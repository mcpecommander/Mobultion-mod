package dev.mcpecommander.mobultion.entities.endermen.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import dev.mcpecommander.mobultion.entities.endermen.entities.WanderingEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.layers.EndermanEyesLayer;
import dev.mcpecommander.mobultion.entities.endermen.layers.WanderingEndermanHoldingLayer;
import dev.mcpecommander.mobultion.entities.endermen.models.WanderingEndermanModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* McpeCommander created on 25/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.renderers */
public class WanderingEndermanRenderer extends GeoEntityRenderer<WanderingEndermanEntity> {

    public WanderingEndermanRenderer(EntityRendererProvider.Context renderManager){
        super(renderManager, new WanderingEndermanModel());
        this.shadowRadius=0.5F;
        this.addLayer(new EndermanEyesLayer<>(this, "wanderingenderman"));
        this.addLayer(new WanderingEndermanHoldingLayer(this));
    }

    @Override
    public void render(GeoModel model, WanderingEndermanEntity animatable, float partialTicks, RenderType type,
                       PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer,
                       @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                       float red, float green, float blue, float alpha) {
        renderEarly(animatable, stack, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);

        if (renderTypeBuffer != null) {
            vertexBuilder = renderTypeBuffer.getBuffer(type);
        }

        // Render all top level bones
        for (GeoBone group : model.topLevelBones) {
            renderRecursively(group, animatable, partialTicks, stack, vertexBuilder, packedLightIn, packedOverlayIn, red,
                    green, blue, alpha);
        }
    }

    public void renderRecursively(GeoBone bone, WanderingEndermanEntity animatable, float partialTicks, PoseStack stack,
                                  VertexConsumer bufferIn, int packedLightIn,
                                  int packedOverlayIn, float red, float green, float blue, float alpha) {
        //Copied from the vanilla cape rendering and modified a bit.
        if(bone.getName().equals("Cape")){
            stack.translate(0.0D, 3.35D, -0.1D);
            double d0 = Mth.lerp(partialTicks, animatable.xCloakO, animatable.xCloak) - Mth.lerp(partialTicks,
                    animatable.xo, animatable.getX());
            double d1 = Mth.lerp(partialTicks, animatable.yCloakO, animatable.yCloak) - Mth.lerp(partialTicks,
                    animatable.yo, animatable.getY());
            double d2 = Mth.lerp(partialTicks, animatable.zCloakO, animatable.zCloak) - Mth.lerp(partialTicks,
                    animatable.zo, animatable.getZ());
            float f = animatable.yBodyRotO + (animatable.yBodyRot - animatable.yBodyRotO);
            double d3 = Mth.sin(f * ((float)Math.PI / 180F));
            double d4 = -Mth.cos(f * ((float)Math.PI / 180F));
            float f1 = (float)d1 * 10.0F;
            f1 = Mth.clamp(f1, -6.0F, 32.0F);
            float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
            f2 = Mth.clamp(f2, 0.0F, 150.0F);
            float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
            f3 = Mth.clamp(f3, -20.0F, 20.0F);
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

    /**
     * How much the entity rotates when it dies. The default is 90 degrees like lying on the ground dead.
     * @param entity The entity that is dying.
     * @return a float of the degrees that this entity rotates on death.
     */
    @Override
    protected float getDeathMaxRotation(WanderingEndermanEntity entity) {
        return 0f;
    }
}