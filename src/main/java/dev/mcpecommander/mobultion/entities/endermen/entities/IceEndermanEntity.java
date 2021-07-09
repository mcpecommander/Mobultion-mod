package dev.mcpecommander.mobultion.entities.endermen.entities;

import dev.mcpecommander.mobultion.entities.endermen.entityGoals.EndermanFindStaringPlayerGoal;
import dev.mcpecommander.mobultion.particles.PortalParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Objects;

/* McpeCommander created on 07/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entities */
public class IceEndermanEntity extends MobultionEndermanEntity{

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public IceEndermanEntity(EntityType<? extends MobultionEndermanEntity> type, World world) {
        super(type, world);
    }

    public boolean doHurtTarget(Entity target) {
        if (super.doHurtTarget(target)) {
            if (target instanceof LivingEntity) {
                ((LivingEntity)target).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 5 * 20, 0));
            }
            return true;
        }
        return false;
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1.0D, 8));
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
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
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

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if(this.deathTime >= 2 && this.deathTime < 24){
            for(int i = 0; i < 2; ++i) {
                double startX = this.getRandomX(5D);
                double startY = this.getRandomY();
                double startZ = this.getRandomZ(5D);
                double finalY = this.getRandomY();//(this.getY() + (this.random.nextFloat() * 0.8f) + this.getBbHeight()/2f);
                Vector3d speed = new Vector3d(this.getX() - startX,
                        finalY - startY,
                        this.getZ() - startZ).normalize();
                this.level.addParticle(new PortalParticle.PortalParticleData((200f/255f),(66f/255f),(220f/255f),1f
                        ,(float) this.getX(), (float)finalY, (float) this.getZ()),
                        startX, startY, startZ,
                        speed.x/10f, speed.y/10f, speed.z/10f);
            }
        }
        if (this.deathTime == 27) {
            this.remove();
        }

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

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}