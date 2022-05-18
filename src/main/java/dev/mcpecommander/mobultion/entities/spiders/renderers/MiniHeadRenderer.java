package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.mcpecommander.mobultion.entities.spiders.entities.MiniHeadEntity;
import dev.mcpecommander.mobultion.entities.spiders.models.MiniHeadModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

/* Created by McpeCommander on 2021/06/18 */
public class MiniHeadRenderer extends GeoProjectilesRenderer<MiniHeadEntity> {

    public MiniHeadRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MiniHeadModel());
    }

    @Override
    public void render(@NotNull MiniHeadEntity entityIn, float entityYaw, float partialTicks,
                       @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.scale(0.3f, 0.3f, 0.3f);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
    }

}
