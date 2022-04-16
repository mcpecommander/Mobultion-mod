package dev.mcpecommander.mobultion.items.models;

import dev.mcpecommander.mobultion.items.ThunderStaffItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.models */
public class ThunderStaffModel extends AnimatedGeoModel<ThunderStaffItem> {

    @Override
    public ResourceLocation getModelLocation(ThunderStaffItem object)
    {
        return new ResourceLocation(MODID, "geo/items/thunderstaff.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ThunderStaffItem object)
    {
        return new ResourceLocation(MODID, "textures/item/thunderstaff.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ThunderStaffItem object)
    {
        return new ResourceLocation(MODID, "animations/items/thunderstaff.animation.json");
    }
}

