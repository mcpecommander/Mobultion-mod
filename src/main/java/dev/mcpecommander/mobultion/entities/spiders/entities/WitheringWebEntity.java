package dev.mcpecommander.mobultion.entities.spiders.entities;

import com.mojang.math.Vector3f;
import dev.mcpecommander.mobultion.utils.MathCalculations;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;

/* McpeCommander created on 18/05/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entities */
public class WitheringWebEntity extends AbstractHurtingProjectile implements IAnimatable {

    /**
     * The color data parameter to sync the color to the client side.
     */
    private static final EntityDataAccessor<Integer> DATA_TARGET = SynchedEntityData.defineId(WitheringWebEntity.class, EntityDataSerializers.INT);
    public double originalDistance, oldDistance = Double.MAX_VALUE;
    private int ticksFurther;
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public WitheringWebEntity(EntityType<WitheringWebEntity> projectileType, Level world){
        super(projectileType, world);
    }

    public WitheringWebEntity(EntityType<WitheringWebEntity> projectileType, double posX, double posY, double posZ,
                           double targetX, double targetY, double targetZ, Level world, LivingEntity owner, LivingEntity target) {
        super(projectileType, posX, posY, posZ, targetX, targetY, targetZ, world);
        this.setOwner(owner);
        this.setTarget(target);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TARGET, -1);
    }

    @Nullable
    public Entity getTarget(){
        return this.level.getEntity(this.entityData.get(DATA_TARGET));
    }

    public void setTarget(@Nullable Entity entity){
        this.entityData.set(DATA_TARGET, entity == null ? -1 : entity.getId());
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(DATA_TARGET.equals(key) && this.getTarget() != null){
            this.originalDistance = this.distanceTo(this.getTarget());
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if(getTarget() != null) compound.putUUID("mobultion:target", getTarget().getUUID());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if(compound.hasUUID("mobultion:target") && !this.level.isClientSide){
            this.setTarget(((ServerLevel)this.level).getEntity(compound.getUUID("mobultion:target")));
        }
    }

    @Override
    public void tick() {
        if(tickCount == 40) discard();
        super.tick();
        if(this.getTarget() != null) {
            double distance = this.distanceTo(this.getTarget());
            double scaleAmount = MathCalculations.map(distance, originalDistance, 0, 0.2, 1);
            float xScale = Mth.sin((float) Math.toRadians(this.getYRot() - 90));
            float zScale = Mth.cos((float) Math.toRadians(this.getYRot() - 90));
            this.setBoundingBox(this.getBoundingBox().inflate(xScale * scaleAmount, 0, zScale * scaleAmount).expandTowards(0, scaleAmount * 2, 0));
            if(distance > oldDistance &&
                    this.distanceToSqr(this.getTarget().xOld, this.getTarget().yOld, this.getTarget().zOld) >= distance){
                ticksFurther++;
            }
            oldDistance = distance;
            if(ticksFurther > 5) discard();
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if(flag) kill();
        return flag;
    }

    /**
     * The particles that will be spawned as the projectile is flying.
     * @return A particle type (implements IParticleData).
     */
    @Nonnull
    @Override
    protected ParticleOptions getTrailParticle() {
        return new DustParticleOptions(new Vector3f(0.188f, 0.106f, 0.247f), 1.5f);
    }

    /**
     * The acceleration scale which the speed is multiplied with each tick.
     * Values over 1 leads to accelerating particle movement while values between 0 and 1 leads to decelerating particle
     * movement.
     * @return the acceleration float value
     */
    @Override
    protected float getInertia() {
        return 0.9f;
    }

    /**
     * Whether this entity can burn and show the burning render.
     * @return true if the entity can burn.
     */
    @Override
    protected boolean shouldBurn() {
        return false;
    }

    /**
     * Gets called if the onHit RayTraceResult is instance of EntityRayTraceResult.
     * @param rayTraceResult has information about which entity got hit.
     */
    @Override
    protected void onHitEntity(@Nonnull EntityHitResult rayTraceResult) {
        if (!this.level.isClientSide) {
            Entity entity = rayTraceResult.getEntity();
            if(this.getOwner() == null || !this.getOwner().isAlive()) return;
            if(entity instanceof LivingEntity livingEntity){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 5 * 20, 0), this.getOwner());
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 0), this.getOwner());
            }
        }
    }

    /**
     * Gets called whenever the particle hits either a block or an entity.
     * @param result The RayTraceResult which has information about what was hit.
     */
    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);
        if((result.getType() == HitResult.Type.BLOCK && ticksFurther > 3) || result.getType() == HitResult.Type.ENTITY) {
            discard();
        }
    }

    /**
     * Whether this projectile can hit the given entity parameter.
     * @param entity the entity tested.
     * @return true if the entity can be hit by this projectile.
     */
    @Override
    protected boolean canHitEntity(@Nonnull Entity entity) {
        return entity.getClass() != WitherSpiderEntity.class && entity.getClass() != WitheringWebEntity.class
                && entity.getClass() != RedEyeEntity.class && super.canHitEntity(entity);
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {}


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