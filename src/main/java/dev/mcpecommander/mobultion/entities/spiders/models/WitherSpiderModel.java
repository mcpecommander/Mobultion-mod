package dev.mcpecommander.mobultion.entities.spiders.models;

import dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static dev.mcpecommander.mobultion.Mobultion.MODID;
import static dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity.Head.LEFT;
import static dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity.Head.RIGHT;

/* McpeCommander created on 18/06/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.models */
public class WitherSpiderModel extends AnimatedGeoModel<WitherSpiderEntity> {

    /**
     * Gets the model json file.
     *
     * @param entity: The entity for which the model file is getting called.
     * @return A resource location for the model file.
     */
    @Override
    public ResourceLocation getModelLocation(WitherSpiderEntity entity) {
        return new ResourceLocation(MODID, "geo/spiders/witherspider.json");
    }

    /**
     * Gets the texture file for the model.
     *
     * @param entity: The entity for which the texture will be applied on.
     * @return A resource location for the texture file.
     */
    @Override
    public ResourceLocation getTextureLocation(WitherSpiderEntity entity) {
        return new ResourceLocation(MODID, "textures/entity/spiders/witherspider.png");
    }

    /**
     * Gets the animation file
     *
     * @param animatable: The entity for which the animation file is being called.
     * @return A resource location for the animation file.
     */
    @Override
    public ResourceLocation getAnimationFileLocation(WitherSpiderEntity animatable) {
        return new ResourceLocation(MODID, "animations/spiders/witherspider.animation.json");
    }

