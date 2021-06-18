package dev.mcpecommander.mobultion.entities.spiders.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/* Created by McpeCommander on 2021/06/18 */
public class WitchSpiderEntity extends MobultionSpiderEntity{

    private final AnimationFactory factory = new AnimationFactory(this);

    public WitchSpiderEntity(EntityType<? extends MonsterEntity> mob, World world) {
        super(mob, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
//        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 0.8D));
//        this.goalSelector.addGoal(3, new AngelSpiderHealGoal(this));
//        this.targetSelector.addGoal(1, new AngelSpiderTargetGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    private <E extends IAnimatable> PlayState predicateController(AnimationEvent<E> event)
    {
        if(this.isDeadOrDying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
            return PlayState.CONTINUE;
        }
        if(event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
        }else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event)
    {
        if(this.isDeadOrDying()) return PlayState.STOP;
        AnimationController controller = factory.getOrCreateAnimationData(this.getId()).getAnimationControllers().get("controller");
        if(controller.getCurrentAnimation() == null || controller.getCurrentAnimation().animationName.equals("move")){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController controller = new AnimationController<>(this, "controller", 0, this::predicateController);
        controller.registerParticleListener(this::particleListener);
        data.addAnimationController(new AnimationController<>(this, "idle", 0, this::predicateIdle));
        data.addAnimationController(controller);
    }

    private <T extends IAnimatable> void particleListener(ParticleKeyFrameEvent<T> event) {
        for(int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean canBeAffected(EffectInstance p_70687_1_) {
        return true;
    }

}
