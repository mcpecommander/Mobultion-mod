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

    private static final DataParameter<Integer> DATA_TARGET = EntityDataManager.defineId(MiniLightningEntity.class, DataSerializers.INT);

    public MiniLightningEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
        this.noCulling = true;

    }

    @Override
    public void tick() {
        super.tick();
        Entity target = this.getTarget();
        if(target != null && target.isAlive()){
            this.setPos(target.getX(), target.getY(), target.getZ());
        }
        if(tickCount > 20){
            remove();
        }
    }

    public void setTarget(int entityID){
        this.entityData.set(DATA_TARGET, entityID);
    }

    @Nullable
    public Entity getTarget(){
        return this.level.getEntity(this.entityData.get(DATA_TARGET));
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_TARGET, -1);
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundNBT NBTTag) {

    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundNBT NBTTag) {

    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
