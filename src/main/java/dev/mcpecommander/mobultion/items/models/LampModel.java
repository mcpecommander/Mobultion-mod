package dev.mcpecommander.mobultion.items.models;

import dev.mcpecommander.mobultion.items.LampItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 03/08/2021 inside the package - dev.mcpecommander.mobultion.items.models */
public class LampModel extends AnimatedGeoModel<LampItem> {

    @Override
    public ResourceLocation getModelLocation(LampItem object)
    {
        return new ResourceLocation(MODID, "geo/items/lamp.json");
    }

    @Override
    public ResourceLocation getTextureLocation(LampItem object)
    {
        return new ResourceLocation(MODID, "textures/item/lamp.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(LampItem object)
    {
        return null;
    }
}
