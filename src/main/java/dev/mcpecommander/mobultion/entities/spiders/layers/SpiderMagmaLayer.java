package dev.mcpecommander.mobultion.entities.spiders.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.mcpecommander.mobultion.entities.spiders.entities.MagmaSpiderEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
public class SpiderMagmaLayer extends GeoLayerRenderer<MagmaSpiderEntity> {

    /**
     * The resource location for a texture that matches the model texture in size but highlights the parts that will
     * light.
     */
    private static final ResourceLocation SPIDER_MAGMA = new ResourceLocation(MODID, "textures/entity/spiders/spidermagma.png");
    /**
     * The resource location for the geckolib model of the entity that has this layer.
     */
    private static final ResourceLocation MAGMA_SPIDER = new ResourceLocation(MODID, "geo/spiders/magmaspider.json");

    public SpiderMagmaLayer(IGeoRenderer<MagmaSpiderEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                       MagmaSpiderEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType eyes = RenderType.eyes(SPIDER_MAGMA);
        this.getRenderer().render(this.getEntityModel().getModel(MAGMA_SPIDER), entitylivingbaseIn, partialTicks, eyes,
                matrixStackIn, bufferIn, bufferIn.getBuffer(eyes), packedLightIn, OverlayTexture.NO_OVERLAY,
                0.9f, 0.3f, 0f, 1f);
    }
}