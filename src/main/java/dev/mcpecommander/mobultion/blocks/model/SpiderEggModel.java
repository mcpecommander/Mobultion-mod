package dev.mcpecommander.mobultion.blocks.model;

import dev.mcpecommander.mobultion.blocks.SpiderEggBlock;
import dev.mcpecommander.mobultion.blocks.tile.SpiderEggTile;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
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

    @Override
    public void setLivingAnimations(SpiderEggTile entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone All2 = this.getAnimationProcessor().getBone("All2");
        IBone All3 = this.getAnimationProcessor().getBone("All3");
        IBone All4 = this.getAnimationProcessor().getBone("All4");
        IBone All5 = this.getAnimationProcessor().getBone("All5");
        switch (entity.getBlockState().getValue(SpiderEggBlock.EGGS)) {
            case 1 -> {
                All2.setHidden(true);
                All3.setHidden(true);
                All4.setHidden(true);
                All5.setHidden(true);
            }
            case 2 -> {
                All2.setHidden(false);
                All3.setHidden(true);
                All4.setHidden(true);
                All5.setHidden(true);
            }
            case 3 -> {
                All2.setHidden(false);
                All3.setHidden(false);
                All4.setHidden(true);
                All5.setHidden(true);
            }
            case 4 -> {
                All2.setHidden(false);
                All3.setHidden(false);
                All4.setHidden(false);
                All5.setHidden(true);
            }
            case 5 -> {
                All2.setHidden(false);
                All3.setHidden(false);
                All4.setHidden(false);
                All5.setHidden(false);
            }
        }

    }
}
