package dev.mcpecommander.mobultion.entities.skeletons.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.entities.skeletons.entities.JokerSkeletonEntity;
import dev.mcpecommander.mobultion.entities.skeletons.layers.BowHoldingLayer;
import dev.mcpecommander.mobultion.entities.skeletons.models.JokerSkeletonModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

/* McpeCommander created on 21/06/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.renderers */
public class JokerSkeletonRenderer extends GeoEntityRenderer<JokerSkeletonEntity> {

    private float rotationYaw;

    public JokerSkeletonRenderer(EntityRendererManager renderManager) {
        super(renderManager, new JokerSkeletonModel());
        this.shadowRadius = 0.5F;
        this.addLayer(new BowHoldingLayer<>(this));

    }

    //The reason I am using this approach to stop the entity from rotating is because it is the least invasive. I tried
    //returning the tick method but that just stopped remove() from being executed. I tried to return all rotation methods
    //but those don't really work either because Vanilla jank. I tried returning this method but that made the rotation 0 no
    //matter what the angle was before death and that is just lazy even for me. This solution is simple but has one problem.
    //If I am ever to go out of the entity's rendering frustum, I believe the rotation will become 0, but it is an edge case
    //that isn't easily noticeable.
    @Override
    protected void applyRotations(JokerSkeletonEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        if(entityLiving.isDeadOrDying()){
            super.applyRotations(entityLiving, matrixStackIn, ageInTicks, this.rotationYaw, partialTicks);
        }else{
            this.rotationYaw = rotationYaw;
            super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        }

    }

    /**
     * How much the entity rotates when it dies. The default is 90 degrees like lying on the ground dead.
     * @param entity The entity that is dying.
     * @return a float of the degrees that this entity rotates on death.
     */
    @Override
    protected float getDeathMaxRotation(JokerSkeletonEntity entity) {
        return 0f;
    }

    @Override
    public void render(GeoModel model, JokerSkeletonEntity animatable, float partialTicks, RenderType type, MatrixStack matrixStackIn,
                       @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn,
                       int packedOverlayIn, float red, float green, float blue, float alpha) {
        renderEarly(animatable, matrixStackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);

        if (renderTypeBuffer != null) {
            vertexBuilder = renderTypeBuffer.getBuffer(type);
        }
        //Makes it that the entity is only rendered with a red overlay when hurt but not on death.
        renderRecursively(model.topLevelBones.get(0), matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.pack(0,
                OverlayTexture.v(animatable.hurtTime > 0)), red, green, blue, alpha);
    }

}