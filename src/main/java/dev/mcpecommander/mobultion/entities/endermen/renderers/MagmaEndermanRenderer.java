package dev.mcpecommander.mobultion.entities.endermen.renderers;

import dev.mcpecommander.mobultion.entities.endermen.entities.MagmaEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.layers.EndermanEyesLayer;
import dev.mcpecommander.mobultion.entities.endermen.layers.EndermanMagmaLayer;
import dev.mcpecommander.mobultion.entities.endermen.models.MagmaEndermanModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* McpeCommander created on 26/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.renderers */
public class MagmaEndermanRenderer extends GeoEntityRenderer<MagmaEndermanEntity> {

    public MagmaEndermanRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MagmaEndermanModel());
        this.shadowRadius = 0.5F;
        this.addLayer(new EndermanEyesLayer<>(this, "magmaenderman"));
        this.addLayer(new EndermanMagmaLayer(this));
    }


}