package dev.mcpecommander.mobultion.entities.skeletons.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.mcpecommander.mobultion.entities.skeletons.entities.ForestSkeletonEntity;
import dev.mcpecommander.mobultion.entities.skeletons.entities.MobultionSkeletonEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeColors;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.AnimationUtils;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 21/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.layers */
public class ForestCameoLayer<T extends MobultionSkeletonEntity> extends GeoLayerRenderer<T> {

    /**
     * The resource location for the overlay texture.
     */
    private static final ResourceLocation CAMEO = new ResourceLocation(MODID,"textures/entity/skeletons/forestskeleton_overlay.png");
    /**
     * The resource location for the geckolib model of the entity that has this layer.
     */
    private final ResourceLocation MODEL = new ResourceLocation(MODID, "geo/skeletons/baseskeleton.json");

    public ForestCameoLayer(IGeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrix, IRenderTypeBuffer bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(entity instanceof ForestSkeletonEntity){
            int color = BiomeColors.getAverageFoliageColor(entity.level, entity.blockPosition());
            float r = 1.0f;
            float g = 1.0f;
            float b = 1.0f;
            if(color != -1){
                r = (color >> 16 & 255) / 255.0f;
                g = (color >> 8 & 255) / 255.0f;
                b = (color & 255) / 255.0f;
            }
            matrix.pushPose();
            matrix.scale(1.05f, 1.01f, 1.05f);
            RenderType cameo =  RenderType.entityTranslucent(CAMEO);
            this.getRenderer().render(this.getEntityModel().getModel(MODEL), entity, partialTicks,
                    cameo, matrix, bufferIn, bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, 1f);
            matrix.popPose();
        }
    }
}
