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

    //Return translucent to make the entity translucent. Default is cutout.
    @Override
    public RenderType getRenderType(GlassEndermanEntity animatable, float partialTicks, MatrixStack stack,
                                    @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder,
                                    int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    /**
     * The render color for this entity.
     * @param animatable The entity being colored.
     * @param partialTicks The partial ticks for smoothing the 20 ticks rendering into any frame rate wanted.
     * @param stack The matrix stack used for pose rotation and translation.
     * @param renderTypeBuffer The render buffer instance.
     * @param vertexBuilder The vertex builder instance.
     * @param packedLightIn The light level vanilla magic numbers.
     * @return The color that should be used to render the entity.
     */
    @Override
    public Color getRenderColor(GlassEndermanEntity animatable, float partialTicks, MatrixStack stack,
                                @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder,
                                int packedLightIn) {
        return animatable.getColor();
    }

    /**
     * How much the entity rotates when it dies. The default is 90 degrees like lying on the ground dead.
     * @param entity The entity that is dying.
     * @return a float of the degrees that this entity rotates on death.
     */
    @Override
    protected float getDeathMaxRotation(GlassEndermanEntity entity) {
        return 0f;
    }
}
