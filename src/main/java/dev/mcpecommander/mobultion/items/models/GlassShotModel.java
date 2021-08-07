package dev.mcpecommander.mobultion.items.models;

import dev.mcpecommander.mobultion.items.GlassShotItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 07/08/2021 inside the package - dev.mcpecommander.mobultion.items.models */
public class GlassShotModel extends AnimatedGeoModel<GlassShotItem> {

    @Override
    public ResourceLocation getModelLocation(GlassShotItem item) {
        return new ResourceLocation(MODID, "geo/items/glassshot.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GlassShotItem item) {
        return new ResourceLocation(MODID, "textures/entity/glassshot.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GlassShotItem item) {
        return new ResourceLocation(MODID, "animations/endermen/glassshot.animation.json");
    }
}