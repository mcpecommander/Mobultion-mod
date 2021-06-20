package dev.mcpecommander.mobultion.entities.spiders.models;

import dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

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
        return new ResourceLocation(MODID, "geo/witherspider.json");
    }

    /**
     * Gets the texture file for the model.
     *
     * @param entity: The entity for which the texture will be applied on.
     * @return A resource location for the texture file.
     */
    @Override
    public ResourceLocation getTextureLocation(WitherSpiderEntity entity) {
        return new ResourceLocation(MODID, "textures/entity/witherspider.png");
    }

    /**
     * Gets the animation file
     *
     * @param animatable: The entity for which the animation file is being called.
     * @return A resource location for the animation file.
     */
    @Override
    public ResourceLocation getAnimationFileLocation(WitherSpiderEntity animatable) {
        return new ResourceLocation(MODID, "animations/witherspider.animation.json");
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
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone head1 = this.getAnimationProcessor().getBone("Head1");
        IBone head2 = this.getAnimationProcessor().getBone("Head2");

        //In case the entity was killed directly from max (or near) max health.
        head.setHidden(entity.isDeadOrDying());

        if(customPredicate != null && customPredicate.getController() != null
                && customPredicate.getController().getCurrentAnimation() != null){
            //Set hidden for the head1 after the animation has stopped.
            if(customPredicate.getController().getCurrentAnimation().animationName.equals("head1_drop")
                    && customPredicate.getController().getAnimationState() == AnimationState.Stopped){
                head1.setHidden(true);
            }else if(customPredicate.getController().getCurrentAnimation().animationName.equals("head1_drop")
                    && customPredicate.getController().getAnimationState() != AnimationState.Stopped){
                head1.setHidden(false);
            }
            //Set hidden for the head2 after the animation has stopped.
            if(customPredicate.getController().getCurrentAnimation().animationName.equals("head2_drop")
                    && customPredicate.getController().getAnimationState() == AnimationState.Stopped){
                head2.setHidden(true);
            }else if(customPredicate.getController().getCurrentAnimation().animationName.equals("head2_drop")
                    && customPredicate.getController().getAnimationState() != AnimationState.Stopped){
                head2.setHidden(false);
            }
        }else{
            head1.setHidden(entity.getHealth() <= 2f/3f * entity.getMaxHealth());
            head2.setHidden(entity.getHealth() <= 1f/3f * entity.getMaxHealth());
        }


        if(entity.getHealth() <= 2f/3f * entity.getMaxHealth() && entity.tickCount % 6 == 0){
            Vector3d decapHead1 = new Vector3d( (10f/16f), (19d/16d), (9.3f/16f));
            decapHead1 = decapHead1.yRot((float) Math.toRadians(-entity.yBodyRot)).add(entity.position());
            entity.level.addParticle(ParticleTypes.DRAGON_BREATH, decapHead1.x, decapHead1.y, decapHead1.z,
                    0, 0, 0);
            entity.level.addParticle(ParticleTypes.SMOKE, decapHead1.x, decapHead1.y, decapHead1.z,
                    Math.random() * 0.05f - 0.025f, 0.05f, Math.random() * 0.05f - 0.025f);
        }

        if(entity.getHealth() <= 1f/3f * entity.getMaxHealth() && entity.tickCount % 6 == 0){
            Vector3d decapHead2 = new Vector3d( (-10f/16f), (19d/16d), (9.3f/16f));
            decapHead2 = decapHead2.yRot((float) Math.toRadians(-entity.yBodyRot)).add(entity.position());
            entity.level.addParticle(ParticleTypes.DRAGON_BREATH, decapHead2.x, decapHead2.y, decapHead2.z,
                    0, 0, 0);
            entity.level.addParticle(ParticleTypes.SMOKE, decapHead2.x, decapHead2.y, decapHead2.z,
                    Math.random() * 0.05f - 0.025f, 0.05f, Math.random() * 0.05f - 0.025f);
        }

        assert customPredicate != null;
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        if (entity.isDeadOrDying()) {
            head.setRotationX(0f);
            head.setRotationY(0f);
        }

        if(entity.getHealth() > 2f/3f * entity.getMaxHealth()) {
            head1.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head1.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }else{
            head1.setRotationX(0f);
            head1.setRotationY(0f);
        }

        if(entity.getHealth() > 1f/3f * entity.getMaxHealth()) {
            head2.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head2.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }else{
            head2.setRotationX(0f);
            head2.setRotationY(0f);
        }


    }
}