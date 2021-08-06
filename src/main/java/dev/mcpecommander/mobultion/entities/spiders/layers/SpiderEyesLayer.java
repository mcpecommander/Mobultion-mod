package dev.mcpecommander.mobultion.entities.spiders.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.mcpecommander.mobultion.entities.spiders.entities.MobultionSpiderEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
public class SpiderEyesLayer<T extends MobultionSpiderEntity> extends GeoLayerRenderer<T> {

    /**
     * The resource location for a texture that matches the model texture in size but highlights the parts that will
     * light.
     */
    private static final ResourceLocation SPIDER_EYES = new ResourceLocation(MODID,"textures/entity/spiders/spidereyes.png");
    /**
     * The resource location for the geckolib model of the entity that has this layer.
     */
    private final ResourceLocation SPIDER_MODEL;
    IGeoRenderer<T> renderer;

    public SpiderEyesLayer(IGeoRenderer<T> entityRendererIn, String spiderName) {
        super(entityRendererIn);
        this.renderer = entityRendererIn;
        SPIDER_MODEL = new ResourceLocation(MODID, "geo/spiders/" + spiderName + ".json");;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType eyes =  RenderType.eyes(SPIDER_EYES);
        this.renderer.render(this.getEntityModel().getModel(SPIDER_MODEL), entitylivingbaseIn, partialTicks, eyes, matrixStackIn, bufferIn, bufferIn.getBuffer(eyes), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1f);
    }

}
