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
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
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

    private static final DataParameter<Integer> TARGET = EntityDataManager.defineId(AngelSpiderEntity.class, DataSerializers.INT);
    private final AnimationFactory factory = new AnimationFactory(this);

    public AngelSpiderEntity(EntityType<? extends MonsterEntity> p_i48553_1_, World p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
        this.maxDeathTimer = 20;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 0.8D));
        this.goalSelector.addGoal(3, new AngelSpiderHealGoal(this));
        this.targetSelector.addGoal(1, new AngelSpiderTargetGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.4D);
    }


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

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected PathNavigator createNavigation(World p_175447_1_) {
        return new ClimberPathNavigator(this, p_175447_1_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET, -1);
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        LivingEntity target = super.getTarget();
        return target != null ? target : (LivingEntity) this.level.getEntity(this.entityData.get(TARGET));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (target != null) {
            this.entityData.set(TARGET, target.getId());
        } else {
            this.entityData.set(TARGET, -1);
        }
    }

    @Override
    public boolean canBeAffected(EffectInstance p_70687_1_) {
        return true;
    }

    @Override
    public void setBaby(boolean p_82227_1_) {}

}
