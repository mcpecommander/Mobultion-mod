package dev.mcpecommander.mobultion.entities.spiders.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.mcpecommander.mobultion.entities.spiders.entities.WitchSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.models.WitchSpiderModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* Created by McpeCommander on 2021/06/18 */
public class WitchSpiderRenderer extends GeoEntityRenderer<WitchSpiderEntity> {

    public WitchSpiderRenderer(EntityRendererManager renderManager) {
        super(renderManager, new WitchSpiderModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    public void render(WitchSpiderEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if(entity.isDeadOrDying()) this.shadowRadius = 0f;
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}
