package dev.mcpecommander.mobultion.entities.spiders.renderers;

import dev.mcpecommander.mobultion.entities.spiders.entities.HypnoWaveEntity;
import dev.mcpecommander.mobultion.entities.spiders.models.HypnoWaveModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

/* Created by McpeCommander on 2021/06/18 */
public class HypnoWaveRenderer extends GeoProjectilesRenderer<HypnoWaveEntity> {

    public HypnoWaveRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HypnoWaveModel());
    }

}
