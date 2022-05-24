package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.mcpecommander.mobultion.entities.spiders.entities.WitheringWebEntity;
import dev.mcpecommander.mobultion.entities.spiders.models.WitheringWebModel;
import dev.mcpecommander.mobultion.utils.MathCalculations;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

/* Created by McpeCommander on 2021/06/18 */
public class WitheringWebRenderer extends GeoProjectilesRenderer<WitheringWebEntity> {

    public WitheringWebRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WitheringWebModel());
    }

    @Override
    public void render(@NotNull WitheringWebEntity entity, float entityYaw, float partialTicks,
                       @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        if(entity.getTarget() != null) {
            matrixStackIn.pushPose();
            float scaleFactor = (float) (0.5f + MathCalculations.map(entity.distanceTo(entity.getTarget()),
                    entity.originalDistance, 0, 0, 1.5));
            matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
            super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
        }
    }

}
