package dev.mcpecommander.mobultion.entities.zombies.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;

/* McpeCommander created on 27/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.entities */
public abstract class MobultionZombieEntity extends Monster implements IAnimatable {

    protected MobultionZombieEntity(EntityType<? extends MobultionZombieEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public void aiStep() {
        if (this.isAlive()) {
            boolean isBurningFromTheSun = this.isSunBurnTick();
            if (isBurningFromTheSun) {
                ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            this.broadcastBreakEvent(EquipmentSlot.HEAD);
                            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    isBurningFromTheSun = false;
                }

                if (isBurningFromTheSun) {
                    this.setSecondsOnFire(8);
                }
            }
        }

        super.aiStep();
    }

    @Override
    protected void pushEntities() {
        if(isDeadOrDying()) return;
        super.pushEntities();
    }

    @Override
    public void knockback(double strength, double ratioX, double ratioZ) {
        if(isDeadOrDying()) return;
        super.knockback(strength, ratioX, ratioZ);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos position, @NotNull BlockState state) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size) {
        return 1.74F;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.45D;
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource damageSource, int lootingLevel, boolean killedByPlayer) {
        super.dropCustomDeathLoot(damageSource, lootingLevel, killedByPlayer);
        Entity entity = damageSource.getEntity();
        if (entity instanceof Creeper creeperentity) {
            if (creeperentity.canDropMobsSkull()) {
                ItemStack itemstack = new ItemStack(Blocks.ZOMBIE_HEAD);
                if (!itemstack.isEmpty()) {
                    creeperentity.increaseDroppedSkulls();
                    this.spawnAtLocation(itemstack);
                }
            }
        }
    }

    abstract void deathParticles();

    abstract int getMaxDeathCount();

    @Override
    public boolean isOnFire() {
        return !isDeadOrDying() && super.isOnFire();
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        deathParticles();

        if(this.deathTime == getMaxDeathCount()){
            this.discard();
        }
    }
}
