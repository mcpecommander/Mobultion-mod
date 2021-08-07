package dev.mcpecommander.mobultion.items.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.items.GlassShotItem;
import dev.mcpecommander.mobultion.items.models.GlassShotModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import javax.annotation.Nullable;
import java.awt.*;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.renderers */
public class GlassShotRenderer extends GeoItemRenderer<GlassShotItem>
{

    public GlassShotRenderer()
    {
        super(new GlassShotModel());
    }

    @Override
    public RenderType getRenderType(GlassShotItem animatable, float partialTicks, MatrixStack stack,
                                    @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder,
                                    int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(textureLocation);
    }

    @Override
    public Color getRenderColor(GlassShotItem animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn) {
        return new Color(0x80FFFFFF, true);
    }
}
