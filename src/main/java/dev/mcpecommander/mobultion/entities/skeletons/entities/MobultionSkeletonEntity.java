package dev.mcpecommander.mobultion.entities.skeletons.entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/* McpeCommander created on 17/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public abstract class MobultionSkeletonEntity extends MonsterEntity implements IAnimatable {

    private static final DataParameter<Optional<UUID>> TARGET = EntityDataManager.defineId(MobultionSkeletonEntity.class, DataSerializers.OPTIONAL_UUID);

    protected MobultionSkeletonEntity(EntityType<? extends MobultionSkeletonEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET, Optional.empty());
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if(target != null){
            this.entityData.set(TARGET, Optional.of(target.getUUID()));
        }else{
            this.entityData.set(TARGET, Optional.empty());
        }
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        if(!this.level.isClientSide){
            return super.getTarget();
        }
        return this.level.getPlayerByUUID(this.entityData.get(TARGET).orElse(UUID.randomUUID()));
    }

    @Override
    public void aiStep() {
        boolean isUnderDirectSun = this.isSunBurnTick();
        if (isUnderDirectSun) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.HEAD);
            if (!itemstack.isEmpty()) {
                if (itemstack.isDamageableItem()) {
                    itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                    if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                        this.broadcastBreakEvent(EquipmentSlotType.HEAD);
                        this.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
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

    protected abstract int getMaxDeathTime();

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == getMaxDeathTime()) {
            remove();
        }
    }

    @Override
    public boolean isOnFire() {
        return !isDeadOrDying() && super.isOnFire();
    }

    protected AbstractArrowEntity getArrow(ItemStack bow, float power) {
        return ProjectileHelper.getMobArrow(this, bow, power);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos blockPos, @Nonnull BlockState state) {
        this.playSound(SoundEvents.SKELETON_STEP, 0.15F, 1.0F);
    }

    @Nonnull
    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

}
