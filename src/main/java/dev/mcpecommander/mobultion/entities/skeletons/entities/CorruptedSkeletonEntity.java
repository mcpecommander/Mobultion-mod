package dev.mcpecommander.mobultion.entities.skeletons.entities;

import dev.mcpecommander.mobultion.entities.skeletons.SkeletonsConfig;
import dev.mcpecommander.mobultion.entities.skeletons.entityGoals.AnimatedMeleeAttackGoal;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* McpeCommander created on 18/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public class CorruptedSkeletonEntity extends MobultionSkeletonEntity {

    /**
     * The animation factory, for more information check GeckoLib.
     */
    //TODO: Make it throw the bone and melee attack with it.
    private final AnimationFactory factory = new AnimationFactory(this);

    public CorruptedSkeletonEntity(EntityType<? extends MobultionSkeletonEntity> type, Level world) {
        super(type, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RestrictSunGoal(this));
        this.goalSelector.addGoal(1, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(2, new AnimatedMeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, SkeletonsConfig.CORRUPTED_HEALTH.get())
                .add(Attributes.MOVEMENT_SPEED, SkeletonsConfig.CORRUPTED_SPEED.get())
                .add(Attributes.FOLLOW_RANGE, SkeletonsConfig.CORRUPTED_RADIUS.get())
                .add(Attributes.ATTACK_DAMAGE, SkeletonsConfig.CORRUPTED_DAMAGE.get());
    }

    /**
     * Used to finalize the normal spawning of mobs such as from eggs or normal world spawning but not commands.
     * @param serverWorld The server world instance.
     * @param difficulty The difficulty of the current world.
     * @param spawnReason How was this entity was spawned.
     * @param livingEntityData The entity data attached to it for further use upon spawning.
     * @param NBTTag The NBT tag that holds persisted data.
     * @return ILivingEntityData that holds information about the entity spawning.
     */
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor serverWorld, @Nonnull DifficultyInstance difficulty,
                                           @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData livingEntityData,
                                           @Nullable CompoundTag NBTTag) {
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Registration.CORRUPTEDBONE.get()));
        return super.finalizeSpawn(serverWorld, difficulty, spawnReason, livingEntityData, NBTTag);
    }

    /**
     * Gets called when an entity is hit by this enderman.
     * @param hurtEntity The entity that this enderman hit.
     * @return true if the attack was successful which is in turn determined from hurt method.
     */
    @Override
    public boolean doHurtTarget(@Nonnull Entity hurtEntity) {
        boolean flag = super.doHurtTarget(hurtEntity);
        if(flag && hurtEntity instanceof Player){
            ((Player) hurtEntity).addEffect(new MobEffectInstance(Registration.CORRUPTION_EFFECT.get(),
                    20 * 30, Math.max(this.level.getDifficulty().getId() - 1, 0)));
        }
        return flag;
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
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "movement", 0, this::movementPredicate));
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::controllerPredicate));
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
        if(this.swinging || event.getController().getAnimationState() == AnimationState.Running){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("melee", false));
            return PlayState.CONTINUE;
        }
        event.getController().clearAnimationCache();
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
//            if(this.animationSpeed > 0.6){
//                event.getController().setAnimation(new AnimationBuilder().addAnimation("running", true));
//            }else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
//            }
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
