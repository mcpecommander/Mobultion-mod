package dev.mcpecommander.mobultion.entities.skeletons.entities;

import dev.mcpecommander.mobultion.entities.skeletons.SkeletonsConfig;
import dev.mcpecommander.mobultion.particles.PortalParticle;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;

/* McpeCommander created on 20/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public class VampireSkeletonEntity extends MobultionSkeletonEntity{

    private static final EntityDataAccessor<Integer> DATA_MORPHING = SynchedEntityData.defineId(VampireSkeletonEntity.class, EntityDataSerializers.INT);

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public VampireSkeletonEntity(EntityType<VampireSkeletonEntity> type, Level world) {
        super(type, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RestrictSunGoal(this));
        this.goalSelector.addGoal(1, new FleeSunGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Wolf.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MORPHING, -1);
    }

    public int getMorphingTime(){
        return this.entityData.get(DATA_MORPHING);
    }

    public void setMorphingTime(int morphingTime){
        this.entityData.set(DATA_MORPHING, morphingTime);
    }

    /**
     * Updates everything that needs to be updated when an entity is hurt but the health and damage calculations happen
     * in the actuallyHurt method instead.
     * @param damageSource: The damage source of the attack.
     * @param amount: How much raw damage the attack did.
     * @return true if the entity should be hurt.
     */
    @Override
    public boolean hurt(@Nonnull DamageSource damageSource, float amount) {
        boolean gotHurt = super.hurt(damageSource, amount);
        if(gotHurt && damageSource.getEntity() instanceof Player && !damageSource.isCreativePlayer()
                && random.nextFloat() < 0.1f){
            setMorphingTime(15);
        }
        return gotHurt;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        int morphingTime = getMorphingTime();
        if (morphingTime > 0){
            setMorphingTime(morphingTime - 1);
        } else if (morphingTime == 0) {
            Bat bat = new Bat(EntityType.BAT, this.level){
                private final int randomExtraTime = this.random.nextInt(150);
                //Make the bat remorph into a vampire skeleton after some random time
                @Override
                public void tick() {
                    super.tick();
                    if(this.tickCount > 150 + randomExtraTime && !this.level.isClientSide){
                        VampireSkeletonEntity skeletonEntity = new VampireSkeletonEntity(Registration.VAMPIRESKELETON.get(),
                                this.level);
                        skeletonEntity.setHealth(skeletonEntity.getMaxHealth() - random.nextInt(5));
                        skeletonEntity.setPos(this.getX(), this.getY(), this.getZ());
                        this.level.addFreshEntity(skeletonEntity);
                        this.discard();
                    }
                }
            };
            bat.setPos(this.getX(), this.getY() + 1, this.getZ());
            this.level.addFreshEntity(bat);
            this.discard();
        }
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, SkeletonsConfig.VAMPIRE_HEALTH.get())
                .add(Attributes.MOVEMENT_SPEED, SkeletonsConfig.VAMPIRE_SPEED.get())
                .add(Attributes.FOLLOW_RANGE, SkeletonsConfig.VAMPIRE_RADIUS.get())
                .add(Attributes.ATTACK_DAMAGE, SkeletonsConfig.VAMPIRE_DAMAGE.get());
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "movement", 0, this::movementPredicate));
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::controllerPredicate));
    }

    /**
     * The amount of ticks the entity ticks after it gets killed.
     * @return an integer of total death ticks
     */
    @Override
    protected int getMaxDeathTime() {
        return 50;
    }

    /**
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState controllerPredicate(AnimationEvent<E> event)
    {
        if(isDeadOrDying()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
            return PlayState.CONTINUE;
        }
        if(getMorphingTime() > 0){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("morph", false));
            double finalX = this.getX() + Math.cos(random.nextFloat() * Math.PI * 2) * 2;
            double finalY = this.getY(0.5f) + (random.nextFloat() * 2 - 1);
            double finalZ = this.getZ() + Math.sin(random.nextFloat() * Math.PI * 2) * 2;
            Vec3 speed = new Vec3(finalX - this.getX(),
                    finalY - getY(2f/3f),
                    finalZ - this.getZ()).normalize();
            this.level.addParticle(ParticleTypes.SMOKE,
                    this.getX(), getY(2f/3f), this.getZ(),
                    speed.x/20f, speed.y/20f, speed.z/20f);
            return PlayState.CONTINUE;
        }
        if(tickCount >= 1 && tickCount < 30){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("unmorph", false));
            return PlayState.CONTINUE;
        }
        //event.getController().setAnimation(new AnimationBuilder().addAnimation("melee", true));

        return PlayState.STOP;
    }

    /**
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState movementPredicate(AnimationEvent<E> event)
    {
        if(isDeadOrDying()) return PlayState.STOP;
        if(event.isMoving()){
            if(this.animationSpeed > 0.6){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("running", true));
            }else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
            }
        }else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    /**
     * Getter for the animation factory. Client side only but not null on the server.
     * @return AnimationFactory
     */
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
