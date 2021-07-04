package dev.mcpecommander.mobultion.entities.endermen.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Objects;

/* McpeCommander created on 03/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entities */
public class GlassShotEntity extends DamagingProjectileEntity implements IAnimatable {


    private final AnimationFactory factory = new AnimationFactory(this);

    //Don't use
    public GlassShotEntity(EntityType<GlassShotEntity> projectileType, World world){
        super(projectileType, world);
    }

//    public GlassShotEntity(EntityType<GlassShotEntity> projectileType, double posX, double posY, double posZ,
//                           double targetX, double targetY, double targetZ, World world, GlassEndermanEntity owner) {
//        super(projectileType, posX, posY, posZ, targetX, targetY, targetZ, world);
//        setPos(posX, posY, posZ);
//        this.setOwner(owner);
//    }

    @Override
    protected IParticleData getTrailParticle() {
        return new BlockParticleData(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState());
    }

    @Override
    protected float getInertia() {
        return 0.8f;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        System.out.println(super.shouldRender(p_145770_1_, p_145770_3_, p_145770_5_));
        return super.shouldRender(p_145770_1_, p_145770_3_, p_145770_5_);
    }

    @Override
    public void tick() {
        super.tick();
        if(tickCount % 100 == 0) System.out.println(position());
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult rayTraceResult) {
        if (!this.level.isClientSide) {
            Entity entity = rayTraceResult.getEntity();
            if(this.getOwner() == null || !this.getOwner().isAlive()) return;
            entity.hurt(DamageSource.thrown(this, this.getOwner()),
                    (float) ((LivingEntity) Objects.requireNonNull(this.getOwner())).getAttributeValue(Attributes.ATTACK_DAMAGE));
        }
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::shouldAnimate));
    }

    private <T extends IAnimatable> PlayState shouldAnimate(AnimationEvent<T> animationEvent) {
        animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("spin", true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new SSpawnObjectPacket(this, entity == null ? 0 : entity.getId());
    }
}
