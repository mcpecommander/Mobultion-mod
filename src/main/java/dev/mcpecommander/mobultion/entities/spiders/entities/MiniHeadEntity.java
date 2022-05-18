package dev.mcpecommander.mobultion.entities.spiders.entities;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;

/* McpeCommander created on 14/05/2022 inside the package - dev.mcpecommander.mobultion.entities.spiders.entities */
public class MiniHeadEntity extends AbstractHurtingProjectile implements IAnimatable {

    private static final EntityDataAccessor<CompoundTag> DATA = SynchedEntityData.defineId(MiniHeadEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private Vec3 center = Vec3.ZERO;
    private Vec3 origin = Vec3.ZERO;
    public Vec3 target = Vec3.ZERO;
    private double distance, angle;
    private int arcType;
    private int speedMultiplier = 10;
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    //DO NOT USE
    public MiniHeadEntity(EntityType<MiniHeadEntity> projectileType, Level world){
        super(projectileType, world);
    }

    public MiniHeadEntity(EntityType<MiniHeadEntity> projectileType, Level world, WitherSpiderEntity owner) {
        super(projectileType, world);
        this.setOwner(owner);
    }

    public void setData(Vec3 pos, Vec3 target, int arcType){
        updateData(pos, target, arcType);
        CompoundTag tag = this.entityData.get(DATA);
        tag.putDouble("x", pos.x);
        tag.putDouble("y", pos.y);
        tag.putDouble("z", pos.z);
        tag.putDouble("tx", target.x);
        tag.putDouble("ty", target.y);
        tag.putDouble("tz", target.z);
        tag.putInt("arcType", arcType);
        this.entityData.set(DATA, tag);
    }

    private void updateData(Vec3 pos, Vec3 target, int arcType){
        this.moveTo(pos.x, pos.y, pos.z, this.getYRot(), this.getXRot());
        this.reapplyPosition();
        this.origin = pos;
        this.target = target;
        this.xPower = (target.x - pos.x)/(float)speedMultiplier;
        this.yPower = (target.y - pos.y)/(float)speedMultiplier;
        this.zPower = (target.z - pos.z)/(float)speedMultiplier;
        this.angle = Math.atan2(target.z - pos.z, target.x - pos.x);
        this.center = target.subtract(pos).scale(0.5d).add(pos);
        this.distance = Math.sqrt(this.distanceToSqr(center));
        //this.speedMultiplier = Mth.ceil(distance * 2);
        this.arcType = arcType;
    }

    /**
     * Is called whenever a data parameter is changed and is being synced between the server and client.
     * @param key The parameter that is being synced.
     */
    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(DATA.equals(key)){
            CompoundTag tag = this.entityData.get(DATA);
            Vec3 pos = new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
            Vec3 target = new Vec3(tag.getDouble("tx"), tag.getDouble("ty"), tag.getDouble("tz"));
            int arcType = tag.getInt("arcType");
            updateData(pos, target, arcType);
        }
    }

    /**
     * Reads the NBT tag and gets any pieces of saved data and applies it to the entity.
     * @param compoundTag The NBT tag that holds the saved data.
     */
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.put("mobultion:data", this.entityData.get(DATA));
    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param compoundTag The tag where the additional data will be written to.
     */
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if(compoundTag.contains("mobultion:data", Tag.TAG_COMPOUND)) {
            this.entityData.set(DATA, compoundTag.getCompound("mobultion:data"));
        }
    }

    /**
     * Register/define the default value of the data parameter here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA, new CompoundTag());
    }

    /**
     * The main tick method that has most of the logic for entities. Called on both the client and server side of course.
     */
    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if(this.tickCount == speedMultiplier) discard();
        if (this.level.isClientSide || (entity == null || !entity.isRemoved()) && this.level.hasChunkAt(this.blockPosition())) {
            this.baseTick();
            HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS) {
                this.onHit(hitresult);
            }
            this.checkInsideBlocks();

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

            if(tickCount%2==0)this.level.addParticle(this.getTrailParticle(), vec3.x, vec3.y + 0.5D, vec3.z, 0.0D, 0.0D, 0.0D);
            this.setPos(vec3);
        } else {
            this.discard();
        }
    }

    /**
     * The particles that will be spawned as the projectile is flying.
     * @return A particle type (implements IParticleData).
     */
    @Nonnull
    @Override
    protected ParticleOptions getTrailParticle() {
        return DustParticleOptions.REDSTONE;
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
            if(entity.hurt(DamageSource.thrown(this, this.getOwner()), 0.2f)){
                entity.invulnerableTime = 1;
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
        discard();
    }

    /**
     * Whether this projectile can hit the given entity parameter.
     * @param entity the entity tested.
     * @return true if the entity can be hit by this projectile.
     */
    @Override
    protected boolean canHitEntity(@Nonnull Entity entity) {
        return entity.getClass() != WitherSpiderEntity.class && entity.getClass() != MiniHeadEntity.class && super.canHitEntity(entity);
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
