package dev.mcpecommander.mobultion.entities.endermen.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
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

import java.awt.*;

/* McpeCommander created on 03/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entities */
public class GlassShotEntity extends DamagingProjectileEntity implements IAnimatable {

    private static final DataParameter<Integer> DATA_COLOR = EntityDataManager.defineId(GlassShotEntity.class, DataSerializers.INT);
    private final AnimationFactory factory = new AnimationFactory(this);

    //Don't use
    public GlassShotEntity(EntityType<GlassShotEntity> projectileType, World world){
        super(projectileType, world);
    }

    public GlassShotEntity(EntityType<GlassShotEntity> projectileType, double posX, double posY, double posZ,
                           double targetX, double targetY, double targetZ, World world, GlassEndermanEntity owner) {
        super(projectileType, posX, posY, posZ, targetX, targetY, targetZ, world);
        this.setOwner(owner);
        //Workaround because of a bug in geckolib projectile renderer.
        //Check https://github.com/bernie-g/geckolib/issues/152
        this.setColor(new Color(owner.getColor().getRed(), owner.getColor().getBlue(), owner.getColor().getGreen()));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR, Color.WHITE.getRGB());
    }

    public void setColor(Color color){
        this.entityData.set(DATA_COLOR, color.getRGB());
    }

    public Color getColor(){
        return new Color(this.entityData.get(DATA_COLOR));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        NBTTag.putInt("mobultion:color", getColor().getRGB());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(NBTTag.contains("mobultion:color", Constants.NBT.TAG_INT)){
            setColor(new Color(NBTTag.getInt("mobultion:color")));
        }
    }

    @Override
    protected IParticleData getTrailParticle() {
        return new BlockParticleData(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState());
    }

    @Override
    protected float getInertia() {
        return 1.05f;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult rayTraceResult) {
        if (!this.level.isClientSide) {
            Entity entity = rayTraceResult.getEntity();
            if(this.getOwner() == null || !this.getOwner().isAlive()) return;
            entity.hurt(DamageSource.thrown(this, this.getOwner()),
                    (float) ((LivingEntity) this.getOwner()).getAttributeValue(Attributes.ATTACK_DAMAGE));
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if(this.level.isClientSide){
            this.level.playLocalSound(result.getLocation().x, result.getLocation().y, result.getLocation().z,
                    SoundEvents.GLASS_BREAK, SoundCategory.BLOCKS, 2.5F, 1.0F, false);
        }
        remove();
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return entity != this && super.canHitEntity(entity);
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
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
