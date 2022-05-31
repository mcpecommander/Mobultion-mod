package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.entities.spiders.entityGoals.MobultionSpiderMoveControl;
import dev.mcpecommander.mobultion.entities.spiders.entityGoals.ThreeAttackableTargetsGoal;
import dev.mcpecommander.mobultion.entities.spiders.entityGoals.WitherSpiderAttackGoal;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.UUID;

/* McpeCommander created on 18/06/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.entities */
public class WitherSpiderEntity extends MobultionSpiderEntity{

    //Temp
    BlockPos pos1, pos2;
    /**
     * The extra heads rotation.
     */
    public float[] headYRot = new float[2];
    public float[] headXRot = new float[2];
    /**
     * The target's sync accessor
     */
    private static final EntityDataAccessor<Integer> LEFT_TARGET = SynchedEntityData.defineId(WitherSpiderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> RIGHT_TARGET = SynchedEntityData.defineId(WitherSpiderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(WitherSpiderEntity.class, EntityDataSerializers.INT);

    private final UUID[] deployedEyes = new UUID[2];
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);
    float prevHealth;
    int timer = -1;
    boolean isDroppingLeftHead, isDroppingRightHead = false;

    public WitherSpiderEntity(EntityType<WitherSpiderEntity> mob, Level world) {
        super(mob, world);
        prevHealth = this.getMaxHealth();
    }

    /**
     * Register/define the default value of the data parameter here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEFT_TARGET, -1);
        this.entityData.define(RIGHT_TARGET, -1);
        this.entityData.define(TARGET, -1);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        //super.registerGoals();
        this.goalSelector.addGoal(3, new WitherSpiderAttackGoal(this, 1.0, 0.3f, 0.4f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ThreeAttackableTargetsGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new ThreeAttackableTargetsGoal<>(this, IronGolem.class, true));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 2D);
    }

    @Nullable
    public LivingEntity getTarget(Head head){
        return (LivingEntity) this.level.getEntity(head == Head.LEFT ? this.entityData.get(LEFT_TARGET)
                                                            : this.entityData.get(RIGHT_TARGET));
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        if(this.level.isClientSide){
            return (LivingEntity) this.level.getEntity(this.entityData.get(TARGET));
        }
        return super.getTarget();
    }

    public void setTarget(Head head, @Nullable LivingEntity target){
        this.entityData.set(head == Head.LEFT ? LEFT_TARGET : RIGHT_TARGET, target == null ? -1 : target.getId());
        if(target != null && getDeployed(head) != null){
            Entity entity = ((ServerLevel) this.level).getEntity(getDeployed(head));
            if(entity instanceof RedEyeEntity eye){
                eye.setTarget(target);
            }
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity) {
        super.setTarget(livingEntity);
        this.entityData.set(TARGET, livingEntity == null ? -1 : livingEntity.getId());
    }


    public void lookAt(Vec3 position, float maxYawIncrease, float maxPitchIncrease, Head head) {
        Vec3 distanceVector = position.subtract(getHead(head));
        //180/PI is to convert from radians to degrees.
        float yRot = (float)(Mth.atan2(distanceVector.z, distanceVector.x) * 180F / Math.PI) - 90.0F;
        float xRot = (float)-(Mth.atan2(distanceVector.y, distanceVector.horizontalDistance()) * 180F /Math.PI);
        this.headYRot[head.number] = this.rotlerp(this.headYRot[head.number], yRot, maxYawIncrease);
        this.headXRot[head.number] = this.rotlerp(this.headXRot[head.number], xRot, maxPitchIncrease);
        //System.out.println(this.headYRot[head] + ", " + this.headXRot[head]);
    }

    public void lookAt(Entity entity, float maxYawIncrease, float maxPitchIncrease, Head head) {
        double y;
        if (entity instanceof LivingEntity livingentity) {
            y = livingentity.getEyeY();
        } else {
            y = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0D;
        }
        lookAt(new Vec3(entity.getX(), y, entity.getZ()), maxYawIncrease, maxPitchIncrease, head);
    }

    private float rotlerp(float angle, float targetAngle, float maxIncrease){
        float f = Mth.wrapDegrees(targetAngle - angle);
        f = Mth.clamp(f, -maxIncrease, maxIncrease);
        return angle + f;
    }

    public Vec3 getHead(Head head){
        Vec3 pos = new Vec3(head.offset, getEyeHeight() + 0.15d, 1d);
        return pos.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
    }

    public boolean hasHead(Head head){
        return head == Head.LEFT ? this.getHealth() > 2/3f * getMaxHealth() : this.getHealth() > 1/3f * getMaxHealth();
    }

    public UUID getDeployed(Head head){
        return this.deployedEyes[head.number];
    }

    public void setDeployed(Head head, UUID deployed){
        this.deployedEyes[head.number] = deployed;
    }

    /**
     * The predicate for animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        if (prevHealth != this.getHealth()){
            if (prevHealth > 2f/3f * this.getMaxHealth() &&
                    this.getHealth() <= 2f/3f * this.getMaxHealth() &&
                    this.getHealth() > 1f/3f * this.getMaxHealth()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("head1_drop", false));

                prevHealth = this.getHealth();
                return PlayState.CONTINUE;
            }
            if (prevHealth > 1f/3f * this.getMaxHealth() &&
                    this.getHealth() <= 1f/3f * this.getMaxHealth()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("head2_drop", false));

                prevHealth = this.getHealth();
                return PlayState.CONTINUE;
            }
        }
        prevHealth = this.getHealth();

        if(this.isDeadOrDying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
            return PlayState.CONTINUE;
        }
        if(event.isMoving()){
            if(event.getController().getCurrentAnimation() == null){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
                return PlayState.CONTINUE;
            }
        }else{
            if(event.getController().getCurrentAnimation() == null ||
                    !event.getController().getCurrentAnimation().animationName.equals("move")){
                return PlayState.CONTINUE;
            }else{
                return PlayState.STOP;
            }
        }
        return PlayState.CONTINUE;
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
     * Getter for the animation factory. Client side only but not null on the server.
     * @return AnimationFactory
     */
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    /**
     * Runs on the server only and does the actual damage calculations and lowers the entity's health.
     * @param damageSource: Type of damage the entity received.
     * @param amount: the amount of damage the entity received.
     */
    @Override
    protected void actuallyHurt(@NotNull DamageSource damageSource, float amount) {
        if(isDroppingLeftHead || isDroppingRightHead) return;
        float prev = this.getHealth();
        super.actuallyHurt(damageSource, Mth.clamp(amount, 0, 1f/3f * this.getMaxHealth()));
        if(prev < 1f/3f * this.getMaxHealth() || this.isDeadOrDying()) return;
        if(prev > 2f/3f * this.getMaxHealth()
                && this.getHealth() <= 2f/3f * this.getMaxHealth()
                && this.getHealth() > 1f/3f * this.getMaxHealth()){
            timer = 15;
            isDroppingLeftHead = true;
        }
        if(prev > 1f/3f * this.getMaxHealth()
                && this.getHealth() <= 1f/3f * this.getMaxHealth()){
            timer = 15;
            isDroppingRightHead = true;
        }
    }

    @Override
    protected int getMaxDeathTick() {
        return 35;
    }

    /**
     * The main update method which ticks on both sides all the time until the entity is removed.
     * Ticks on the server side only when the entity is not rendered.
     */
    @Override
    public void tick() {
        super.tick();
        //System.out.println("spider: " + this.getYHeadRot() + ", " + this.yBodyRot);
        if(!this.level.isClientSide && (isDroppingLeftHead || isDroppingRightHead)){
            if(timer == 0){
                Vec3 pos = new Vec3(isDroppingLeftHead ? 1d : -1d, 0.0d, 1d);
                pos = pos.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                WitherHeadBugEntity bug = new WitherHeadBugEntity(Registration.WITHERHEADBUG.get(), this.level);
                bug.setPos(pos.x, pos.y, pos.z);
                this.level.addFreshEntity(bug);
                isDroppingLeftHead = false;
                isDroppingRightHead = false;
            }
            timer --;
        }
        if(this.level.isClientSide) {
            if(this.getTarget(Head.LEFT) == null && this.getTarget() == null) {
                if (this.tickCount % 40 == 0 && this.random.nextFloat() < 0.2f) {
                    pos1 = this.blockPosition().offset(RandomPos.generateRandomDirection(this.random, 30, 30));
                } else if (pos1 != null) {
                    this.lookAt(Vec3.atCenterOf(pos1), 15, 15, Head.LEFT);
                }
            }else if(this.getTarget(Head.LEFT) != null){
                this.lookAt(this.getTarget(Head.LEFT), 15, 15, Head.LEFT);
            }else{
                this.lookAt(this.getTarget(), 15, 15, Head.LEFT);
            }
            if(this.getTarget(Head.RIGHT) == null && this.getTarget() == null){
                if (this.tickCount % 40 == 0 && this.random.nextFloat() < 0.2f) {
                    pos2 = this.blockPosition().offset(RandomPos.generateRandomDirection(this.random, 30, 30));
                } else if (pos2 != null){
                    this.lookAt(Vec3.atCenterOf(pos2), 15, 15, Head.RIGHT);
                }
            }else if(this.getTarget(Head.RIGHT) != null){
                this.lookAt(this.getTarget(Head.RIGHT), 15, 15, Head.RIGHT);
            }else{
                this.lookAt(this.getTarget(), 15, 15, Head.RIGHT);
            }
        }
    }

    /**
     * Removes the entity from the level. Gets called when the death timer has reached 20 or if the entity is despawned.
     */
    @Override
    public void remove(@NotNull RemovalReason removalReason) {
        super.remove(removalReason);
        if(!this.level.isClientSide && removalReason == RemovalReason.KILLED) {
            AreaEffectCloud cloudEntity = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
            cloudEntity.setOwner(this);
            cloudEntity.setRadius(3.0F);
            cloudEntity.setRadiusOnUse(-0.5F);
            cloudEntity.setWaitTime(10);
            cloudEntity.setRadiusPerTick(-cloudEntity.getRadius() / (float) cloudEntity.getDuration());
            cloudEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 160));

            this.level.addFreshEntity(cloudEntity);
        }

    }

    @Override
    public boolean canCollideWith(@NotNull Entity entity) {
        return !(entity instanceof RedEyeEntity) && super.canCollideWith(entity);
    }

    /**
     * Weather this mob can be affected by the potion effect in the params.
     * @param effectInstance: the potion effect instance.
     * @return true if the mob can be affected.
     */
    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        MobEffect effect = effectInstance.getEffect();
        return effect != MobEffects.REGENERATION && effect != MobEffects.POISON && effect != MobEffects.WITHER;
    }

    public enum Head{
        RIGHT(1, -0.625D), LEFT(0, 0.625);

        public final int number;
        public final double offset;

        Head(int number, double offset){
            this.number = number;
            this.offset = offset;
        }
    }
}
