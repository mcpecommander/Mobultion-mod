package dev.mcpecommander.mobultion.entities.endermen.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.Mobultion;
import dev.mcpecommander.mobultion.entities.endermen.entities.GardenerEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.layers.EndermanEyesLayer;
import dev.mcpecommander.mobultion.entities.endermen.layers.GardenerEndermanItemLayer;
import dev.mcpecommander.mobultion.entities.endermen.models.GardenerEndermanModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.List;

/* McpeCommander created on 26/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.renderers */
public class GardenerEndermanRenderer extends GeoEntityRenderer<GardenerEndermanEntity> {

    public GardenerEndermanRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GardenerEndermanModel());
        this.shadowRadius = 0.5F;
        this.addLayer(new EndermanEyesLayer<>(this, "gardenerenderman"));
        this.addLayer(new GardenerEndermanItemLayer(this));
    }

    @Override
    public boolean shouldRender(GardenerEndermanEntity entity, ClippingHelper clippingHelper,
                                double x, double y, double z) {
        return Mobultion.DEBUG || super.shouldRender(entity, clippingHelper, x, y, z);
    }

    @Override
    protected float getDeathMaxRotation(GardenerEndermanEntity entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void renderLate(GardenerEndermanEntity animatable, MatrixStack stackIn, float ticks,
                           IRenderTypeBuffer renderTypeBuffer, IVertexBuilder bufferIn, int packedLightIn,
                           int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        if(!Mobultion.DEBUG) return;
        List<BlockPos> positions = animatable.getDebugRoad();
        //if(animatable.tickCount % 10 == 0)System.out.println(positions);
        stackIn.pushPose();
        IVertexBuilder builder = renderTypeBuffer.getBuffer(RenderType.LINES);
        stackIn.mulPose(new Quaternion(0,
                180 + MathHelper.rotLerp(partialTicks, animatable.yBodyRotO, animatable.yBodyRot),
                0, true));
        stackIn.translate(-animatable.position().x, -animatable.position().y, -animatable.position().z);
        for(int i = 0; i < positions.size() -1; i++){
            builder.vertex(stackIn.last().pose(), positions.get(i).getX() + 0.5f,
                    positions.get(i).getY() + 0.1f,
                    positions.get(i).getZ() + 0.5f).
                    color(255, 255, 255, 255).endVertex();
            builder.vertex(stackIn.last().pose(), positions.get(i+1).getX() + 0.5f,
                    positions.get(i+1).getY() + 0.1f,
                    positions.get(i+1).getZ() + 0.5f).
                    color(255, 255, 255, 255).endVertex();
        }
        stackIn.popPose();
        renderTypeBuffer.getBuffer(RenderType.entityCutout(getTextureLocation(animatable)));
    }
}