package dev.mcpecommander.mobultion.entities.skeletons.renderers;

import dev.mcpecommander.mobultion.entities.skeletons.entities.HeartArrowEntity;
import dev.mcpecommander.mobultion.entities.skeletons.models.HeartArrowModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

/* McpeCommander created on 23/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.renderers */
public class HeartArrowRenderer extends GeoProjectilesRenderer<HeartArrowEntity> {

    public HeartArrowRenderer(EntityRendererManager renderManager) {
        super(renderManager, new HeartArrowModel());
    }

}
