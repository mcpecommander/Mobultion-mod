package dev.mcpecommander.mobultion.entities.endermen.renderers;

import dev.mcpecommander.mobultion.entities.endermen.entities.MagmaEndermanEntity;
import dev.mcpecommander.mobultion.entities.endermen.layers.EndermanEyesLayer;
import dev.mcpecommander.mobultion.entities.endermen.layers.EndermanMagmaLayer;
import dev.mcpecommander.mobultion.entities.endermen.models.MagmaEndermanModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/* McpeCommander created on 26/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.renderers */
public class MagmaEndermanRenderer extends GeoEntityRenderer<MagmaEndermanEntity> {

    public MagmaEndermanRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MagmaEndermanModel());
        this.shadowRadius = 0.5F;
        this.addLayer(new EndermanEyesLayer<>(this, "magmaenderman"));
        this.addLayer(new EndermanMagmaLayer(this));
    }

    /**
     * How much the entity rotates when it dies. The default is 90 degrees like lying on the ground dead.
     * @param entity The entity that is dying.
     * @return a float of the degrees that this entity rotates on death.
     */
    @Override
    protected float getDeathMaxRotation(MagmaEndermanEntity entity) {
        return 0f;
    }
}