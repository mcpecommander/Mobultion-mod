package dev.mcpecommander.mobultion.entities.spiders.entities;

import com.mojang.math.Vector3f;
import dev.mcpecommander.mobultion.entities.spiders.SpidersConfig;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;

/* McpeCommander created on 08/08/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.entities */
public class HypnoWaveEntity extends AbstractHurtingProjectile implements IAnimatable {

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    //Use with caution, otherwise use the second one.
    public HypnoWaveEntity(EntityType<HypnoWaveEntity> projectileType, Level world){
        super(projectileType, world);
    }

    public HypnoWaveEntity(@Nonnull LivingEntity owner) {
        this(Registration.HYPNOWAVE.get(), owner.level);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1d, owner.getZ());
        this.setOwner(owner);
    }

    /**
     * The particles that will be spawned as the projectile is flying.
     * @return A particle type (implements IParticleData).
     */
    @Nonnull
    @Override
    protected ParticleOptions getTrailParticle() {
        return new DustParticleOptions(new Vector3f(0.612f, 0.149f, 0.784f), 1f);
    }

    /**
     * The acceleration scale which the speed is multiplied with each tick.
     * Values over 1 leads to accelerating particle movement while values between 0 and 1 leads to decelerating particle
     * movement.
     * @return the acceleration float value
     */
    @Override
    protected float getInertia() {
        return 1f;
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
        Entity entity = rayTraceResult.getEntity();
        if(this.getOwner() == null || !this.getOwner().isAlive() || !(entity instanceof LivingEntity)) return;
        entity.hurt(DamageSource.thrown(this, this.getOwner()), SpidersConfig.HYPNO_DAMAGE.get().floatValue());
        ((LivingEntity) entity).addEffect(new MobEffectInstance(Registration.HYPNO_EFFECT.get(), 20 * SpidersConfig.HYPNO_DURATION.get(),
                this.level.getDifficulty() == Difficulty.HARD ? 1 : 0));
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
