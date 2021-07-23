package dev.mcpecommander.mobultion.entities.skeletons.models;

import dev.mcpecommander.mobultion.entities.skeletons.entities.CorruptedSkeletonEntity;
import dev.mcpecommander.mobultion.entities.skeletons.entities.MagmaSkeletonEntity;
import dev.mcpecommander.mobultion.entities.skeletons.entities.MobultionSkeletonEntity;
import dev.mcpecommander.mobultion.entities.skeletons.entities.VampireSkeletonEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 20/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.models */
public class BaseSkeletonModel<T extends MobultionSkeletonEntity> extends AnimatedGeoModel<T> {

    /**
     * Gets the model json file.
     * @param entity: The entity for which the model file is getting called.
     * @return A resource location for the model file.
     */
    @Override
    public ResourceLocation getModelLocation(T entity) {
        return new ResourceLocation(MODID, "geo/skeletons/baseskeleton.json");
    }

    /**
     * Gets the texture file for the model.
     * @param entity: The entity for which the texture will be applied on.
     * @return A resource location for the texture file.
     */
    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if(entity instanceof CorruptedSkeletonEntity){
            return new ResourceLocation(MODID, "textures/entity/skeletons/corruptedskeleton.png");
        }else if(entity instanceof VampireSkeletonEntity){
            return new ResourceLocation(MODID, "textures/entity/skeletons/vampireskeleton.png");
        }else if(entity instanceof MagmaSkeletonEntity){
            return new ResourceLocation(MODID, "textures/entity/skeletons/magmaskeleton.png");
        }
        return new ResourceLocation(MODID, "textures/entity/skeletons/baseskeleton.png");
    }

    /**
     * Gets the animation file
     * @param animatable: The entity for which the animation file is being called.
     * @return A resource location for the animation file.
     */
    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return new ResourceLocation(MODID, "animations/skeletons/baseskeleton.animation.json");
    }

    /**
     * The animation ticking and rotation happens here.
     * @param entity: The entity that is being ticked.
     * @param uniqueID: The entity ID.
     * @param customPredicate: The animation event which has information about the animation.
     */
    @Override
    public void setLivingAnimations(T entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");

        assert customPredicate != null;
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

    }
}
