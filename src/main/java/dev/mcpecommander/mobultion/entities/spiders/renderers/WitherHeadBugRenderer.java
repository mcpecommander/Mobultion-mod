package dev.mcpecommander.mobultion.entities.spiders.renderers;

import dev.mcpecommander.mobultion.entities.spiders.entities.WitherHeadBugEntity;
import dev.mcpecommander.mobultion.entities.spiders.models.WitherHeadBugModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* McpeCommander created on 19/06/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.renderers */
public class WitherHeadBugRenderer extends GeoEntityRenderer<WitherHeadBugEntity> {

    public WitherHeadBugRenderer(EntityRendererManager renderManager) {
        super(renderManager, new WitherHeadBugModel());
        this.shadowRadius = 0.2F;
    }

}