    /**
     * The animation ticking and rotation happens here.
     *
     * @param entity:          The entity that is being ticked.
     * @param uniqueID:        The entity ID.
     * @param customPredicate: The animation event which has information about the animation.
     */
    @Override
    public void setLivingAnimations(WitherSpiderEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone mainHead = this.getAnimationProcessor().getBone("Head");
        IBone leftHeadBone = this.getAnimationProcessor().getBone("Head1");
        IBone leftNeckBone = this.getAnimationProcessor().getBone("HeadBase1");
        IBone rightHeadBone = this.getAnimationProcessor().getBone("Head2");
        IBone rightNeckBone = this.getAnimationProcessor().getBone("HeadBase2");

        //In case the entity was killed directly from max (or near) max health.
        mainHead.setHidden(entity.isDeadOrDying());

        if(customPredicate != null && customPredicate.getController() != null
                && customPredicate.getController().getCurrentAnimation() != null){
            //Set hidden for the head1 after the animation has stopped.
            if(customPredicate.getController().getCurrentAnimation().animationName.equals("head1_drop")
                    && customPredicate.getController().getAnimationState() == AnimationState.Stopped){
                leftHeadBone.setHidden(true);
            }else if(customPredicate.getController().getCurrentAnimation().animationName.equals("head1_drop")
                    && customPredicate.getController().getAnimationState() != AnimationState.Stopped){
                leftHeadBone.setHidden(false);
            }
            //Set hidden for the head2 after the animation has stopped.
            if(customPredicate.getController().getCurrentAnimation().animationName.equals("head2_drop")
                    && customPredicate.getController().getAnimationState() == AnimationState.Stopped){
                rightHeadBone.setHidden(true);
            }else if(customPredicate.getController().getCurrentAnimation().animationName.equals("head2_drop")
                    && customPredicate.getController().getAnimationState() != AnimationState.Stopped){
                rightHeadBone.setHidden(false);
            }
        }else{
            leftHeadBone.setHidden(entity.getHealth() <= 2f/3f * entity.getMaxHealth());
            rightHeadBone.setHidden(entity.getHealth() <= 1f/3f * entity.getMaxHealth());
        }

        //Add particles to the decapitated head
        if(entity.getHealth() <= 2f/3f * entity.getMaxHealth() && entity.tickCount % 6 == 0){
            Vec3 decapHead1 = new Vec3( (10f/16f), (19d/16d), (9.3f/16f));
            decapHead1 = decapHead1.yRot((float) Math.toRadians(-entity.yBodyRot)).add(entity.position());
            entity.level.addParticle(ParticleTypes.DRAGON_BREATH, decapHead1.x, decapHead1.y, decapHead1.z,
                    0, 0, 0);
            entity.level.addParticle(ParticleTypes.SMOKE, decapHead1.x, decapHead1.y, decapHead1.z,
                    Math.random() * 0.05f - 0.025f, 0.05f, Math.random() * 0.05f - 0.025f);
        }
        if(entity.getHealth() <= 1f/3f * entity.getMaxHealth() && entity.tickCount % 6 == 0){
            Vec3 decapHead2 = new Vec3( (-10f/16f), (19d/16d), (9.3f/16f));
            decapHead2 = decapHead2.yRot((float) Math.toRadians(-entity.yBodyRot)).add(entity.position());
            entity.level.addParticle(ParticleTypes.DRAGON_BREATH, decapHead2.x, decapHead2.y, decapHead2.z,
                    0, 0, 0);
            entity.level.addParticle(ParticleTypes.SMOKE, decapHead2.x, decapHead2.y, decapHead2.z,
                    Math.random() * 0.05f - 0.025f, 0.05f, Math.random() * 0.05f - 0.025f);
        }

        assert customPredicate != null;
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        //PI/180 is to convert from degrees to radians.
        mainHead.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        mainHead.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        if (entity.isDeadOrDying()) {
            mainHead.setRotationX(0f);
            mainHead.setRotationY(0f);
        }

        /*
        Left head rotation logic
         */
        float leftHeadYRotation = entity.headYRot[LEFT.number];
        //Wrap the head degrees.
        if(leftHeadYRotation > 360){
            entity.headYRot[LEFT.number] -= 360;
        }
        if(leftHeadYRotation < -360){
            entity.headYRot[LEFT.number] += 360;
        }
        //Limit the head rotation.
        float leftHeadYTotalRotation = entity.headYRot[LEFT.number] - entity.yBodyRot;
        if(leftHeadYTotalRotation < -30){
            entity.headYRot[LEFT.number] = -30 + entity.yBodyRot;
        }
        if(leftHeadYTotalRotation > 100){
            entity.headYRot[LEFT.number] = 100 + entity.yBodyRot;
        }
        leftHeadYTotalRotation = entity.headYRot[LEFT.number] - entity.yBodyRot;
        //PI/180 is to convert from degrees to radians.
        if(entity.getHealth() > 2f/3f * entity.getMaxHealth()) {
            leftHeadBone.setRotationX(-entity.headXRot[LEFT.number]  * ((float) Math.PI / 180F));
            leftHeadBone.setRotationY(leftHeadYTotalRotation * ((float) Math.PI / 180F));
        }else{
            leftHeadBone.setRotationX(0f);
            leftHeadBone.setRotationY(0f);
        }
        //Rotate the neck according to the head rotation.
        if(leftHeadYTotalRotation > 0 && leftHeadYTotalRotation < 90){
            leftNeckBone.setRotationY(leftHeadYTotalRotation/2f * ((float) Math.PI / 180F));
        } else if (leftHeadYTotalRotation >= 90) {
            leftNeckBone.setRotationY(45 * ((float) Math.PI / 180F));
        }else {
            leftNeckBone.setRotationY(0f);
        }

        /*
        Right head rotation logic
         */
        float rightHeadYRotation = entity.headYRot[RIGHT.number];
        //Wrap the head degrees.
        if(rightHeadYRotation > 360){
            entity.headYRot[RIGHT.number] -= 360;
        }
        if(rightHeadYRotation < -360){
            entity.headYRot[RIGHT.number] += 360;
        }
        //Limit the head rotation.
        float rightHeadYTotalRotation = entity.headYRot[RIGHT.number] - entity.yBodyRot;
        if(rightHeadYTotalRotation > 30){
            entity.headYRot[RIGHT.number] = 30 + entity.yBodyRot;
        }
        if(rightHeadYTotalRotation < -100){
            entity.headYRot[RIGHT.number] = -100 + entity.yBodyRot;
        }
        rightHeadYTotalRotation = entity.headYRot[RIGHT.number] - entity.yBodyRot;
        //PI/180 is to convert from degrees to radians.
        if(entity.getHealth() > 2f/3f * entity.getMaxHealth()) {
            rightHeadBone.setRotationX(-entity.headXRot[RIGHT.number]  * ((float) Math.PI / 180F));
            rightHeadBone.setRotationY(rightHeadYTotalRotation * ((float) Math.PI / 180F));
        }else{
            rightHeadBone.setRotationX(0f);
            rightHeadBone.setRotationY(0f);
        }
        //Rotate the neck according to the head rotation.
        if(rightHeadYTotalRotation < 0 && rightHeadYTotalRotation > -90){
            rightNeckBone.setRotationY(rightHeadYTotalRotation/2f * ((float) Math.PI / 180F));
        } else if (rightHeadYTotalRotation <= -90) {
            rightNeckBone.setRotationY(-45 * ((float) Math.PI / 180F));
        }else {
            rightNeckBone.setRotationY(0f);
        }

    }
}