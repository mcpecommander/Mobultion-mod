package dev.mcpecommander.mobultion.items.models;

import dev.mcpecommander.mobultion.items.JokerHatItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 18/08/2021 inside the package - dev.mcpecommander.mobultion.items.models */
public class JokerHatModel extends AnimatedGeoModel<JokerHatItem> {

    @Override
    public ResourceLocation getModelLocation(JokerHatItem object)
    {
        return new ResourceLocation(MODID, "geo/items/jokerhat.json");
    }

    @Override
    public ResourceLocation getTextureLocation(JokerHatItem object)
    {
        return new ResourceLocation(MODID, "textures/item/jokerhat.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(JokerHatItem object)
    {
        return new ResourceLocation(MODID, "animations/items/jokerhat.animation.json");
    }
}
