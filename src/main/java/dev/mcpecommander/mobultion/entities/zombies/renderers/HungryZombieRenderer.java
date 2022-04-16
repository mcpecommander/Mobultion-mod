package dev.mcpecommander.mobultion.entities.zombies.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import dev.mcpecommander.mobultion.entities.zombies.entities.HungryZombieEntity;
import dev.mcpecommander.mobultion.entities.zombies.layers.KnifeForkHoldingLayer;
import dev.mcpecommander.mobultion.entities.zombies.models.HungryZombieModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* McpeCommander created on 28/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.renderers */
public class HungryZombieRenderer extends GeoEntityRenderer<HungryZombieEntity> {

    public HungryZombieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HungryZombieModel());
        this.shadowRadius = 0.5f;
        this.addLayer(new KnifeForkHoldingLayer(this));
    }

    @Override
    protected float getDeathMaxRotation(HungryZombieEntity entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void render(GeoModel model, HungryZombieEntity animatable, float partialTicks, RenderType type, PoseStack stack,
                       @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                       int packedOverlayIn, float red, float green, float blue, float alpha) {
        renderEarly(animatable, stack, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);

        if (renderTypeBuffer != null) {
            vertexBuilder = renderTypeBuffer.getBuffer(type);
            //Makes it that the entity is only rendered with a red overlay when hurt but not on death.
            renderRecursively(model.topLevelBones.get(0), stack, vertexBuilder, packedLightIn, OverlayTexture.pack(0,
                    OverlayTexture.v(animatable.hurtTime > 0)), red, green, blue, alpha);
            //Render cake on death.
            for(int i = 0; i < 8 && animatable.isDeadOrDying(); i++){
                stack.pushPose();
                //Move the block to the center.
                stack.translate(-0.5f, 0f, -0.5f);
                //Move the block to the border of a circle.
                stack.translate(Mth.cos((float) (Math.PI/4 * i)) * 0.5, 0,
                        Mth.sin((float) (Math.PI/4 * i)) * 0.5);
                //Translate to the rotation and scaling pivot point.
                stack.translate(0.5f, 0f, 0.5f);
                //Rotate each cake around its middle y-axis.
                stack.mulPose(Vector3f.YP.rotation(animatable.tickCount * 0.1f));
                //Scale the block to make it look better.
                stack.scale(0.4f, 0.4f, 0.4f);
                //Translate back to the center from the rotation pivot.
                stack.translate(-0.5f, 0f, -0.5f);
                //The actual rendering part.
                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.CAKE.defaultBlockState(), stack,
                        renderTypeBuffer, packedLightIn, OverlayTexture.NO_OVERLAY);
                stack.popPose();
            }
        }
    }
}
