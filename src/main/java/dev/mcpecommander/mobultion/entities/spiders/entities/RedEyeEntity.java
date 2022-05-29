package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.entities.spiders.entityGoals.RedEyeHoverAroundOwnerGoal;
import dev.mcpecommander.mobultion.entities.spiders.entityGoals.RedEyeZapAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.easing.EasingManager;
import software.bernie.geckolib3.core.easing.EasingType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.UUID;

import static dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity.Head.LEFT;
import static dev.mcpecommander.mobultion.entities.spiders.entities.WitherSpiderEntity.Head.RIGHT;

/* McpeCommander created on 14/05/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entities */
public class RedEyeEntity extends Monster implements IAnimatable {

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    /**
     * A simple data parameter to sync the launch animation with the goals.
     */
    private static final EntityDataAccessor<Boolean> DATA_LAUNCHING = SynchedEntityData.defineId(RedEyeEntity.class, EntityDataSerializers.BOOLEAN);
    /**
     * A data parameter to sync the owner id to the client for positioning and looking vector.
     */
    private static final EntityDataAccessor<Integer> DATA_OWNER = SynchedEntityData.defineId(RedEyeEntity.class, EntityDataSerializers.INT);
    /**
     * A data parameter to sync the target entity's id to the client.
     */
    private static final EntityDataAccessor<Integer> DATA_TARGET = SynchedEntityData.defineId(RedEyeEntity.class, EntityDataSerializers.INT);
    /**
     * Cache the owner instance since it shouldn't change in principle.
     */
    private WitherSpiderEntity ownerCache;
    /**
     * The owner UUID which is used to save owner entity permanently.
     */
    private UUID ownerUUID;
    /**
     * Which head does this entity belong to.
     */
    public WitherSpiderEntity.Head head = LEFT;

