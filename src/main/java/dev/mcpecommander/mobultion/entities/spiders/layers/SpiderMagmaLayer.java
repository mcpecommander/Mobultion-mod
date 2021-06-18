package dev.mcpecommander.mobultion.entities.spiders.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.mcpecommander.mobultion.entities.spiders.entities.MagmaSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.renderers.MagmaSpiderRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
public class SpiderMagmaLayer extends GeoLayerRenderer<MagmaSpiderEntity> {

    /**
     * The resource location for a texture that matches the model texture in size but highlights the parts that will
     * light.
     */
    private static final ResourceLocation SPIDER_MAGMA = new ResourceLocation(MODID, "textures/entity/spidermagma.png");
    /**
     * The resource location for the geckolib model of the entity that has this layer.
     */
    private static final ResourceLocation MAGMA_SPIDER = new ResourceLocation(MODID, "geo/magmaspider.json");
    MagmaSpiderRenderer renderer;

    public SpiderMagmaLayer(IGeoRenderer<MagmaSpiderEntity> entityRendererIn) {
        super(entityRendererIn);
        this.renderer = (MagmaSpiderRenderer) entityRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, MagmaSpiderEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType eyes = RenderType.eyes(SPIDER_MAGMA);
        this.renderer.render(this.getEntityModel().getModel(MAGMA_SPIDER), entitylivingbaseIn, partialTicks, eyes, matrixStackIn, bufferIn, bufferIn.getBuffer(eyes), packedLightIn, OverlayTexture.NO_OVERLAY, 0.9f, 0.3f, 0f, 1f);
    }
}