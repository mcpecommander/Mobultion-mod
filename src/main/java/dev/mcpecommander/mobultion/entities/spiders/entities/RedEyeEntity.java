package dev.mcpecommander.mobultion.entities.spiders.entities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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

    private static final EntityDataAccessor<Boolean> DATA_LAUNCHING = SynchedEntityData.defineId(RedEyeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_OWNER = SynchedEntityData.defineId(RedEyeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET = SynchedEntityData.defineId(RedEyeEntity.class, EntityDataSerializers.INT);
    private WitherSpiderEntity ownerCache;
    private UUID ownerUUID;
    private WitherSpiderEntity.Head head = LEFT;

    public RedEyeEntity(EntityType<RedEyeEntity> entityType, Level world){
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this){
            @Override
            public boolean canUse() {
                return !isLaunching() && getOwner() == null && super.canUse();
            }
        });
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_LAUNCHING, false);
        this.entityData.define(DATA_OWNER, -1);
        this.entityData.define(DATA_TARGET, -1);
    }

    public void setLaunching(boolean launching){
        this.entityData.set(DATA_LAUNCHING, launching);
    }

    public boolean isLaunching(){
        return this.entityData.get(DATA_LAUNCHING);
    }

    public void setOwner(WitherSpiderEntity owner){
        if(owner != null){
            this.entityData.set(DATA_OWNER, owner.getId());
            this.ownerUUID = owner.getUUID();
        }else{
            this.entityData.set(DATA_OWNER, -1);
            this.ownerUUID = null;
        }
    }

    public WitherSpiderEntity getOwner(){
        if(this.ownerCache != null) return ownerCache;
        Entity entity;
        if(!this.level.isClientSide && ownerUUID != null){
            entity = ((ServerLevel) this.level).getEntity(ownerUUID);
            if(entity instanceof WitherSpiderEntity spider){
                this.entityData.set(DATA_OWNER, spider.getId());
                spider.setDeployed(head, true);
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

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        this.entityData.set(DATA_TARGET, target == null ? -1 : target.getId());
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        if(this.level.isClientSide){
            Entity entity = this.level.getEntity(this.entityData.get(DATA_TARGET));
            return entity != null ? (LivingEntity) entity : null;
        }
        return super.getTarget();
    }

    public void setHead(WitherSpiderEntity.Head head){
        this.head = head;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if(this.getOwner() != null) compound.putUUID("mobultion:owner", this.getOwner().getUUID());
        compound.putInt("mobultion:head", this.head.number);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if(compound.contains("mobultion:head", Tag.TAG_INT)){
            this.head = compound.getInt("mobultion:head") == 0 ? LEFT : RIGHT;
        }
        if(compound.hasUUID("mobultion:owner") && !this.level.isClientSide){
            this.ownerUUID = compound.getUUID("mobultion:owner");
            //this.setOwner((WitherSpiderEntity) ((ServerLevel) this.level).getEntity(compound.getUUID("mobultion:owner")));
            //this.getOwner().setDeployed(head, true);
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
            if (this.getTarget() != null) {
                double distance = Mth.abs(Mth.sin(tickCount%160f/160f * Mth.PI)) * 4;
                if(distance < 2) distance = 2;
                double xTarget = Mth.cos(tickCount/40f * Mth.PI) * distance + this.getTarget().getX();
                double zTarget = Mth.sin(tickCount/40f * Mth.PI) * distance + this.getTarget().getZ();
                Vec3 deltaMovement = new Vec3(xTarget - this.getX(), this.getTarget().getY() + 2 - this.getY(), zTarget - this.getZ());

                this.setDeltaMovement(deltaMovement.scale(0.2D/deltaMovement.length())
                        .add(0, Mth.sin(tickCount%30f/30f * Mth.TWO_PI) * 0.05d, 0));

                this.getLookControl().setLookAt(this.getTarget());
            }

        }

        this.noPhysics = false;
        this.setNoGravity(true);

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

    @Override
    public void remove(@NotNull RemovalReason reason) {
        super.remove(reason);
        if(this.getOwner() != null) this.getOwner().setDeployed(this.head, false);
    }

    @Override
    public int getMaxHeadXRot() {
        return 100;
    }

    @Override
    public int getMaxHeadYRot() {
        return 180;
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
