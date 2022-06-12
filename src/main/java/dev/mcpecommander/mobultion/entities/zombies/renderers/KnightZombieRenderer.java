package dev.mcpecommander.mobultion.entities.zombies.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import dev.mcpecommander.mobultion.entities.zombies.entities.KnightZombieEntity;
import dev.mcpecommander.mobultion.entities.zombies.layers.ItemHoldingLayer;
import dev.mcpecommander.mobultion.entities.zombies.models.KnightZombieModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 26/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.renderers */
public class KnightZombieRenderer extends GeoEntityRenderer<KnightZombieEntity> {

    private static final ResourceLocation IRON_ARMOUR = new ResourceLocation(MODID, "textures/entity/zombies/ironarmour.png");
    private final List<Vec3> images = new ArrayList<>();

    public KnightZombieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new KnightZombieModel());
        this.shadowRadius = 0.5f;
        this.addLayer(new ItemHoldingLayer<>(this));
    }

    @Override
    protected float getDeathMaxRotation(KnightZombieEntity entityLivingBaseIn) {
        return 0f;
    }


    @Override
    public void render(GeoModel model, KnightZombieEntity animatable, float partialTicks, RenderType type, PoseStack stack,
                @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLight,
                int packedOverlayIn, float red, float green, float blue, float alpha) {
        renderEarly(animatable, stack, partialTicks, renderTypeBuffer, vertexBuilder, packedLight,
                packedOverlayIn, red, green, blue, alpha);

        if (renderTypeBuffer != null) {
            vertexBuilder = renderTypeBuffer.getBuffer(type);
        }
        //Don't use the partial position, otherwise we will get way too many positions that only differ slightly.
        Vec3 position = animatable.position();
        //Add the old positions to make blur fake-out.
        if(animatable.isAlive() && animatable.isCharging()){
            if(!images.contains(position)) {
                images.add(0, position);
            }
            //Keep the blur to a maximum of 4 copies.
            if(images.size() > 4) images.remove(images.size() - 1);
        }else{
            //Remove the copies if the entity stopped charging.
            images.clear();
        }
        for(int i = 0; i < images.size(); i++){
            Vec3 image = images.get(i);
            Vec3 sub = position.subtract(image);
            stack.pushPose();
            //Rotate with the original entity rotation.
            stack.mulPose(Vector3f.YP.rotationDegrees(animatable.yBodyRot));
            //Translate to the position of the image.
            stack.translate(sub.x, 0, sub.z);
            //Rotate again but backwards to rotate the entity to the correct rotation.
            stack.mulPose(Vector3f.YN.rotationDegrees(animatable.yBodyRot));
            //Scale down just a bit to avoid z-fighting.
            stack.scale(0.9999f, 0.9999f, 0.9999f);
            //Render the copy.
            renderRecursively(model.topLevelBones.get(0), stack, vertexBuilder, packedLight, OverlayTexture.pack(0,
                    OverlayTexture.v(animatable.hurtTime > 0)), red, green, blue, 1.3f - i/6f + 0.3f);
            //Scale up for the armour.
            stack.scale(1.1f, 1.01f, 1.1f);
            //Render the iron armour.
            assert renderTypeBuffer != null;
            renderRecursively(model.topLevelBones.get(0), stack, renderTypeBuffer.getBuffer(RenderType.entityTranslucent(IRON_ARMOUR)),
                    packedLight, OverlayTexture.pack(0, OverlayTexture.v(animatable.hurtTime > 0)), red, green, blue, 1.3f - i/6f + 0.3f);
            stack.popPose();
        }

        if (renderTypeBuffer != null) {
            vertexBuilder = renderTypeBuffer.getBuffer(type);
        }
        //Makes it that the entity is only rendered with a red overlay when hurt but not on death.
        this.renderRecursively(model.topLevelBones.get(0), stack, vertexBuilder, packedLight, OverlayTexture.pack(0,
                OverlayTexture.v(animatable.hurtTime > 0)), red, green, blue, alpha);
        //stack.pushPose();
        //Scale up for the armour render.
        //stack.scale(1.1f, 1.01f, 1.1f);
        //Render the armour.
        assert renderTypeBuffer != null;
        this.renderRecursively(model.topLevelBones.get(0), stack, renderTypeBuffer.getBuffer(RenderType.entityTranslucent(IRON_ARMOUR)),
                packedLight, OverlayTexture.pack(0, OverlayTexture.v(animatable.hurtTime > 0)));
        //stack.popPose();

    }

    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn,
                                   int packedOverlayIn) {
        stack.pushPose();
        RenderUtils.translate(bone, stack);
        RenderUtils.moveToPivot(bone, stack);
        RenderUtils.rotate(bone, stack);
        RenderUtils.scale(bone, stack);
        RenderUtils.moveBackFromPivot(bone, stack);

        if (!bone.isHidden) {
            for (GeoCube cube : bone.childCubes) {
                stack.pushPose();
                stack.scale(1.1f, 1.01f, 1.1f);
                renderCube(cube, stack, bufferIn, packedLightIn, packedOverlayIn, 1f, 1f, 1f, 1f);
                stack.popPose();
            }
            for (GeoBone childBone : bone.childBones) {
                renderRecursively(childBone, stack, bufferIn, packedLightIn, packedOverlayIn);
            }
        }

        stack.popPose();
    }

    @Override
    public RenderType getRenderType(KnightZombieEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
                                    int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(textureLocation);
    }
}
