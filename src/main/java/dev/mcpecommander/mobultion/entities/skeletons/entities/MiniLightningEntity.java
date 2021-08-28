package dev.mcpecommander.mobultion.entities.skeletons.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* McpeCommander created on 28/08/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public class MiniLightningEntity extends Entity {

    /**
     * A data parameter to sync the healed entity id to the client to get the effect centered around it.
     */
    private static final DataParameter<Integer> DATA_TARGET = EntityDataManager.defineId(MiniLightningEntity.class, DataSerializers.INT);

    public MiniLightningEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
        this.noCulling = true;

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
            this.setPos(target.getX(), target.getY(), target.getZ());
        }
        //Lives for only 20 ticks.
        if(tickCount > 20){
            remove();
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
    protected void readAdditionalSaveData(@Nonnull CompoundNBT NBTTag) {

    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param NBTTag The tag where the additional data will be written to.
     */
    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundNBT NBTTag) {

    }

    /**
     * The spawning packet that is sent to client side to make it tick and render on the client side.
     * DO NOT USE the vanilla spawning packet because it doesn't work.
     * @return The spawning packet to be sent to the client.
     */
    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
