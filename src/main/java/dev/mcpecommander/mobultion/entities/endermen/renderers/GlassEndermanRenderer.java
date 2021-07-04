package dev.mcpecommander.mobultion.entities.endermen.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.mcpecommander.mobultion.entities.endermen.entities.GlassEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.layers.EndermanEyesLayer;
import dev.mcpecommander.mobultion.entities.endermen.models.GlassEndermanModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.awt.*;

/* McpeCommander created on 02/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.renderers */
public class GlassEndermanRenderer extends GeoEntityRenderer<GlassEndermanEntity> {

    public GlassEndermanRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GlassEndermanModel());
        this.shadowRadius = 0.5F;
        this.addLayer(new EndermanEyesLayer<>(this, "glassenderman"));
    }

    @Override
    public RenderType getRenderType(GlassEndermanEntity animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public Color getRenderColor(GlassEndermanEntity animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn) {
        return animatable.getColor();
    }
}
