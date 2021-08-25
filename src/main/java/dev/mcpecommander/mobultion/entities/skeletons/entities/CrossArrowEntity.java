package dev.mcpecommander.mobultion.entities.skeletons.entities;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

/* McpeCommander created on 23/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public class CrossArrowEntity extends AbstractArrowEntity {

    /**
     * This parameter is always expected to have a value.
     */
    private Vector3d explosionPoint = Vector3d.ZERO;
    /**
     * Used for a janky proximity calculation.
     */
    private int closerTime, oCloserTime;

    public CrossArrowEntity(EntityType<CrossArrowEntity> type, World world) {
        super(type, world);
    }

    /**
     * What kind of item you get when the player intersects the arrow entity.
     * @return item stack instance of the item to be picked up.
     */
    @Nonnull
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    /**
     * A setter for the explosion point of this arrow.
     * @param target A vector3d for the coords of the explosion point.
     */
    public void setTarget(@Nonnull Vector3d target){
        this.explosionPoint = target;
    }

    /**
     * A getter for the explosion point of this arrow.
     * @return A vector3d of the explosion points coords.
     */
    @Nonnull
    public Vector3d getTarget(){
        return this.explosionPoint;
    }

    /**
     * The main tick method that has most of the logic for entities. A bit more advanced than baseTick method where this
     * method is called. Called on both the client and server side of course.
     */
    @Override
    public void tick() {
        super.tick();
        if(this.distanceToSqr(explosionPoint) <= explosionPoint.distanceToSqr(xOld, yOld, zOld)){
            closerTime++;
        }else{
            closerTime--;
        }
        if(closerTime < -100) remove();
        if(this.distanceToSqr(explosionPoint) < 1 || closerTime < oCloserTime - 1){
            this.explode();
        }

        oCloserTime = Math.max(closerTime, oCloserTime);
    }

    private void explode(){
        //Make sure to only explode if the shooter skeleton is alive otherwise we cannot get the target for distance
        //measuring and aiming, and make the sure the target is still alive and was not killed or went out of sight
        //otherwise we would get an NPE. I could pass the old location of the player at the moment this arrow was fired,
        //but that is more janky and not as accurate and would require more nbt data than I deem necessary.
        if(this.getOwner() != null && this.getOwner().isAlive()){
            LivingEntity target = ((ForestSkeletonEntity)this.getOwner()).getTarget();
            if(target != null) {
                this.playSound(Registration.SPLIT_SOUND.get(), 2f, 1f);
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        //This check gives me the X pattern.
                        if ((i == 0 || j == 0) && i != j) continue;
                        ArrowEntity arrow = new ArrowEntity(EntityType.ARROW, level);
                        arrow.setOwner(this.getOwner());
                        arrow.setPos(this.getX(), this.getY(), this.getZ());
                        double motionX = target.getX() + i - this.getX();
                        double motionY = target.getY(0.3333333333333333D) - arrow.getY();
                        double motionZ = target.getZ() + j - this.getZ();
                        //Calculates the horizontal distance to add a bit of lift to the arrow to simulate real life height adjustment
                        //for far away targets.
                        double horizontalDistance = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
                        //2 is the vector scaling factor which in turn translates into speed.
                        //The last parameter is the error scale. 0 = exact shot.
                        arrow.shoot(motionX, motionY + horizontalDistance * 0.2d, motionZ, 1.6F, 0);
                        this.level.addFreshEntity(arrow);
                    }
                }
            }
        }
        this.remove();
    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param NBTTag The tag where the additional data will be written to.
     */
    @Override
    public void addAdditionalSaveData(@Nonnull CompoundNBT NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        NBTTag.putDouble("mobultion:targetx", this.getTarget().x);
        NBTTag.putDouble("mobultion:targety", this.getTarget().y);
        NBTTag.putDouble("mobultion:targetz", this.getTarget().z);
    }

    /**
     * Reads the NBT tag and gets any pieces of saved data and applies it to the entity.
     * @param NBTTag The NBT tag that holds the saved data.
     */
    @Override
    public void readAdditionalSaveData(@Nonnull CompoundNBT NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(NBTTag.contains("mobultion:targetx", Constants.NBT.TAG_DOUBLE)){
            this.setTarget(new Vector3d(NBTTag.getDouble("mobultion:targetx"), NBTTag.getDouble("mobultion:targety"),
                    NBTTag.getDouble("mobultion:targetz")));
        }else{
            this.setTarget(Vector3d.ZERO);
        }
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
