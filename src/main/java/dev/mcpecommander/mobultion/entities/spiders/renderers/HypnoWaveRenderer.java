package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.mcpecommander.mobultion.entities.spiders.entities.HypnoWaveEntity;
import dev.mcpecommander.mobultion.entities.spiders.models.HypnoWaveModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

/* Created by McpeCommander on 2021/06/18 */
public class HypnoWaveRenderer extends GeoProjectilesRenderer<HypnoWaveEntity> {

    public HypnoWaveRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HypnoWaveModel());
    }

    @Override
    public void render(@NotNull HypnoWaveEntity entityIn, float entityYaw, float partialTicks,
                       @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.scale(1.5f, 1.5f, 1.5f);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
    }
}
