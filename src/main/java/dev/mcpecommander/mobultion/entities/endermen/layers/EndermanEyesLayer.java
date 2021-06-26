package dev.mcpecommander.mobultion.entities.endermen.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.mcpecommander.mobultion.entities.endermen.entities.WanderingEndermanEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 25/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.layers */
public class EndermanEyesLayer extends GeoLayerRenderer<WanderingEndermanEntity> {

    /**
     * The resource location for a texture that matches the model texture in size but highlights the parts that will
     * light.
     */
    private static final ResourceLocation ENDERMAN_EYES = new ResourceLocation(MODID, "textures/entity/endermen/wanderingendermaneyes.png");
    /**
     * The resource location for the geckolib model of the entity that has this layer.
     */
    private static final ResourceLocation ENDERMAN_MODEL = new ResourceLocation(MODID, "geo/endermen/wanderingenderman.json");
    IGeoRenderer<WanderingEndermanEntity> renderer;

    public EndermanEyesLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
        this.renderer = entityRendererIn;

    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, WanderingEndermanEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType eyes = RenderType.eyes(ENDERMAN_EYES);
        this.renderer.render(this.getEntityModel().getModel(ENDERMAN_MODEL), entitylivingbaseIn, partialTicks, eyes, matrixStackIn, bufferIn, bufferIn.getBuffer(eyes), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1f);
    }
}