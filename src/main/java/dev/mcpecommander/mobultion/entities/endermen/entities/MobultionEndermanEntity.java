package dev.mcpecommander.mobultion.entities.endermen.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/* McpeCommander created on 26/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entities */
public abstract class MobultionEndermanEntity extends MonsterEntity implements IAngerable, IAnimatable {
    /**
     * Unique ID for the speed modifier.
     */
    private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("020E0DFB-87AE-4653-9556-501010E291A0");
    /**
     * Unique speed modifier for this class of entities.
     */
    public static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 1.3F, AttributeModifier.Operation.MULTIPLY_BASE);
    /**
     * A synced boolean of whether this entity is angry or not.
     */
    private static final DataParameter<Boolean> DATA_CREEPY = EntityDataManager.defineId(MobultionEndermanEntity.class, DataSerializers.BOOLEAN);
    /**
     * A synced boolean of whether this entity is being stared at or not.
     */
    private static final DataParameter<Boolean> DATA_STARED_AT = EntityDataManager.defineId(MobultionEndermanEntity.class, DataSerializers.BOOLEAN);
    /**
     * A counter to make sure that the creepy enderman sound is not spammed or overlapped.
     */
    private int lastStareSound = Integer.MIN_VALUE;
    /**
     * A countdown of 600 ticks until the target can be nullified (in day light only).
     */
    private int targetChangeTime;
    /**
     * A persistent anger timer that keeps track both on server and client and even when the games is restarted.
     */
    private static final RangedInteger PERSISTENT_ANGER_TIME = TickRangeConverter.rangeOfSeconds(20, 39);
    /**
     * The remaining anger time. It is synced and persists when the game is restarted.
     */
    private int remainingPersistentAngerTime;
    /**
     * The unique ID of the enderman target.
     */
    private UUID persistentAngerTarget;

    public MobultionEndermanEntity(EntityType<? extends MobultionEndermanEntity> type, World world){
        super(type, world);
        //Means that this entity can step up blocks that are 1 high.
        this.maxUpStep = 1.0F;
    }

    /**
     * Sets the creepy state when there is a target as well as modifying the speed attribute.
     * @param target A living entity or null if we want to reset the target.
     */
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (target == null) {
            this.targetChangeTime = 0;
            this.entityData.set(DATA_CREEPY, false);
            this.entityData.set(DATA_STARED_AT, false);
            assert modifiableattributeinstance != null;
            modifiableattributeinstance.removeModifier(SPEED_MODIFIER_ATTACKING);
        } else {
            this.targetChangeTime = this.tickCount;
            this.entityData.set(DATA_CREEPY, true);
            assert modifiableattributeinstance != null;
            if (!modifiableattributeinstance.hasModifier(SPEED_MODIFIER_ATTACKING)) {
                modifiableattributeinstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
            }
        }
        super.setTarget(target);
    }

    /**
     * Register/define the default value of the data parameters here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CREEPY, false);
        this.entityData.define(DATA_STARED_AT, false);
    }

    /**
     * Sets a random amount of ticks between 400 and 780 ticks (20 and 39 seconds) in which the enderman stays angry.
     */
    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.randomValue(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingTime) {
        this.remainingPersistentAngerTime = remainingTime;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID targetUUID) {
        this.persistentAngerTarget = targetUUID;
    }

    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    /**
     * Plays the staring sound if the enderman is angry and more than 400 ticks have passed since the last time it was playes.
     */
    public void playStareSound() {
        if (this.tickCount >= this.lastStareSound + 400) {
            this.lastStareSound = this.tickCount;
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ENDERMAN_STARE, this.getSoundSource(), 2.5F, 1.0F, false);
            }
        }

    }

    /**
     * The amount of ticks the entity ticks after it gets killed.
     * @return an integer of total death ticks
     */
    protected abstract int maxDeathAge();

    /**
     * Gets called every tick on the client side after the entity dies until its removed.
     */
    protected abstract void addDeathParticles();

    /**
     * Gets called every tick after the entity dies until it's removed.
     */
    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if(this.level.isClientSide) addDeathParticles();
        if (this.deathTime == maxDeathAge()) {
            this.remove();
        }
    }

    /**
     * Is called whenever a data parameter is changed and is being synced between the server and client.
     * @param syncedParameter The parameter that is being synced.
     */
    @Override
    public void onSyncedDataUpdated(DataParameter<?> syncedParameter) {
        if (DATA_CREEPY.equals(syncedParameter) && this.hasBeenStaredAt() && this.level.isClientSide) {
            this.playStareSound();
        }
        super.onSyncedDataUpdated(syncedParameter);
    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param NBTTag The tag where the additional data will be written to.
     */
    @Override
    public void addAdditionalSaveData(CompoundNBT NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        //Writes the anger time and the UUID of the target to the NBT tag.
        this.addPersistentAngerSaveData(NBTTag);
    }

    /**
     * Reads the NBT tag and gets any pieces of saved data and applies it to the entity.
     * @param NBTTag The NBT tag that holds the saved data.
     */
    @Override
    public void readAdditionalSaveData(CompoundNBT NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(!level.isClientSide)
            this.readPersistentAngerSaveData((ServerWorld)this.level, NBTTag);
    }


    public boolean isLookingAtMe(PlayerEntity player) {
        ItemStack itemstack = player.inventory.armor.get(3);
        if (itemstack.getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
            return false;
        } else {
            Vector3d vector3d = player.getViewVector(1.0F).normalize();
            Vector3d vector3d1 = new Vector3d(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
            double d0 = vector3d1.length();
            vector3d1 = vector3d1.normalize();
            double d1 = vector3d.dot(vector3d1);
            return d1 > 1.0D - 0.025D / d0 && player.canSee(this);
        }
    }

    /**
     * The eye height where the look vector starts.
     * @param pose different poses have different eye heights like when riding vs walking.
     * @param size mobs with different sizes have different eye height like slime entities.
     * @return a float of how high are the eye from the ground up.
     */
    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return 2.55F;
    }

    /**
     * Gets called on the client side each tick to spawn ambient particles.
     */
    protected void addAmbientParticles(){
        for(int i = 0; i < 2; ++i) {
            this.level.addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5D),
                    this.getRandomY() - 0.25D,
                    this.getRandomZ(0.5D),
                    (this.random.nextDouble() - 0.5D) * 2.0D,
                    -this.random.nextDouble(),
                    (this.random.nextDouble() - 0.5D) * 2.0D);
        }
    }

    /**
     * An update method within the tick method that updates somethings but not others. Not really obvious why it exists.
     */
    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            addAmbientParticles();
        }

        this.jumping = false;
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerWorld)this.level, true);
        }

        super.aiStep();
    }

    /**
     * Another update method for mobs that only ticks on the server, has multiple uses.
     */
    @Override
    protected void customServerAiStep() {
        if (this.level.isDay() && this.tickCount >= this.targetChangeTime + 600) {
            float f = this.getBrightness();
            if (f > 0.5F && this.level.canSeeSky(this.blockPosition()) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.setTarget(null);
                //this.teleport();
            }
        }

        super.customServerAiStep();
    }

    /**
     * Not the actual teleport method but a random location chooser.
     * @return true if the actual teleport method returns true for the chosen location.
     */
    public boolean teleport() {
        if (!this.level.isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
            double d1 = this.getY() + (double)(this.random.nextInt(64) - 32);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
            return this.teleport(d0, d1, d2);
        } else {
            return false;
        }
    }

    /**
     * Tries to find a random location to transport which is between 4 and 16 blocks closer to the target.
     * @param target the entity that the enderman is trying to teleport closer to.
     * @return true if the actual teleport method returns true and the teleportation location is possible.
     */
    private boolean teleportTowards(Entity target) {
        Vector3d vector3d = new Vector3d(this.getX() - target.getX(), this.getY(0.5D) - target.getEyeY(), this.getZ() - target.getZ());
        vector3d = vector3d.normalize();
        double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.x * 16.0D;
        double d2 = this.getY() + (double)(this.random.nextInt(16) - 8) - vector3d.y * 16.0D;
        double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.z * 16.0D;
        return this.teleport(d1, d2, d3);
    }

    public boolean teleportAround(Entity target){
        Vector3d pos = RandomPositionGenerator.getLandPosTowards(this, 10, 7, target.position());
        if(pos != null){
            return this.teleport(pos.x, pos.y, pos.z);
        }
        return false;
    }

    /**
     * The actual teleport method that checks if the teleportation is possible.
     * @param x The x axis position of where to teleport to.
     * @param y The y axis position of where to teleport to.
     * @param z The z axis position of where to teleport to.
     * @return true if the entity can teleport to this position.
     */
    public boolean teleport(double x, double y, double z) {
        BlockPos.Mutable mutablePosition = new BlockPos.Mutable(x, y, z);

        while(mutablePosition.getY() > 0 && !this.level.getBlockState(mutablePosition).getMaterial().blocksMotion()) {
            mutablePosition.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(mutablePosition);
        boolean hasCollider = blockstate.getMaterial().blocksMotion();
        boolean isWater = blockstate.getFluidState().is(FluidTags.WATER);
        if (hasCollider && !isWater) {
            boolean canTransport = this.randomTeleport(x, y, z, true);
            if (canTransport && !this.isSilent()) {
                this.level.playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }

            return canTransport;
        }
        return false;
    }

    public boolean canTeleport(double x, double y, double z){
        double oX = this.getX();
        double oY = this.getY();
        double oZ = this.getZ();
        double d3 = y;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(x, y, z);
        World world = this.level;
        if (world.hasChunkAt(blockpos)) {
            boolean flag1 = false;

            while(!flag1 && blockpos.getY() > 0) {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = world.getBlockState(blockpos1);
                if (blockstate.getMaterial().blocksMotion()) {
                    flag1 = true;
                } else {
                    --d3;
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                this.teleportTo(x, d3, z);
                if (world.noCollision(this) && !world.containsAnyLiquid(this.getBoundingBox())) {
                    flag = true;
                }
            }
        }
        this.teleportTo(oX, oY, oZ);
        return flag;
    }

    public boolean teleport(BlockPos pos){
        return this.teleport(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * The ambient sound of the creature
     * @return SoundEvent of the ambient sound
     */
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isCreepy() ? SoundEvents.ENDERMAN_SCREAM : SoundEvents.ENDERMAN_AMBIENT;
    }

    /**
     * The sound that the entity makes when hurt. Can be different depending on the damage source.
     * @param damageSource: The type of the damage the entity took
     * @return SoundEvent of the damage sound
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENDERMAN_HURT;
    }

    /**
     * The sound that entity makes when it dies.
     * @return SoundEvent of the death sound
     */
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }

    /**
     * Updates everything that needs to be updated when an entity is hurt but the health and damage calculations happen
     * in the actuallyHurt method instead. The enderman negates indirect damage sources such as fireworks and projectiles
     * as well teleports in some cases.
     * @param damageSource: The damage source of the attack.
     * @param amount: How much raw damage the attack did.
     * @return true if the entity should be hurt.
     */
    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else if (damageSource instanceof IndirectEntityDamageSource) {
            for(int i = 0; i < 64; ++i) {
                if (this.teleport()) {
                    return true;
                }
            }

            return false;
        } else {
            boolean isHurt = super.hurt(damageSource, amount);
            if (!this.level.isClientSide() && !(damageSource.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
                this.teleport();
            }

            return isHurt;
        }
    }

    /**
     * Is the enderman angry or not.
     * @return true if the enderman is angry.
     */
    public boolean isCreepy() {
        return this.entityData.get(DATA_CREEPY);
    }

    /**
     * Has a player stared at this enderman.
     * @return true if a player has stared at this enderman.
     */
    public boolean hasBeenStaredAt() {
        return this.entityData.get(DATA_STARED_AT);
    }

    /**
     * Set "stared at" data parameter to true.
     */
    public void setBeingStaredAt() {
        this.entityData.set(DATA_STARED_AT, true);
    }

    public static List<BlockPos> getPathNodes(MobultionEndermanEntity entity){
        List<BlockPos> positions = new ArrayList<>();
        if(entity.getNavigation().getPath() == null) return positions;
        int size = entity.getNavigation().getPath().getNodeCount();
        for(int i = 0; i < size; i++){
            positions.add(entity.getNavigation().getPath().getNodePos(i));
        }
        return positions;
    }
}
