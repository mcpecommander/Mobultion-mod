package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import dev.mcpecommander.mobultion.entities.spiders.entities.WitchSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderEyesLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.WitchSpiderModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* Created by McpeCommander on 2021/06/18 */
public class WitchSpiderRenderer extends GeoEntityRenderer<WitchSpiderEntity> {

    public WitchSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WitchSpiderModel());
        this.addLayer(new SpiderEyesLayer<>(this, "witchspider"));
        this.shadowRadius = 0.7F;
    }

    @Override
    public boolean shouldRender(@NotNull WitchSpiderEntity witchSpider, @NotNull Frustum viewFrustum,
                                double posX, double posY, double posZ) {
        //Don't stop rendering when attacking to make sure the confusion attack doesn't magically disappear if the player
        //turned around.
        return witchSpider.getTarget() != null || super.shouldRender(witchSpider, viewFrustum, posX, posY, posZ);
    }

    @Override
    public void render(WitchSpiderEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack stack,
                       @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        if(entity.isDeadOrDying()) this.shadowRadius = 0f;
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public void render(GeoModel model, WitchSpiderEntity animatable, float partialTicks, RenderType type,
                       PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
                       int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        renderEarly(animatable, stack, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);
        if (renderTypeBuffer != null) {
            vertexBuilder = renderTypeBuffer.getBuffer(type);

            if(animatable.getAttackMode() != 4) {
                renderRecursively(model.topLevelBones.get(0), stack, vertexBuilder, packedLightIn, OverlayTexture.pack(0,
                        OverlayTexture.v(animatable.hurtTime > 0)), red, green, blue, alpha);
            }
            //Render copies when attack mode is 4.
            if(animatable.isAlive() && animatable.getUseItemRemainingTicks() > 0 && animatable.getTarget() instanceof Player player
                    && animatable.getAttackMode() == 4){
                //Linear interpolation to stop the copies from jittering.
                double xPlayerTran = Mth.lerp(partialTicks, player.xOld, player.getX());
                double yPlayerTran = Mth.lerp(partialTicks, player.yOld, player.getY());
                double zPlayerTran = Mth.lerp(partialTicks, player.zOld, player.getZ());
                double xMobTran = Mth.lerp(partialTicks, animatable.xOld, animatable.getX());
                double yMobTran = Mth.lerp(partialTicks, animatable.yOld, animatable.getY());
                double zMobTran = Mth.lerp(partialTicks, animatable.zOld, animatable.getZ());
                double distance = animatable.distanceTo(player);
                for(int i = 0; i < 8; i++){
                    stack.pushPose();
                    //Account for the entity rotation.
                    stack.mulPose(new Quaternion(0, 180 + Mth.rotLerp(partialTicks, animatable.yBodyRotO, animatable.yBodyRot),
                            0, true));
                    //Translate to the target.
                    stack.translate(xPlayerTran - xMobTran, yPlayerTran - yMobTran, zPlayerTran - zMobTran);
                    //Move the spider to the border of a circle and then make it come closer.
                    stack.translate(Mth.cos((float) (Math.PI/4d * i)) * distance *
                                    ((animatable.getUseItemRemainingTicks()%WitchSpiderEntity.ATTACK_4)/WitchSpiderEntity.ATTACK_4), 0,
                                    Mth.sin((float) (Math.PI/4d * i)) * distance *
                                    ((animatable.getUseItemRemainingTicks()%WitchSpiderEntity.ATTACK_4)/WitchSpiderEntity.ATTACK_4));
                    //Rotate each spider around its middle y-axis.
                    //The first Math.PI/2 is the offset and then rotate by 45 degrees for each copy.
                    stack.mulPose(Vector3f.YP.rotation((float) (Math.PI/2f - i * Math.PI/4f)));
                    //The actual rendering part.
                    renderRecursively(model.topLevelBones.get(0), stack, vertexBuilder, packedLightIn, OverlayTexture.pack(0,
                            10), red, green, blue, animatable.getUseItemRemainingTicks()/WitchSpiderEntity.ATTACK_4);
                    stack.popPose();
                }
            }
        }
    }

    //Return translucent to make the entity translucent which makes it so that the alpha value is taken into
    //consideration when rendering. Default is cutout.
    @Override
    public RenderType getRenderType(WitchSpiderEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
                                    int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    protected float getDeathMaxRotation(WitchSpiderEntity entityLivingBaseIn) {
        return 0;
    }
}
