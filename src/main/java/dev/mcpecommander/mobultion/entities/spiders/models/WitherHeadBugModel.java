package dev.mcpecommander.mobultion.entities.spiders.models;

import dev.mcpecommander.mobultion.entities.spiders.entities.WitherHeadBugEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 19/06/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.models */
public class WitherHeadBugModel extends AnimatedGeoModel<WitherHeadBugEntity> {

    /**
     * Gets the model json file.
     *
     * @param entity: The entity for which the model file is getting called.
     * @return A resource location for the model file.
     */
    @Override
    public ResourceLocation getModelLocation(WitherHeadBugEntity entity) {
        return new ResourceLocation(MODID, "geo/spiders/witherheadbug.json");
    }

    /**
     * Gets the texture file for the model.
     *
     * @param entity: The entity for which the texture will be applied on.
     * @return A resource location for the texture file.
     */
    @Override
    public ResourceLocation getTextureLocation(WitherHeadBugEntity entity) {
        return new ResourceLocation(MODID, "textures/entity/spiders/witherheadbug.png");
    }

    /**
     * Gets the animation file
     *
     * @param animatable: The entity for which the animation file is being called.
     * @return A resource location for the animation file.
     */
    @Override
    public ResourceLocation getAnimationFileLocation(WitherHeadBugEntity animatable) {
        return new ResourceLocation(MODID, "animations/spiders/witherheadbug.animation.json");
    }

    /**
     * The animation ticking and rotation happens here.
     *
     * @param entity:          The entity that is being ticked.
     * @param uniqueID:        The entity ID.
     * @param customPredicate: The animation event which has information about the animation.
     */
    @Override
    public void setLivingAnimations(WitherHeadBugEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        assert customPredicate != null;
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        if (entity.isDeadOrDying()) {
            head.setRotationX(0f);
            head.setRotationY(0f);
        }

    }
}