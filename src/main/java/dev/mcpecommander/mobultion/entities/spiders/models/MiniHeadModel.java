package dev.mcpecommander.mobultion.entities.spiders.models;

import dev.mcpecommander.mobultion.entities.spiders.entities.MiniHeadEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 08/08/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.models */
public class MiniHeadModel extends AnimatedGeoModel<MiniHeadEntity> {
    /**
     * Gets the model json file.
     * @param entity: The entity for which the model file is getting called.
     * @return A resource location for the model file.
     */
    @Override
    public ResourceLocation getModelLocation(MiniHeadEntity entity) {
        return new ResourceLocation(MODID, "geo/spiders/redeye.json");
    }

    /**
     * Gets the texture file for the model.
     * @param entity: The entity for which the texture will be applied on.
     * @return A resource location for the texture file.
     */
    @Override
    public ResourceLocation getTextureLocation(MiniHeadEntity entity) {
        return new ResourceLocation(MODID, "textures/entity/spiders/redeye.png");
    }

    /**
     * Gets the animation file
     * @param animatable: The entity for which the animation file is being called.
     * @return A resource location for the animation file.
     */
    @Override
    public ResourceLocation getAnimationFileLocation(MiniHeadEntity animatable) {
        return null;
    }

    /**
     * The animation ticking and rotation happens here.
     * @param entity: The entity that is being ticked.
     * @param uniqueID: The entity ID.
     * @param customPredicate: The animation event which has information about the animation.
     */
    @Override
    public void setLivingAnimations(MiniHeadEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone main = this.getAnimationProcessor().getBone("main");

        double xDist = entity.target.x - entity.position().x;
        double zDist = entity.target.z - entity.position().z;
        main.setRotationX((float) -(Mth.atan2(entity.target.y - entity.position().y, Math.sqrt(xDist * xDist + zDist * zDist))));
        main.setRotationY((float) (Mth.atan2(zDist, xDist) - Math.PI));

    }
}
