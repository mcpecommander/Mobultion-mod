package dev.mcpecommander.mobultion.entities.spiders.renderers;

import dev.mcpecommander.mobultion.entities.spiders.entities.MiniSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderEyesLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.MiniSpiderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.renderers */
public class MiniSpiderRenderer extends GeoEntityRenderer<MiniSpiderEntity> {

    public MiniSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MiniSpiderModel());
        this.shadowRadius = 0.4F;
        this.addLayer(new SpiderEyesLayer<>(this, "minispider"));
    }

}
