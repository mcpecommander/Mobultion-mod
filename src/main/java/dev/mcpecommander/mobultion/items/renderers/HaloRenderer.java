package dev.mcpecommander.mobultion.items.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.mcpecommander.mobultion.items.HaloItem;
import dev.mcpecommander.mobultion.items.models.HaloModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import javax.annotation.Nullable;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.renderers */
public class HaloRenderer extends GeoItemRenderer<HaloItem>
{

    public HaloRenderer()
    {
        super(new HaloModel());
    }

    @Override
    public void render(GeoModel model, HaloItem animatable, float partialTicks, RenderType type, PoseStack matrixStackIn,
                       @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                       int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn,
                packedOverlayIn, red, green, blue, alpha);
        if(renderTypeBuffer != null) {
            this.renderRecursively(modelProvider.getModel(modelProvider.getModelLocation(animatable)).topLevelBones.get(0), matrixStackIn,
                    renderTypeBuffer.getBuffer(RenderType.eyes(getTextureLocation(animatable))), packedLightIn, packedOverlayIn,
                    red, green, blue, 1f);
        }
    }
}
