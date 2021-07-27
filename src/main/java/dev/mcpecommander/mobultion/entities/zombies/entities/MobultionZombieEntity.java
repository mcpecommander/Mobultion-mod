package dev.mcpecommander.mobultion.entities.zombies.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;

/* McpeCommander created on 27/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.entities */
public abstract class MobultionZombieEntity extends MonsterEntity implements IAnimatable {

    protected MobultionZombieEntity(EntityType<? extends MobultionZombieEntity> type, World world) {
        super(type, world);
    }

    @Override
    public void aiStep() {
        if (this.isAlive()) {
            boolean isBurningFromTheSun = this.isSunBurnTick();
            if (isBurningFromTheSun) {
                ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            this.broadcastBreakEvent(EquipmentSlotType.HEAD);
                            this.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
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
    public void knockback(float strength, double ratioX, double ratioZ) {
        if(isDeadOrDying()) return;
        super.knockback(strength, ratioX, ratioZ);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos position, BlockState state) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return 1.74F;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.45D;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingLevel, boolean killedByPlayer) {
        super.dropCustomDeathLoot(damageSource, lootingLevel, killedByPlayer);
        Entity entity = damageSource.getEntity();
        if (entity instanceof CreeperEntity) {
            CreeperEntity creeperentity = (CreeperEntity)entity;
            if (creeperentity.canDropMobsSkull()) {
                ItemStack itemstack = new ItemStack(Blocks.ZOMBIE_HEAD);
                if (!itemstack.isEmpty()) {
                    creeperentity.increaseDroppedSkulls();
                    this.spawnAtLocation(itemstack);
                }
            }
        }
    }
}
