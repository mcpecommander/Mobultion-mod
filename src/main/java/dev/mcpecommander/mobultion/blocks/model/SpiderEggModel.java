package dev.mcpecommander.mobultion.blocks.model;

import dev.mcpecommander.mobultion.blocks.tile.SpiderEggTile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.blocks.model */
public class SpiderEggModel extends AnimatedGeoModel<SpiderEggTile> {

    /**
     * Gets the model json file.
     * @param animatable: The tile entity for which the model file is getting called.
     * @return A resource location for the model file.
     */
    @Override
    public ResourceLocation getModelLocation(SpiderEggTile animatable) {
        return new ResourceLocation(MODID, "geo/spiders/spideregg.json");
    }

    /**
     * Gets the texture file for the model.
     * @param animatable: The tile entity for which the texture will be applied on.
     * @return A resource location for the texture file.
     */
    @Override
    public ResourceLocation getTextureLocation(SpiderEggTile animatable) {
        return new ResourceLocation(MODID, "textures/entity/spiders/spideregg.png");
    }

    /**
     * Gets the animation file
     * @param animatable: The tile entity for which the animation file is being called.
     * @return A resource location for the animation file.
     */
    @Override
    public ResourceLocation getAnimationFileLocation(SpiderEggTile animatable) {
        return new ResourceLocation(MODID, "animations/spiders/spideregg.animation.json");
    }
}
