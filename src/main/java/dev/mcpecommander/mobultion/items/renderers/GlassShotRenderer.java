package dev.mcpecommander.mobultion.items.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.mcpecommander.mobultion.items.GlassShotItem;
import dev.mcpecommander.mobultion.items.models.GlassShotModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.core.util.Color;

import javax.annotation.Nullable;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.renderers */
public class GlassShotRenderer extends GeoItemRenderer<GlassShotItem>
{

    public GlassShotRenderer()
    {
        super(new GlassShotModel());
    }

    @Override
    public RenderType getRenderType(GlassShotItem animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
                                    int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(textureLocation);
    }

    @Override
    public Color getRenderColor(GlassShotItem animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn) {
        return Color.ofRGBA(255, 255, 255, 163);
    }
}
