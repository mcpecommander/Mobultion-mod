package dev.mcpecommander.mobultion.entities.spiders.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;

import javax.annotation.Nonnull;

/* Created by McpeCommander on 2021/06/18 */
public abstract class MobultionSpiderEntity extends MonsterEntity implements IAnimatable{

    /**
     * A data parameter copied from the minecraft spider that sets the spider climbing.
     */
    private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(MobultionSpiderEntity.class, DataSerializers.BYTE);

    public MobultionSpiderEntity(EntityType<? extends MobultionSpiderEntity> type, World world) {
        super(type, world);
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
     * Register/define the default value of the data parameter here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
    }

    /**
     * The main update method which ticks on both sides all the time until the entity is removed.
     * Ticks on the server side only when the entity is not rendered.
     */
    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
    }

    /**
     * The ambient sound of the creature
     * @return SoundEvent of the ambient sound
     */
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    /**
     * The sound that the entity makes when hurt. Can be different depending on the damage source.
     * @param damageSource: The type of the damage the entity took
     * @return SoundEvent of the damage sound
     */
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.SPIDER_HURT;
    }

    /**
     * The sound that entity makes when it dies.
     * @return SoundEvent of the death sound
     */
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    /**
     * Play the step sound in this method. It supplies the blockstate and the position of the block the entity walked on
     * @param blockPosition
     * @param blockState
     */
    @Override
    protected void playStepSound(@Nonnull BlockPos blockPosition, @Nonnull BlockState blockState) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    /**
     * Gets if the entity climbing from the data parameter.
     * @return true if the entity is climbing.
     */
    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    /**
     *
     * @return The creature attribute that this entity belongs to.
     */
    @Nonnull
    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.ARTHROPOD;
    }

    /**
     * Set the entity climbing data parameter to sync it and save it.
     * @param isClimbing
     */
    public void setClimbing(boolean isClimbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (isClimbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    /**
     * Changes the kind of path navigation system this entity uses.
     * @param world: the world of this entity.
     * @return PathNavigator
     */
    @Nonnull
    @Override
    protected PathNavigator createNavigation(@Nonnull World world) {
        return new ClimberPathNavigator(this, world);
    }

    /**
     * Ticks on both sides when isDeadOrDying() is true.
     */
    @Override
    protected void tickDeath() {
        if(this.deathTime++ == getMaxDeathTick()){
            this.remove();
        }
    }

    protected abstract int getMaxDeathTick();

    /**
     * Gets the eye height of the entity allowing to change based on different poses or different sizes like babies or
     * different sizes of slimes.
     * @param pose: The pose of the entity.
     * @param entitySize: An instance of EntitySize that has information about he width, height and bounding box of
     *                  the entity.
     * @return a float representing the eye height.
     */
    @Override
    protected float getStandingEyeHeight(@Nonnull Pose pose, @Nonnull EntitySize entitySize) {
        return 0.65F;
    }

}
