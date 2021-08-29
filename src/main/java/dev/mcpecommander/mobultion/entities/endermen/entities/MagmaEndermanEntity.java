package dev.mcpecommander.mobultion.entities.endermen.entities;

import dev.mcpecommander.mobultion.entities.endermen.EndermenConfig;
import dev.mcpecommander.mobultion.entities.endermen.entityGoals.EndermanFindStaringPlayerGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.Objects;

/* McpeCommander created on 26/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entities */
public class MagmaEndermanEntity extends MobultionEndermanEntity{

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public MagmaEndermanEntity(EntityType<? extends MobultionEndermanEntity> type, World world) {
        super(type, world);
        //Set the priority to negative to signal that this entity avoids water at all costs.
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
    }

    /**
     * Whether the entity is hurt by water, whether it is rain, bubble column or in water.
     * @return true if the entity is damaged by water.
     */
    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new EndermanFindStaringPlayerGoal(this, livingEntity -> true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EndermiteEntity.class, 10, true, false, (entity) -> entity instanceof EndermiteEntity && ((EndermiteEntity)entity).isPlayerSpawned()));
        this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, EndermenConfig.MAGMA_HEALTH.get())
                .add(Attributes.MOVEMENT_SPEED, EndermenConfig.MAGMA_SPEED.get())
                .add(Attributes.ATTACK_DAMAGE, EndermenConfig.MAGMA_DAMAGE.get())
                .add(Attributes.FOLLOW_RANGE, EndermenConfig.MAGMA_RADIUS.get());
    }

    /**
     * The amount of ticks the entity ticks after it gets killed.
     * @return an integer of total death ticks
     */
    @Override
    protected int maxDeathAge() {
        return 27;
    }

    /**
     * Gets called every tick on the client side after the entity dies until its removed.
     */
    @Override
    protected void addDeathParticles() {
        if(this.deathTime >= 10 && this.deathTime < 20){
            for(int i = 0; i < 10; i++) {
                double finalX = this.getX() + Math.cos(random.nextFloat() * Math.PI * 2) * 2;
                double finalY = this.getY(1f) + (random.nextFloat() * 2 - 1);
                double finalZ = this.getZ() + Math.sin(random.nextFloat() * Math.PI * 2) * 2;
                Vector3d speed = new Vector3d(finalX - this.getX(),
                        finalY - getY(2f/3f),
                        finalZ - this.getZ()).normalize();
                this.level.addParticle(ParticleTypes.FLAME,
                        this.getX(), getY(2f/3f), this.getZ(),
                        speed.x/20f, speed.y/10f, speed.z/20f);
            }
        }
    }

    /**
     * Gets called when an entity is hit by this enderman.
     * @param target The entity that this enderman hit.
     * @return true if the attack was successful which is in turn determined from hurt method.
     */
    public boolean doHurtTarget(@Nonnull Entity target) {
        if (super.doHurtTarget(target)) {
            if (target instanceof LivingEntity) {
                target.setSecondsOnFire(EndermenConfig.MAGMA_FIRE.get());
            }
            return true;
        }
        return false;
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 1, this::shouldAnimate));
        data.addAnimationController(new AnimationController<>(this, "movement", 1, this::shouldMove));
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
     * The predicate for the movement animations.
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState shouldMove(AnimationEvent<E> event)
    {
        if(isDeadOrDying()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
            return PlayState.CONTINUE;
        }
        if(event.isMoving()) {
            if(Objects.requireNonNull(getAttribute(Attributes.MOVEMENT_SPEED)).hasModifier(MobultionEndermanEntity.SPEED_MODIFIER_ATTACKING)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("running", true));
            }else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    /**
     * The predicate for the animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState shouldAnimate(AnimationEvent<E> event)
    {
        if(isCreepy() && !isDeadOrDying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("rage", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
}
