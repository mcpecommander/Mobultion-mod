package dev.mcpecommander.mobultion.entities.endermen.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.entities.endermen.entities.GlassShotEntity;
import dev.mcpecommander.mobultion.entities.endermen.models.GlassShotModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

import javax.annotation.Nullable;
import java.awt.*;

/* McpeCommander created on 03/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.renderers */
public class GlassShotRenderer extends GeoProjectilesRenderer<GlassShotEntity> {

    public GlassShotRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GlassShotModel());
        this.shadowRadius = 0.5F;
    }

    @Override
    public RenderType getRenderType(GlassShotEntity animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public Color getRenderColor(GlassShotEntity animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn) {
        return animatable.getColor();
    }
}
