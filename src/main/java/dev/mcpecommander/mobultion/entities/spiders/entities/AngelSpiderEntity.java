package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.entities.spiders.entityGoals.AngelSpiderHealGoal;
import dev.mcpecommander.mobultion.entities.spiders.entityGoals.AngelSpiderTargetGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

/* Created by McpeCommander on 2021/06/18 */
@SuppressWarnings("NullableProblems")
public class AngelSpiderEntity extends MobultionSpiderEntity {

    /**
     * A data parameter to help keep the target in sync and to be saved when quiting the game.
     */
    private static final DataParameter<Integer> TARGET = EntityDataManager.defineId(AngelSpiderEntity.class, DataSerializers.INT);
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public AngelSpiderEntity(EntityType<? extends MonsterEntity> mob, World world) {
        super(mob, world);
        this.maxDeathTimer = 20;
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 0.8D));
        this.goalSelector.addGoal(3, new AngelSpiderHealGoal(this));
        this.targetSelector.addGoal(1, new AngelSpiderTargetGoal(this));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    /**
     *
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        if(this.getTarget() != null){
            if(level.isClientSide){
                level.addParticle(ParticleTypes.END_ROD, this.getTarget().getX() + Math.cos(tickCount)/2d
                        , this.getTarget().getY() + 1.5d + Math.random() * 0.5d - 0.25d
                        , this.getTarget().getZ() + Math.sin(tickCount)/2d,
                        0, -0.05d, 0);
            }
            if(event.getController().getCurrentAnimation() == null ||
                    (event.getController().getCurrentAnimation() != null &&
                            event.getController().getCurrentAnimation().animationName.equals("move"))) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", false));
            }
            if(event.getController().getCurrentAnimation() != null &&
                    event.getController().getCurrentAnimation().animationName.equals("attack")
                    && event.getController().getAnimationState() == AnimationState.Stopped){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("attack_hold", true));
            }
            return PlayState.CONTINUE;
        }
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
     * Register/define the default value of the data parameter here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET, -1);
    }

    /**
     * Try to see if the target is not null otherwise try to retrieve it via the data parameter.
     * @return the current LivingEntity that this mob is targeting.
     */
    @Nullable
    @Override
    public LivingEntity getTarget() {
        LivingEntity target = super.getTarget();
        return target != null ? target : (LivingEntity) this.level.getEntity(this.entityData.get(TARGET));
    }

    /**
     * Write the target ID into the data parameter for syncing easy retrieval purposes later.
     * @param target A living entity or null if we want to reset the target.
     */
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (target != null) {
            this.entityData.set(TARGET, target.getId());
        } else {
            this.entityData.set(TARGET, -1);
        }
    }

    /**
     * Weather this mob can be affected by the potion effect in the params.
     * @param effect: the potion effect instance.
     * @return true if the mob can be affected.
     */
    @Override
    public boolean canBeAffected(EffectInstance effect) {
        return true;
    }

}
