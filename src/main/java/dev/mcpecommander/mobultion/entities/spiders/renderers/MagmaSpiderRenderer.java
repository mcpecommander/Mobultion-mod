package dev.mcpecommander.mobultion.entities.spiders.renderers;

import dev.mcpecommander.mobultion.entities.spiders.entities.MagmaSpiderEntity;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderEyesLayer;
import dev.mcpecommander.mobultion.entities.spiders.layers.SpiderMagmaLayer;
import dev.mcpecommander.mobultion.entities.spiders.models.MagmaSpiderModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* Created by McpeCommander on 2021/06/18 */
public class MagmaSpiderRenderer extends GeoEntityRenderer<MagmaSpiderEntity> {

    public MagmaSpiderRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MagmaSpiderModel());
        this.shadowRadius = 0.7F;
        this.addLayer(new SpiderMagmaLayer(this));
        this.addLayer(new SpiderEyesLayer(this, "magmaspider"));
    }
}