package dev.mcpecommander.mobultion.entities.zombies.models;

import dev.mcpecommander.mobultion.entities.zombies.entities.HungryZombieEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 28/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.models */
public class HungryZombieModel extends AnimatedGeoModel<HungryZombieEntity> {

    /**
     * Gets the model json file.
     * @param entity: The entity for which the model file is getting called.
     * @return A resource location for the model file.
     */
    @Override
    public ResourceLocation getModelLocation(HungryZombieEntity entity) {
        return new ResourceLocation(MODID, "geo/zombies/hungryzombie.json");
    }

    /**
     * Gets the texture file for the model.
     * @param entity: The entity for which the texture will be applied on.
     * @return A resource location for the texture file.
     */
    @Override
    public ResourceLocation getTextureLocation(HungryZombieEntity entity) {
        return new ResourceLocation(MODID, "textures/entity/zombies/hungryzombie.png");
    }

    /**
     * Gets the animation file
     * @param animatable: The entity for which the animation file is being called.
     * @return A resource location for the animation file.
     */
    @Override
    public ResourceLocation getAnimationFileLocation(HungryZombieEntity animatable) {
        return new ResourceLocation(MODID, "animations/zombies/hungryzombie.animation.json");
    }

    /**
     * The animation ticking and rotation happens here.
     * @param entity: The entity that is being ticked.
     * @param uniqueID: The entity ID.
     * @param customPredicate: The animation event which has information about the animation.
     */
    @Override
    public void setLivingAnimations(HungryZombieEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if(entity.isDeadOrDying()) return;

        AnimationController controller = entity.getFactory().getOrCreateAnimationData(entity.getId())
                .getAnimationControllers().get("controller");
        AnimationController movement = entity.getFactory().getOrCreateAnimationData(entity.getId())
                .getAnimationControllers().get("movement");

        if(!(movement.getCurrentAnimation() != null && movement.getCurrentAnimation().animationName.equals("eat") &&
                movement.getAnimationState() == AnimationState.Running)){
            IBone head = this.getAnimationProcessor().getBone("Head");
            assert customPredicate != null;
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
        if((controller.getCurrentAnimation() == null || controller.getAnimationState() == AnimationState.Stopped)
                && (movement.getCurrentAnimation() == null || movement.getAnimationState() == AnimationState.Stopped)){
            IBone rightArm = this.getAnimationProcessor().getBone("RightArm");
            IBone leftArm = this.getAnimationProcessor().getBone("LeftArm");
            IBone rightArm1 = this.getAnimationProcessor().getBone("RightArm1");
            IBone leftArm1 = this.getAnimationProcessor().getBone("LeftArm1");
            rightArm.setRotationY((float) (-7.5 * Math.PI / 180F));
            leftArm.setRotationY((float) (7.5 * Math.PI / 180F));
            rightArm.setRotationX((float) (Math.sin(entity.tickCount/10F) * 4 * Math.PI / 180F));
            leftArm.setRotationX((float) (Math.sin(entity.tickCount/10F + Math.PI) * 4 * Math.PI / 180F));
            rightArm1.setRotationX((float) Math.abs(Math.sin(entity.tickCount/10F) * 6 * Math.PI / 180F));
            leftArm1.setRotationX((float) Math.abs(Math.sin(entity.tickCount/10F + Math.PI) * 6 * Math.PI / 180F));
        }
    }
}
