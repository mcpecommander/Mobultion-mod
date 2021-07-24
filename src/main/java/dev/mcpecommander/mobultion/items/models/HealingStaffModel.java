package dev.mcpecommander.mobultion.items.models;

import dev.mcpecommander.mobultion.items.HealingStaffItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 23/06/2021 inside the package - dev.mcpecommander.mobultion.items.models */
public class HealingStaffModel extends AnimatedGeoModel<HealingStaffItem> {

    @Override
    public ResourceLocation getModelLocation(HealingStaffItem item)
    {
        return new ResourceLocation(MODID, "geo/items/healingstaff.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HealingStaffItem item)
    {
        return new ResourceLocation(MODID, "textures/item/healingstaff.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HealingStaffItem item)
    {
        return new ResourceLocation(MODID, "animations/items/healingstaff.animation.json");
    }
}

