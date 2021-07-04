package dev.mcpecommander.mobultion.entities.endermen.models;

import dev.mcpecommander.mobultion.entities.endermen.entities.GlassShotEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 03/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.models */
public class GlassShotModel extends AnimatedGeoModel<GlassShotEntity> {
    /**
     * Gets the model json file.
     *
     * @param entity: The entity for which the model file is getting called.
     * @return A resource location for the model file.
     */
    @Override
    public ResourceLocation getModelLocation(GlassShotEntity entity) {
        return new ResourceLocation(MODID, "geo/endermen/glassshot.geo.json");
    }

    /**
     * Gets the texture file for the model.
     *
     * @param entity: The entity for which the texture will be applied on.
     * @return A resource location for the texture file.
     */
    @Override
    public ResourceLocation getTextureLocation(GlassShotEntity entity) {
        return new ResourceLocation(MODID, "textures/entity/endermen/glassshot.png");
    }

    /**
     * Gets the animation file
     *
     * @param animatable: The entity for which the animation file is being called.
     * @return A resource location for the animation file.
     */
    @Override
    public ResourceLocation getAnimationFileLocation(GlassShotEntity animatable) {
        return new ResourceLocation(MODID, "animations/endermen/glassshot.animation.json");
    }

}
