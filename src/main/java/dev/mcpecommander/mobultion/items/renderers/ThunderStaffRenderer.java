package dev.mcpecommander.mobultion.items.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.mcpecommander.mobultion.items.ThunderStaffItem;
import dev.mcpecommander.mobultion.items.models.ThunderStaffModel;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import javax.annotation.Nullable;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.renderers */
public class ThunderStaffRenderer extends GeoItemRenderer<ThunderStaffItem>
{
    public ThunderStaffRenderer()
    {
        super(new ThunderStaffModel());
    }

    @Override
    public Color getRenderColor(ThunderStaffItem animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn) {

        return Color.ofRGBA(255,250,205, 255);
    }


}
