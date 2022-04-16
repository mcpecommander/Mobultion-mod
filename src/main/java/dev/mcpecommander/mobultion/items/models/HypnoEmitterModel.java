package dev.mcpecommander.mobultion.items.models;

import dev.mcpecommander.mobultion.items.HypnoEmitterItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.models */
public class HypnoEmitterModel extends AnimatedGeoModel<HypnoEmitterItem> {

    @Override
    public ResourceLocation getModelLocation(HypnoEmitterItem object)
    {
        return new ResourceLocation(MODID, "geo/items/hypnoemitter.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HypnoEmitterItem object)
    {
        return new ResourceLocation(MODID, "textures/item/hypnoemitter.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HypnoEmitterItem object)
    {
        return new ResourceLocation(MODID, "animations/items/hypnoemitter.animation.json");
    }
}