    public RedEyeEntity(EntityType<RedEyeEntity> entityType, Level world){
        super(entityType, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RedEyeZapAttackGoal(this));
        this.goalSelector.addGoal(2, new RedEyeHoverAroundOwnerGoal(this));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this){
            @Override
            public boolean canUse() {
                    return !isLaunching() && (getOwner() == null || distanceToSqr(getOwner().getHead(head).add(0, 2, 0)) < 1);
            }
        });
    }

    /**
     * Register/define the default value of the data parameter here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_LAUNCHING, false);
        this.entityData.define(DATA_OWNER, -1);
        this.entityData.define(DATA_TARGET, -1);
    }

    /**
     * A setter for the launching data parameter which is only used in the first 15 ticks.
     * @param launching true if the red eye should be launching.
     */
    public void setLaunching(boolean launching){
        this.entityData.set(DATA_LAUNCHING, launching);
    }

    /**
     * A getter for the launching data parameter which is only used in the first 15 ticks.
     * @return true is the red eye is launching right now.
     */
    public boolean isLaunching(){
        return this.entityData.get(DATA_LAUNCHING);
    }

    /**
     * A more advanced setter for the wither spider entity that owns this red eye. Sets both the data parameter and the UUID.
     * @param owner the wither spider entity that owns this entity or null to reset it.
     */
    public void setOwner(WitherSpiderEntity owner){
        if(owner != null){
            this.entityData.set(DATA_OWNER, owner.getId());
            this.ownerUUID = owner.getUUID();
        }else{
            this.entityData.set(DATA_OWNER, -1);
            this.ownerUUID = null;
        }
    }

    /**
     * A really advanced getter for the owner. It tries to fetch the owner from the cached version, if it is null, it tries to
     * fetch the target by the id data parameter or by UUID depending on the logical side.
     * @return the wither spider entity instance that owns this spider.
     */
    public WitherSpiderEntity getOwner(){
        if(this.ownerCache != null) return ownerCache;
        Entity entity;
        if(!this.level.isClientSide && ownerUUID != null){
            entity = ((ServerLevel) this.level).getEntity(ownerUUID);
            if(entity instanceof WitherSpiderEntity spider){
                this.entityData.set(DATA_OWNER, spider.getId());
                spider.setDeployed(head, getUUID());
                this.ownerCache = spider;
                return spider;
            }
        }else if(this.level.isClientSide){
            entity = this.level.getEntity(this.entityData.get(DATA_OWNER));
            if(entity instanceof WitherSpiderEntity spider) {
                this.ownerCache = spider;
                return spider;
            }
        }
        return null;
    }

    /**
     * The vanilla target setter but with a data parameter setter attached to it.
     * @param target the living entity that this entity is targeting or null to reset it.
     */
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        this.entityData.set(DATA_TARGET, target == null ? -1 : target.getId());
    }

    /**
     * The vanilla target getter but with an actual return on the client side using a data parameter to sync the entity's ID.
     * @return the living entity that this entity is targeting.
     */
    @Nullable
    @Override
    public LivingEntity getTarget() {
        if(this.level.isClientSide){
            Entity entity = this.level.getEntity(this.entityData.get(DATA_TARGET));
            return entity != null ? (LivingEntity) entity : null;
        }
        return super.getTarget();
    }

    /**
     * Setter for the head of the wither spider entity that owns this entity, because left and right heads own one red each.
     * @param head enum of the head that this red eye belongs to and tries to flock around once idle.
     */
    public void setHead(WitherSpiderEntity.Head head){
        this.head = head;
    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param compound The tag where the additional data will be written to.
     */
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if(this.getOwner() != null) compound.putUUID("mobultion:owner", this.getOwner().getUUID());
        if(this.getTarget() != null) compound.putUUID("mobultion:target", this.getTarget().getUUID());
        compound.putInt("mobultion:head", this.head.number);
    }

    /**
     * Reads the NBT tag and gets any pieces of saved data and applies it to the entity.
     * @param compound The NBT tag that holds the saved data.
     */
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if(compound.contains("mobultion:head", Tag.TAG_INT)){
            this.head = compound.getInt("mobultion:head") == 0 ? LEFT : RIGHT;
        }
        if(compound.hasUUID("mobultion:owner") && !this.level.isClientSide){
            this.ownerUUID = compound.getUUID("mobultion:owner");
        }
        if(compound.hasUUID("mobultion:target") && !this.level.isClientSide){
            this.setTarget((LivingEntity) ((ServerLevel)this.level).getEntity(compound.getUUID("mobultion:owner")));
        }
    }


    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    /**
     * The main tick method that has most of the logic for entities. Called on both the client and server side of course.
     */
    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        if(this.isLaunching()){
            this.setDeltaMovement(0, 1 - EasingManager.ease(tickCount/15d, EasingType.EaseOutCubic, null), 0);
            this.lookControl.setLookAt(this.position().add(0, 5, 0));
            if(tickCount == 15) this.setLaunching(false);
        }else {
            if (!this.level.isClientSide) {
                if (this.getOwner() == null || !this.getOwner().isAlive() || !this.getOwner().hasHead(this.head)) {
                    this.kill();
                }
            }
        }

        this.noPhysics = false;
        this.setNoGravity(true);

    }

    /**
     * Whether this entity can collide with the entity being passed in the parameter.
     * @param entity a non-null entity which this check is run for.
     * @return true if the two entity can collide as normal or whether the collision calculation is skipped.
     */
    @Override
    public boolean canCollideWith(@NotNull Entity entity) {
        return !(entity instanceof WitherSpiderEntity) && super.canCollideWith(entity);
    }

    /*
    double x = Mth.cos((float) ((speedMultiplier-tickCount)/(float)speedMultiplier * Math.PI)) * distance;
            double z;
            if(arcType == 0){
                z = Mth.sin((float) ((speedMultiplier-tickCount)/(float)speedMultiplier * Math.PI)) * distance * 0.2d;
            }else{
                z = Mth.sin((float) (-(speedMultiplier-tickCount)/(float)speedMultiplier * Math.PI)) * distance * 0.2d;
            }

            Vec3 vec3 = new Vec3(x, this.origin.y + yPower*tickCount, z);
            vec3 = vec3.yRot((float) -this.angle);
            this.setDeltaMovement(vec3.subtract(0, this.origin.y, 0));
            vec3 = vec3.add(this.center.x, 0, this.center.z);
     */

    /**
     * The last method that is called by every other method that kills or removes this entity.
     * @param reason An enum for the removal reason like killed or discarded.
     */
    @Override
    public void remove(@NotNull RemovalReason reason) {
        super.remove(reason);
        if(this.getOwner() != null) this.getOwner().setDeployed(this.head, null);
    }

    /**
     * The maximum rotation on the x-axis for this entity's head.
     * @return an integer of the maximum degrees, positive or negative, that this head can rotate towards.
     */
    @Override
    public int getMaxHeadXRot() {
        return 100;
    }

    /**
     * The maximum rotation on the y-axis for this entity's head.
     * @return an integer of the maximum degrees, positive or negative, that this head can rotate towards.
     */
    @Override
    public int getMaxHeadYRot() {
        return 180;
    }

    /**
     * Gets the eye height in relation to the collision box. Check {@linkplain Entity#getEyeY()} EyeY} too.
     * @param pose The current pose, since some entities have different eye height depending on the pose.
     * @return a float of the current eye height.
     */
    @Override
    public float getEyeHeight(@NotNull Pose pose) {
        return 0.35f;
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    /**
     * The predicate for animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("flap", true));
        return PlayState.CONTINUE;
    }

    /**
     * Getter for the animation factory. Client side only but not null on the server.
     * @return AnimationFactory
     */
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
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
