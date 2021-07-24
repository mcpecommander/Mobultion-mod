package dev.mcpecommander.mobultion.entities.skeletons.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.mcpecommander.mobultion.entities.skeletons.entities.MagmaSkeletonEntity;
import dev.mcpecommander.mobultion.entities.skeletons.entities.MobultionSkeletonEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.AnimationUtils;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 21/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.layers */
public class SkeletonMagmaLayer<T extends MobultionSkeletonEntity> extends GeoLayerRenderer<T> {

    /**
     * The resource location for the overlay texture.
     */
    private static final ResourceLocation MAGMA = new ResourceLocation(MODID,"textures/entity/skeletons/skeletonmagma.png");
    /**
     * The resource location for the geckolib model of the entity that has this layer.
     */
    private final ResourceLocation MODEL = new ResourceLocation(MODID, "geo/skeletons/baseskeleton.json");

    public SkeletonMagmaLayer(IGeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrix, IRenderTypeBuffer bufferIn, int packedLightIn, T entity, float limbSwing,
                       float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(entity instanceof MagmaSkeletonEntity){
            RenderType cameo =  RenderType.eyes(MAGMA);
            ((IGeoRenderer<T>) AnimationUtils.getRenderer(entity)).render(this.getEntityModel().getModel(MODEL), entity, partialTicks,
                    cameo, matrix, bufferIn, bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        }
    }
}
