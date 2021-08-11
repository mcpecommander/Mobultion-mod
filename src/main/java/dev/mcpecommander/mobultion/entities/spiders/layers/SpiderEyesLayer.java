package dev.mcpecommander.mobultion.entities.spiders.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.mcpecommander.mobultion.entities.spiders.entities.MobultionSpiderEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.AnimationUtils;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* Created by McpeCommander on 2021/06/18 */
public class SpiderEyesLayer<T extends MobultionSpiderEntity> extends GeoLayerRenderer<T> {

    /**
     * The resource location for a texture that matches the model texture in size but highlights the parts that will
     * light.
     */
    private final ResourceLocation SPIDER_EYES;
    /**
     * The resource location for the geckolib model of the entity that has this layer.
     */
    private final ResourceLocation SPIDER_MODEL;

    public SpiderEyesLayer(IGeoRenderer<T> entityRendererIn, String spiderName) {
        super(entityRendererIn);
        SPIDER_EYES = spiderName.equals("witchspider") ? new ResourceLocation(MODID,"textures/entity/spiders/witchspidereyes.png")
                : new ResourceLocation(MODID,"textures/entity/spiders/spidereyes.png");
        SPIDER_MODEL = new ResourceLocation(MODID, "geo/spiders/" + spiderName + ".json");;
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn, T entity, float limbSwing,
                       float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IGeoRenderer<T> renderer = (IGeoRenderer<T>) AnimationUtils.getRenderer(entity);
        renderer.render(getEntityModel().getModel(SPIDER_MODEL), entity, partialTicks, RenderType.eyes(SPIDER_EYES),matrixStack,
                buffer, buffer.getBuffer(RenderType.eyes(SPIDER_EYES)), packedLightIn, OverlayTexture.pack(0,
                        OverlayTexture.v(entity.hurtTime > 0)), 1f, 1f, 1f, 1f);
    }

}
