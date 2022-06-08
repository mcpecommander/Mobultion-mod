package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.entities.spiders.SpidersConfig;
import dev.mcpecommander.mobultion.entities.spiders.entityGoals.MobultionSpiderMeleeGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;

/* Created by McpeCommander on 2021/06/18 */
public class MagmaSpiderEntity extends MobultionSpiderEntity{

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);
    /**
     * A tick timer for the smoke death particles
     */
    private int flameParticleTick = 0;

    public MagmaSpiderEntity(EntityType<MagmaSpiderEntity> mob, Level world) {
        super(mob, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new MobultionSpiderMeleeGoal(this, 1.0, 0.5f, 0.7f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true){
            @Override
            public boolean canUse() {
                return !(mob.getBrightness() >= 0.5F) && super.canUse();
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true){
            @Override
            public boolean canUse() {
                return !(mob.getBrightness() >= 0.5F) && super.canUse();
            }
        });

    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, SpidersConfig.MAGMA_HEALTH.get())
                .add(Attributes.MOVEMENT_SPEED, SpidersConfig.MAGMA_SPEED.get())
                .add(Attributes.ATTACK_DAMAGE, SpidersConfig.MAGMA_DAMAGE.get());
    }

    /**
     * The predicate for animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
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

    /**
     * Whether the entity is hurt by water, whether it is rain, bubble column or in water.
     * @return true if the entity is damaged by water.
     */
    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    /**
     * Gets called when an entity is hit by this enderman.
     * @param target The entity that this enderman hit.
     * @return true if the attack was successful which is in turn determined from hurt method.
     */
    public boolean doHurtTarget(@Nonnull Entity target) {
        if (super.doHurtTarget(target)) {
            if (target instanceof LivingEntity) {
                target.setSecondsOnFire(SpidersConfig.MAGMA_FIRE.get());
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
        AnimationController<MagmaSpiderEntity> controller = new AnimationController<>(this, "controller",
                0, this::predicate);
        controller.registerParticleListener(this::particleListener);
        data.addAnimationController(controller);
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
     * The main update method which ticks on both sides all the time until the entity is removed.
     * Ticks on the server side only when the entity is not rendered anymore.
     */
    @Override
    public void tick() {
        super.tick();
        if(this.level.isClientSide && flameParticleTick > 0){
            for(int i = 0; i < 10; ++i) {
                double d0 = this.random.nextGaussian() * 0.05D;
                double d1 = this.random.nextGaussian() * 0.05D;
                double d2 = this.random.nextGaussian() * 0.05D;
                this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getRandomY(),
                        this.getRandomZ(0.5D), d0, d1, d2);
            }
            flameParticleTick --;
        }
    }

    /**
     * Is the entity immune to fire like {@link net.minecraft.world.entity.monster.Blaze}
     * @return true if the entity is immune to fire
     */
    @Override
    public boolean fireImmune() {
        return true;
    }

    /**
     * The particle listener which gets called every time there is a particle effect in the current animation.
     * @param event: gives access to the locator name, particle name and script data.
     */
    private <T extends IAnimatable> void particleListener(ParticleKeyFrameEvent<T> event) {
        switch (event.locator) {
            case "all" -> flameParticleTick = 2;
            case "leg1" -> {
                Vec3 leg1 = new Vec3((-16.4f / 16f), (2.9d / 16d), (9.3f / 16f));
                leg1 = leg1.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg1.x, leg1.y, leg1.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg1.x, leg1.y + 0.1f, leg1.z, 0, 0.1f, 0);
            }
            case "leg2" -> {
                Vec3 leg2 = new Vec3((-16.5f / 16f), (2.9d / 16d), (4f / 16f));
                leg2 = leg2.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg2.x, leg2.y, leg2.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg2.x, leg2.y + 0.1f, leg2.z, 0, 0.1f, 0);
            }
            case "leg3" -> {
                Vec3 leg3 = new Vec3((-16.5f / 16f), (2.9d / 16d), (-3.3f / 16f));
                leg3 = leg3.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg3.x, leg3.y, leg3.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg3.x, leg3.y + 0.1f, leg3.z, 0, 0.1f, 0);
            }
            case "leg4" -> {
                Vec3 leg4 = new Vec3((-16.4f / 16f), (2.9d / 16d), (-9.3f / 16f));
                leg4 = leg4.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg4.x, leg4.y, leg4.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg4.x, leg4.y + 0.1f, leg4.z, 0, 0.1f, 0);
            }
            case "leg5" -> {
                Vec3 leg5 = new Vec3((16.4f / 16f), (2.9d / 16d), (9.3f / 16f));
                leg5 = leg5.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg5.x, leg5.y, leg5.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg5.x, leg5.y + 0.1f, leg5.z, 0, 0.1f, 0);
            }
            case "leg6" -> {
                Vec3 leg6 = new Vec3((16.5f / 16f), (2.9d / 16d), (4f / 16f));
                leg6 = leg6.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg6.x, leg6.y, leg6.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg6.x, leg6.y + 0.1f, leg6.z, 0, 0.1f, 0);
            }
            case "leg7" -> {
                Vec3 leg7 = new Vec3((16.5f / 16f), (2.9d / 16d), (-3.3f / 16f));
                leg7 = leg7.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg7.x, leg7.y, leg7.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg7.x, leg7.y + 0.1f, leg7.z, 0, 0.1f, 0);
            }
            case "leg8" -> {
                Vec3 leg8 = new Vec3((16.4f / 16f), (2.9d / 16d), (-9.3f / 16f));
                leg8 = leg8.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                this.level.addParticle(ParticleTypes.FLAME,
                        leg8.x, leg8.y, leg8.z, 0, 0, 0);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE,
                        leg8.x, leg8.y + 0.1f, leg8.z, 0, 0.1f, 0);
            }
            default -> {
            }
        }
    }

    /**
     * The amount of ticks the entity ticks after it gets killed.
     * @return an integer of total death ticks
     */
    @Override
    protected int getMaxDeathTick() {
        return 35;
    }

    /**
     * Ticks on both sides when isDeadOrDying() is true.
     */
    @Override
    protected void tickDeath() {
        if(this.deathTime++ == getMaxDeathTick()){
            this.discard();
            BlockPos pos = new BlockPos(this.getX(), this.getY(), this.getZ());
            if(BaseFireBlock.canBePlacedAt(this.level, pos, Direction.UP)){
                BlockState state = BaseFireBlock.getState(this.level, pos);
                this.level.setBlock(pos, state, Block.UPDATE_ALL_IMMEDIATE);
            }
        }
    }
}
