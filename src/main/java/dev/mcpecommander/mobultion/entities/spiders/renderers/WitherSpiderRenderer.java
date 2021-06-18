package dev.mcpecommander.mobultion.entities.spiders.renderers;

import dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderEyesLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.WitherSpiderModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* McpeCommander created on 18/06/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.renderers */
public class WitherSpiderRenderer extends GeoEntityRenderer<WitherSpiderEntity> {

    public WitherSpiderRenderer(EntityRendererManager renderManager) {
        super(renderManager, new WitherSpiderModel());
        this.shadowRadius = 0.7F;
        this.addLayer(new SpiderEyesLayer<>(this, "witherspider"));
    }



}
