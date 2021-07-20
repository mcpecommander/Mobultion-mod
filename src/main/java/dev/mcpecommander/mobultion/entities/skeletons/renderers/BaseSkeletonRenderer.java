package dev.mcpecommander.mobultion.entities.skeletons.renderers;

import dev.mcpecommander.mobultion.entities.skeletons.entities.MobultionSkeletonEntity;
import dev.mcpecommander.mobultion.entities.skeletons.models.BaseSkeletonModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* McpeCommander created on 20/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.renderers */
public class BaseSkeletonRenderer<T extends MobultionSkeletonEntity> extends GeoEntityRenderer<T> {

    public BaseSkeletonRenderer(EntityRendererManager renderManager) {
        super(renderManager, new BaseSkeletonModel<>());
        this.shadowRadius = 0.5F;
    }
}
