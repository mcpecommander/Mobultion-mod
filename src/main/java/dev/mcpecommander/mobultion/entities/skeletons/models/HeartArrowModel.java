package dev.mcpecommander.mobultion.entities.skeletons.models;

import dev.mcpecommander.mobultion.entities.skeletons.entities.HeartArrowEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 23/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.models */
public class HeartArrowModel extends AnimatedGeoModel<HeartArrowEntity> {

    /**
     * Gets the model json file.
     * @param entity: The entity for which the model file is getting called.
     * @return A resource location for the model file.
     */
    @Override
    public ResourceLocation getModelLocation(HeartArrowEntity entity) {
        return new ResourceLocation(MODID, "geo/skeletons/heartarrow.json");
    }

    /**
     * Gets the texture file for the model.
     * @param entity: The entity for which the texture will be applied on.
     * @return A resource location for the texture file.
     */
    @Override
    public ResourceLocation getTextureLocation(HeartArrowEntity entity) {
        return new ResourceLocation(MODID, "textures/entity/heartarrow.png");
    }

    /**
     * Gets the animation file
     * @param animatable: The entity for which the animation file is being called.
     * @return A resource location for the animation file.
     */
    @Override
    public ResourceLocation getAnimationFileLocation(HeartArrowEntity animatable) {
        return new ResourceLocation(MODID, "animations/skeletons/heartarrow.animation.json");
    }

    /**
     * The animation ticking and rotation happens here.
     * @param entity: The entity that is being ticked.
     * @param uniqueID: The entity ID.
     * @param customPredicate: The animation event which has information about the animation.
     */
    @Override
    public void setLivingAnimations(HeartArrowEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone main = this.getAnimationProcessor().getBone("Main");

        assert customPredicate != null;
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        main.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        main.setRotationY((extraData.netHeadYaw - 90) * ((float) Math.PI / 180F));

    }
}
