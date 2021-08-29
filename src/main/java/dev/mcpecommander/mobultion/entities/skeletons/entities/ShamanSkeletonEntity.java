package dev.mcpecommander.mobultion.entities.skeletons.entities;

import dev.mcpecommander.mobultion.entities.skeletons.entityGoals.ShamanHealGoal;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* McpeCommander created on 21/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public class ShamanSkeletonEntity extends MobultionSkeletonEntity implements IRangedAttackMob {

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public ShamanSkeletonEntity(EntityType<? extends MobultionSkeletonEntity> type, World world) {
        super(type, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RestrictSunGoal(this));
        this.goalSelector.addGoal(1, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PlayerEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(2, new ShamanHealGoal(this, 1D, 30, 25.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtGoal(this, MobultionSkeletonEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MobultionSkeletonEntity.class, 10, false,
                false, skeleton -> skeleton.getHealth() < skeleton.getMaxHealth() && !skeleton.isUsingItem()));
    }

    /**
     * Gets called from ranged attack goals/AI.
     * @param target The entity being targeted.
     * @param power The power of the arrow, usually defined from the goal itself for each mob.
     */
    public void performRangedAttack(@Nonnull LivingEntity target, float power) {
        MiniLightningEntity boltEntity = new MiniLightningEntity(Registration.MINILIGHTNING.get(), this.level);
        boltEntity.setPos(target.getX(), target.getY(), target.getZ());
        boltEntity.setTarget(target.getId());
        this.level.addFreshEntity(boltEntity);
        target.heal(power);
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 18)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 64);
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
    public ILivingEntityData finalizeSpawn(@Nonnull IServerWorld serverWorld, @Nonnull DifficultyInstance difficulty,
                                           @Nonnull SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityData,
                                           @Nullable CompoundNBT NBTTag) {
        this.setItemInHand(Hand.MAIN_HAND, new ItemStack(Registration.HEALINGSTAFF.get()));
        return super.finalizeSpawn(serverWorld, difficulty, spawnReason, livingEntityData, NBTTag);
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
        if(isAggressive()){
            if(isUsingItem()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("cast", true));
            }else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("castHold", true));
            }
            return PlayState.CONTINUE;
        }

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
