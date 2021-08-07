package dev.mcpecommander.mobultion.items.models;

import dev.mcpecommander.mobultion.items.HaloItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 07/08/2021 inside the package - dev.mcpecommander.mobultion.items.models */
public class HaloModel extends AnimatedGeoModel<HaloItem> {

    @Override
    public ResourceLocation getModelLocation(HaloItem item) {
        return new ResourceLocation(MODID, "geo/items/halo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HaloItem item) {
        return new ResourceLocation(MODID, "textures/item/halo.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HaloItem item) {
        return null;
    }
}