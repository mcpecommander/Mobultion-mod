package dev.mcpecommander.mobultion.entities.skeletons.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* McpeCommander created on 28/08/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public class MiniLightningEntity extends Entity {

    /**
     * A data parameter to sync the healed entity id to the client to get the effect centered around it.
     */
    private static final EntityDataAccessor<Integer> DATA_TARGET = SynchedEntityData.defineId(MiniLightningEntity.class, EntityDataSerializers.INT);

    public MiniLightningEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
        setNoGravity(true);

    }

    /**
     * The main tick method that gets called on both the server and client.
     */
    @Override
    public void tick() {
        super.tick();
        Entity target = this.getTarget();
        if(target != null && target.isAlive()){
            //Move the effect towards the entity as long as it is available.
            this.setPos(target.getX(), target.getY() + target.getBbHeight() - (target.getBbHeight() * tickCount/20), target.getZ());
        }
        //Lives for only 20 ticks.
        if(tickCount > 20){
            discard();
        }
    }

    /**
     * Set the target id and save it to the entity data to be synced to the client.
     * @param entityID The target's id
     */
    public void setTarget(int entityID){
        this.entityData.set(DATA_TARGET, entityID);
    }

    /**
     * Gets the target entity using the id saved in the data parameters, although it can be null if the target was removed.
     * @return The target entity from the world entity list if it exists otherwise null.
     */
    @Nullable
    public Entity getTarget(){
        return this.level.getEntity(this.entityData.get(DATA_TARGET));
    }

    /**
     * Register/define the default value of the data parameter here.
     */
    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_TARGET, -1);
    }

    /**
     * Reads the NBT tag and gets any pieces of saved data and applies it to the entity.
     * @param NBTTag The NBT tag that holds the saved data.
     */
    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag NBTTag) {

    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param NBTTag The tag where the additional data will be written to.
     */
    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag NBTTag) {

    }

    /**
     * The spawning packet that is sent to client side to make it tick and render on the client side.
     * DO NOT USE the vanilla spawning packet because it doesn't work.
     * @return The spawning packet to be sent to the client.
     */
    @Nonnull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
