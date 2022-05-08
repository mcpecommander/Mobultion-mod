package dev.mcpecommander.mobultion.entities.spiders.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;

import javax.annotation.Nonnull;

/* Created by McpeCommander on 2021/06/18 */
public abstract class MobultionSpiderEntity extends Monster implements IAnimatable{

    /**
     * A data parameter copied from the minecraft spider that sets the spider climbing.
     */
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(MobultionSpiderEntity.class, EntityDataSerializers.BYTE);

    public MobultionSpiderEntity(EntityType<? extends MobultionSpiderEntity> type, Level world) {
        super(type, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        //this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        //this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
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
     * @param blockPosition the position the entity is walking on right now
     * @param blockState the state of the block being walked on
     */
    @Override
    protected void playStepSound(@Nonnull BlockPos blockPosition, @Nonnull BlockState blockState) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    /**
     * Gets called for example ladder blocks to set their climbing status
     * The block in question is not supplied and should be checked manually
     * @return true if the entity should be climbing
     */
    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    /**
     * Gets if the entity is climbing from the data parameter.
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
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    /**
     * Set the entity climbing data parameter to sync it to the client.
     * @param isClimbing true if the entity should be climbing
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
    protected PathNavigation createNavigation(@Nonnull Level world) {
        return new WallClimberNavigation(this, world);
    }

    /**
     * Ticks on both sides when isDeadOrDying() is true.
     */
    @Override
    protected void tickDeath() {
        if(this.deathTime++ == getMaxDeathTick()){
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    /**
     * The amount of ticks the entity ticks after it gets killed.
     * @return an integer of total death ticks
     */
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
    protected float getStandingEyeHeight(@Nonnull Pose pose, @Nonnull EntityDimensions entitySize) {
        return 0.65F;
    }

}
