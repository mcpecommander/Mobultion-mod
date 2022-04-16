package dev.mcpecommander.mobultion.entities.endermen.renderers;

import dev.mcpecommander.mobultion.Mobultion;
import dev.mcpecommander.mobultion.entities.endermen.entities.GardenerEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.layers.EndermanEyesLayer;
import dev.mcpecommander.mobultion.entities.endermen.layers.GardenerEndermanItemLayer;
import dev.mcpecommander.mobultion.entities.endermen.models.GardenerEndermanModel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nonnull;

/* McpeCommander created on 26/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.renderers */
public class GardenerEndermanRenderer extends GeoEntityRenderer<GardenerEndermanEntity> {

    public GardenerEndermanRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GardenerEndermanModel());
        this.shadowRadius = 0.5F;
        this.addLayer(new EndermanEyesLayer<>(this, "gardenerenderman"));
        this.addLayer(new GardenerEndermanItemLayer(this));
    }

    /**
     * Whether this entity should be rendered or not. Usually it calculates frustum and position stuff to make sure
     * Minecraft is only rendering entities within view.
     * @param entity The entity in question.
     * @param clippingHelper A helper class for frustum calculations and more.
     * @param x The x position of the camera.
     * @param y The y position of the camera.
     * @param z The z position of the camera.
     * @return true if the entity should be rendered.
     */
    @Override
    public boolean shouldRender(@Nonnull GardenerEndermanEntity entity, @Nonnull Frustum clippingHelper,
                                double x, double y, double z) {
        return Mobultion.DEBUG || super.shouldRender(entity, clippingHelper, x, y, z);
    }

    /**
     * How much the entity rotates when it dies. The default is 90 degrees like lying on the ground dead.
     * @param entity The entity that is dying.
     * @return a float of the degrees that this entity rotates on death.
     */
    @Override
    protected float getDeathMaxRotation(GardenerEndermanEntity entity) {
        return 0f;
    }

    //An awful way to debug navigation paths, but it works.
    //DO NOT REPEAT OR COPY AT ALL COSTS.
//    @Override
//    public void renderLate(GardenerEndermanEntity animatable, PoseStack stackIn, float ticks,
//                           MultiBufferSource renderTypeBuffer, VertexConsumer bufferIn, int packedLightIn,
//                           int packedOverlayIn, float red, float green, float blue, float partialTicks) {
//        //Render debug path
//        if(!Mobultion.DEBUG) return;
//        List<BlockPos> positions = animatable.getDebugRoad();
//        stackIn.pushPose();
//        VertexConsumer builder = renderTypeBuffer.getBuffer(RenderType.LINES);
//
//        //Rotate the lines to be in the right direction
//        stackIn.mulPose(new Quaternion(0,
//                180 + Mth.rotLerp(partialTicks, animatable.yBodyRotO, animatable.yBodyRot),
//                0, true));
//        //Translate to the entity position
//        stackIn.translate(-animatable.position().x, -animatable.position().y, -animatable.position().z);
//        for(int i = 0; i < positions.size() -1; i++){
//            builder.vertex(stackIn.last().pose(), positions.get(i).getX() + 0.5f,
//                    positions.get(i).getY() + 0.1f,
//                    positions.get(i).getZ() + 0.5f).
//                    color(255, 255, 255, 255).endVertex();
//            builder.vertex(stackIn.last().pose(), positions.get(i+1).getX() + 0.5f,
//                    positions.get(i+1).getY() + 0.1f,
//                    positions.get(i+1).getZ() + 0.5f).
//                    color(255, 255, 255, 255).endVertex();
//        }
//        stackIn.popPose();
//        renderTypeBuffer.getBuffer(RenderType.entityCutout(getTextureLocation(animatable)));
//    }
}