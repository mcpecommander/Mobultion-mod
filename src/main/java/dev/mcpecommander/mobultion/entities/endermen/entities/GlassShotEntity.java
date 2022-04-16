package dev.mcpecommander.mobultion.entities.endermen.entities;

import dev.mcpecommander.mobultion.entities.endermen.EndermenConfig;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.util.Color;

import javax.annotation.Nonnull;

/* McpeCommander created on 03/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entities */
public class GlassShotEntity extends AbstractHurtingProjectile implements IAnimatable {

    /**
     * The color data parameter to sync the color to the client side.
     */
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(GlassShotEntity.class, EntityDataSerializers.INT);
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public GlassShotEntity(EntityType<GlassShotEntity> projectileType, Level world){
        super(projectileType, world);
    }

    public GlassShotEntity(EntityType<GlassShotEntity> projectileType, double posX, double posY, double posZ,
                           double targetX, double targetY, double targetZ, Level world, GlassEndermanEntity owner) {
        super(projectileType, posX, posY, posZ, targetX, targetY, targetZ, world);
        this.setOwner(owner);
        this.setColor(owner.getColor());
    }

    /**
     * Register/define the default value of the data parameter here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR, Color.ofRGB(255,255,255).getColor());
    }

    /**
     * Sets the color data parameter and syncs it to the client.
     * @param color the color that this enderman is rendered by.
     */
    public void setColor(Color color){
        this.entityData.set(DATA_COLOR, color.getColor());
    }

    /**
     * Gets the render color of this enderman (synced on both sides).
     * @return Color of this enderman.
     */
    public Color getColor(){
        return Color.ofOpaque(this.entityData.get(DATA_COLOR));
    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param NBTTag The tag where the additional data will be written to.
     */
    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        NBTTag.putInt("mobultion:color", getColor().getColor());
    }

    /**
     * Reads the NBT tag and gets any pieces of saved data and applies it to the entity.
     * @param NBTTag The NBT tag that holds the saved data.
     */
    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(NBTTag.contains("mobultion:color", Tag.TAG_INT)){
            setColor(Color.ofOpaque(NBTTag.getInt("mobultion:color")));
        }
    }

    /**
     * The particles that will be spawned as the projectile is flying.
     * @return A particle type (implements IParticleData).
     */
    @Nonnull
    @Override
    protected ParticleOptions getTrailParticle() {
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState());
    }

    /**
     * The acceleration scale which the speed is multiplied with each tick.
     * Values over 1 leads to accelerating particle movement while values between 0 and 1 leads to decelerating particle
     * movement.
     * @return the acceleration float value
     */
    @Override
    protected float getInertia() {
        return 1.05f;
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
            entity.hurt(DamageSource.thrown(this, this.getOwner()),
                    this.getOwner() instanceof Player ? EndermenConfig.SHOT_DAMAGE.get().floatValue() :
                            (float) ((LivingEntity) this.getOwner()).getAttributeValue(Attributes.ATTACK_DAMAGE));
        }
    }

    /**
     * Gets called whenever the particle hits either a block or an entity.
     * @param result The RayTraceResult which has information about what was hit.
     */
    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);
        if(this.level.isClientSide){
            this.level.playLocalSound(result.getLocation().x, result.getLocation().y, result.getLocation().z,
                    SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 2.5F, 1.0F, false);
        }
        discard();
    }

    /**
     * Whether this projectile can hit the given entity parameter.
     * @param entity the entity tested.
     * @return true if the entity can be hit by this projectile.
     */
    @Override
    protected boolean canHitEntity(@Nonnull Entity entity) {
        return entity != this.getOwner() && super.canHitEntity(entity);
    }

    /**
     * Whether this entity is in a water block. Used for particles and speed adjustments.
     * @return true if the entity current position is in a water block.
     */
    @Override
    public boolean isInWater() {
        return false;
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::shouldAnimate));
    }

    /**
     * The predicate for the animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <T extends IAnimatable> PlayState shouldAnimate(AnimationEvent<T> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("spin", true));
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
