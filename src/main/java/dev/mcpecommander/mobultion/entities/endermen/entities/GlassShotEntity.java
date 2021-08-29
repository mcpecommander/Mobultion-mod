package dev.mcpecommander.mobultion.entities.endermen.entities;

import dev.mcpecommander.mobultion.entities.endermen.EndermenConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.awt.*;

/* McpeCommander created on 03/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entities */
public class GlassShotEntity extends DamagingProjectileEntity implements IAnimatable {

    /**
     * The color data parameter to sync the color to the client side.
     */
    private static final DataParameter<Integer> DATA_COLOR = EntityDataManager.defineId(GlassShotEntity.class, DataSerializers.INT);
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public GlassShotEntity(EntityType<GlassShotEntity> projectileType, World world){
        super(projectileType, world);
    }

    public GlassShotEntity(EntityType<GlassShotEntity> projectileType, double posX, double posY, double posZ,
                           double targetX, double targetY, double targetZ, World world, GlassEndermanEntity owner) {
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
        this.entityData.define(DATA_COLOR, Color.WHITE.getRGB());
    }

    /**
     * Sets the color data parameter and syncs it to the client.
     * @param color the color that this enderman is rendered by.
     */
    public void setColor(Color color){
        this.entityData.set(DATA_COLOR, color.getRGB());
    }

    /**
     * Gets the render color of this enderman (synced on both sides).
     * @return Color of this enderman.
     */
    public Color getColor(){
        return new Color(this.entityData.get(DATA_COLOR));
    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param NBTTag The tag where the additional data will be written to.
     */
    @Override
    public void addAdditionalSaveData(@Nonnull CompoundNBT NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        NBTTag.putInt("mobultion:color", getColor().getRGB());
    }

    /**
     * Reads the NBT tag and gets any pieces of saved data and applies it to the entity.
     * @param NBTTag The NBT tag that holds the saved data.
     */
    @Override
    public void readAdditionalSaveData(@Nonnull CompoundNBT NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(NBTTag.contains("mobultion:color", Constants.NBT.TAG_INT)){
            setColor(new Color(NBTTag.getInt("mobultion:color")));
        }
    }

    /**
     * The particles that will be spawned as the projectile is flying.
     * @return A particle type (implements IParticleData).
     */
    @Nonnull
    @Override
    protected IParticleData getTrailParticle() {
        return new BlockParticleData(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState());
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
    protected void onHitEntity(@Nonnull EntityRayTraceResult rayTraceResult) {
        if (!this.level.isClientSide) {
            Entity entity = rayTraceResult.getEntity();
            if(this.getOwner() == null || !this.getOwner().isAlive()) return;
            entity.hurt(DamageSource.thrown(this, this.getOwner()),
                    this.getOwner() instanceof PlayerEntity ? new Float(EndermenConfig.SHOT_DAMAGE.get()) :
                            (float) ((LivingEntity) this.getOwner()).getAttributeValue(Attributes.ATTACK_DAMAGE));
        }
    }

    /**
     * Gets called whenever the particle hits either a block or an entity.
     * @param result The RayTraceResult which has information about what was hit.
     */
    @Override
    protected void onHit(@Nonnull RayTraceResult result) {
        super.onHit(result);
        if(this.level.isClientSide){
            this.level.playLocalSound(result.getLocation().x, result.getLocation().y, result.getLocation().z,
                    SoundEvents.GLASS_BREAK, SoundCategory.BLOCKS, 2.5F, 1.0F, false);
        }
        remove();
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
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
