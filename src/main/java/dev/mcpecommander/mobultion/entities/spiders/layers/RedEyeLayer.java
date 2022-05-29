package dev.mcpecommander.mobultion.entities.spiders.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.mcpecommander.mobultion.entities.spiders.entities.RedEyeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 26/05/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.layers */
public class RedEyeLayer extends GeoLayerRenderer<RedEyeEntity> {

    /**
     * The resource location for a texture that matches the model texture in size but highlights the parts that will
     * light.
     */
    private static final ResourceLocation RED_EYE = new ResourceLocation(MODID, "textures/entity/spiders/redeyered.png");
    /**
     * The resource location for the geckolib model of the entity that has this layer.
     */
    private final ResourceLocation RED_EYE_MODEL = new ResourceLocation(MODID, "geo/spiders/redeye.json");

    public RedEyeLayer(IGeoRenderer<RedEyeEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                       RedEyeEntity entity, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType eyes = RenderType.eyes(RED_EYE);
        float light = Mth.sin(entity.tickCount%40/40f * Mth.PI) * 0.5f + 0.25f;
        this.getRenderer().render(this.getEntityModel().getModel(RED_EYE_MODEL), entity, partialTicks,
                eyes, matrixStackIn, bufferIn, bufferIn.getBuffer(eyes), packedLightIn, OverlayTexture.NO_OVERLAY,
                light, light, light, 1f);
    }
}
