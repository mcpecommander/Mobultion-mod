package dev.mcpecommander.mobultion.entities.skeletons.entities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;

import javax.annotation.Nonnull;

/* McpeCommander created on 17/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public abstract class MobultionSkeletonEntity extends Monster implements IAnimatable {

    protected MobultionSkeletonEntity(EntityType<? extends MobultionSkeletonEntity> type, Level world) {
        super(type, world);
    }

    /**
     * An update method within the tick method that updates some stuff but not other stuff. Not really obvious why it exists.
     */
    @Override
    public void aiStep() {
        boolean isUnderDirectSun = this.isSunBurnTick();
        if (isUnderDirectSun) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
            if (!itemstack.isEmpty()) {
                if (itemstack.isDamageableItem()) {
                    itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                    if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                        this.broadcastBreakEvent(EquipmentSlot.HEAD);
                        this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    }
                }
                isUnderDirectSun = false;
            }

            if (isUnderDirectSun) {
                this.setSecondsOnFire(8);
            }
        }

        super.aiStep();
    }

    /**
     * The amount of ticks the entity ticks after it gets killed.
     * @return an integer of total death ticks
     */
    protected abstract int getMaxDeathTime();

    /**
     * Gets called every tick after the entity dies until it's removed.
     */
    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == getMaxDeathTime()) {
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    /**
     * Checks if the entity is on fire by checking its remaining fire ticks and its fire immunity.
     * @return true if the remaining fire ticks are more than 0.
     */
    @Override
    public boolean isOnFire() {
        return !isDeadOrDying() && super.isOnFire();
    }

    /**
     * Used to create an instance of the arrow entity for when this entity fires or shoots an arrow.
     * @param bow The bow item being used by this entity right now.
     * @param power The power of the bow usually given in the attack goal or AI.
     * @return An arrow entity instance
     */
    protected AbstractArrow getArrow(ItemStack bow, float power) {
        return ProjectileUtil.getMobArrow(this, bow, power);
    }

    /**
     * The ambient sound of the creature
     * @return SoundEvent of the ambient sound
     */
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    /**
     * The sound that the entity makes when hurt. Can be different depending on the damage source.
     * @param damageSource: The type of the damage the entity took
     * @return SoundEvent of the damage sound
     */
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    /**
     * The sound that entity makes when it dies.
     * @return SoundEvent of the death sound
     */
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    /**
     * The actions to do when the entity walks on specific blocks. Usually has one type of sound events per entity type,
     * but can vary depending on the block if needed.
     * @param blockPos The position of the block being walked on.
     * @param state The block state of the block being walked on.
     */
    @Override
    protected void playStepSound(@Nonnull BlockPos blockPos, @Nonnull BlockState state) {
        this.playSound(SoundEvents.SKELETON_STEP, 0.15F, 1.0F);
    }

    /**
     * The type of creature this mob is. Check {@link MobType}
     * @return An enum of the creature type
     */
    @Nonnull
    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

}
