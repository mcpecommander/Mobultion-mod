package dev.mcpecommander.mobultion.entities.spiders.renderers;

import dev.mcpecommander.mobultion.entities.spiders.entities.HypnoSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.models.HypnoSpiderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* Created by McpeCommander on 2021/06/18 */
public class HypnoSpiderRenderer extends GeoEntityRenderer<HypnoSpiderEntity> {

    public HypnoSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HypnoSpiderModel());
        this.shadowRadius = 0.7F;
    }
}
