package dev.mcpecommander.mobultion.entities.zombies.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.mcpecommander.mobultion.entities.zombies.entities.MagmaZombieEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.AnimationUtils;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 21/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.layers */
public class ZombieMagmaLayer extends GeoLayerRenderer<MagmaZombieEntity> {

    /**
     * The resource location for the overlay texture.
     */
    private static final ResourceLocation MAGMA = new ResourceLocation(MODID,"textures/entity/zombies/zombiemagma.png");
    /**
     * The resource location for the geckolib model of the entity that has this layer.
     */
    private final ResourceLocation MODEL = new ResourceLocation(MODID, "geo/zombies/advancedbiped.json");

    public ZombieMagmaLayer(IGeoRenderer<MagmaZombieEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrix, IRenderTypeBuffer bufferIn, int packedLightIn, MagmaZombieEntity entity, float limbSwing,
                       float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType cameo =  RenderType.eyes(MAGMA);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entity, partialTicks, cameo, matrix, bufferIn,
                bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
    }
}
