package dev.mcpecommander.mobultion.items.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.items.ThunderStaffItem;
import dev.mcpecommander.mobultion.items.models.ThunderStaffModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import javax.annotation.Nullable;
import java.awt.*;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.renderers */
public class ThunderStaffRenderer extends GeoItemRenderer<ThunderStaffItem>
{
    public ThunderStaffRenderer()
    {
        super(new ThunderStaffModel());
    }

    @Override
    public Color getRenderColor(ThunderStaffItem animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn) {

        return new Color(255,250,205, 255);
    }
}
