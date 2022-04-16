package dev.mcpecommander.mobultion.entities.spiders.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.mcpecommander.mobultion.entities.spiders.entities.AngelSpiderEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
public class AngelRingLayer extends GeoLayerRenderer<AngelSpiderEntity> {

    /**
     * The resource location for a texture that matches the model texture in size but highlights the parts that will
     * light.
     */
    private static final ResourceLocation SPIDER_RING = new ResourceLocation(MODID, "textures/entity/spiders/angelring.png");
    /**
     * The resource location for the geckolib model of the entity that has this layer.
     */
    private static final ResourceLocation ANGEL_SPIDER = new ResourceLocation(MODID, "geo/spiders/angelspider.json");

    public AngelRingLayer(IGeoRenderer<AngelSpiderEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AngelSpiderEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType ring = RenderType.eyes(SPIDER_RING);
        this.getRenderer().render(this.getEntityModel().getModel(ANGEL_SPIDER), entitylivingbaseIn, partialTicks, ring, matrixStackIn, bufferIn, bufferIn.getBuffer(ring), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1f);
    }
}