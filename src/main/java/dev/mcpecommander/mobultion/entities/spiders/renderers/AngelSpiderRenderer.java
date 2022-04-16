package dev.mcpecommander.mobultion.entities.spiders.renderers;

import dev.mcpecommander.mobultion.entities.spiders.entities.AngelSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.AngelRingLayer;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderEyesLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.AngelSpiderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* Created by McpeCommander on 2021/06/18 */
public class AngelSpiderRenderer extends GeoEntityRenderer<AngelSpiderEntity> {

    public AngelSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AngelSpiderModel());
        this.shadowRadius = 0.7F;
        this.addLayer(new SpiderEyesLayer<>(this, "angelspider"));
        this.addLayer(new AngelRingLayer(this));
    }


}
