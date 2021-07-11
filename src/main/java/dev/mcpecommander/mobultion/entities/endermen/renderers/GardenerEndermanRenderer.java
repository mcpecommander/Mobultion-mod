package dev.mcpecommander.mobultion.entities.endermen.renderers;

import dev.mcpecommander.mobultion.entities.endermen.entities.GardenerEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.layers.EndermanEyesLayer;
import dev.mcpecommander.mobultion.entities.endermen.models.GardenerEndermanModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* McpeCommander created on 26/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.renderers */
public class GardenerEndermanRenderer extends GeoEntityRenderer<GardenerEndermanEntity> {

    public GardenerEndermanRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GardenerEndermanModel());
        this.shadowRadius = 0.5F;
        this.addLayer(new EndermanEyesLayer<>(this, "gardenerenderman"));
    }

    @Override
    protected float getDeathMaxRotation(GardenerEndermanEntity entityLivingBaseIn) {
        return 0f;
    }
}