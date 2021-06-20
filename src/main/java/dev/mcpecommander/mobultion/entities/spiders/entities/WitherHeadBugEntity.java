package dev.mcpecommander.mobultion.entities.spiders.entities;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/* McpeCommander created on 19/06/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.entities */
public class WitherHeadBugEntity extends MonsterEntity implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public WitherHeadBugEntity(EntityType<? extends MonsterEntity> mob, World level) {
        super(mob, level);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.MOVEMENT_SPEED, 0.5D);
    }

    /**
     * The predicate for animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        if(event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
        }else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    /**
     * Getter for the animation factory. Client side only but not null on the server.
     * @return AnimationFactory
     */
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    /**
     * Gets the eye height of the entity allowing to change based on different poses or different sizes like babies or
     * different sizes of slimes.
     * @param pose: The pose of the entity.
     * @param entitySize: An instance of EntitySize that has information about he width, height and bounding box of
     *                  the entity.
     * @return a float representing the eye height.
     */
    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize entitySize) {
        return 0.25F;
    }
}
