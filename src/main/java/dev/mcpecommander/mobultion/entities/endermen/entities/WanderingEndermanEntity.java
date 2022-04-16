package dev.mcpecommander.mobultion.entities.endermen.entities;

import dev.mcpecommander.mobultion.entities.endermen.EndermenConfig;
import dev.mcpecommander.mobultion.entities.endermen.entityGoals.EndermanFindStaringPlayerGoal;
import dev.mcpecommander.mobultion.entities.endermen.entityGoals.WanderingEndermanLightningAttackGoal;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Objects;

/* McpeCommander created on 25/06/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entities */
@SuppressWarnings("NullableProblems")
public class WanderingEndermanEntity extends MobultionEndermanEntity {

    /**
     * Is casting the lightning spell.
     */
    private static final EntityDataAccessor<Boolean> DATA_CASTING = SynchedEntityData.defineId(WanderingEndermanEntity.class, EntityDataSerializers.BOOLEAN);
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);
    /**
     * Variables to control the rotations of the cape based on the movement of the entity.
     */
    public double xCloakO, yCloakO, zCloakO, xCloak, yCloak, zCloak;

    public WanderingEndermanEntity(EntityType<WanderingEndermanEntity> entityType, Level world) {
        super(entityType, world);
        //Set the priority to negative to signal that this entity avoids water at all costs.
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    /**
     * Whether the entity is hurt by water, whether it is rain, bubble column or in water.
     * @return true if the entity is damaged by water.
     */
    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WanderingEndermanLightningAttackGoal(this, 60));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new EndermanFindStaringPlayerGoal(this, livingEntity -> true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Creeper.class, 10, true, false, null));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, EndermenConfig.WANDERING_HEALTH.get())
                .add(Attributes.ATTACK_DAMAGE, EndermenConfig.WANDERING_DAMAGE.get())
                .add(Attributes.MOVEMENT_SPEED, EndermenConfig.WANDERING_SPEED.get())
                .add(Attributes.FOLLOW_RANGE, EndermenConfig.WANDERING_RADIUS.get());
    }

    /**
     * The amount of ticks the entity ticks after it gets killed.
     * @return an integer of total death ticks
     */
    @Override
    protected int maxDeathAge() {
        return 27;
    }

    /**
     * Gets called every tick on the client side after the entity dies until its removed.
     */
    @Override
    protected void addDeathParticles() {}

    /**
     * Gets called every tick after the entity dies until it's removed.
     */
    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (this.deathTime == 25 && !level.isClientSide) {
            LightningBolt entity = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
            entity.setPos(this.getX(), this.getY(), this.getZ());
            entity.setVisualOnly(true);
            level.addFreshEntity(entity);
        }
    }

    /**
     * If the entity is immune to fire.
     * @return true if it is immune to fire.
     */
    @Override
    public boolean fireImmune() {
        return true;
    }

    /**
     * What kind of damage sources this entity is invulnerable to.
     * @param damageSource that damage source being tested.
     * @return true if the entity is invulnerable to the given damage source.
     */
    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if(damageSource == DamageSource.LIGHTNING_BOLT) return true;
        return super.isInvulnerableTo(damageSource);
    }

    /**
     * Register/define the default value of the data parameters here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CASTING, false);
    }

    /**
     * A setter for the casting data parameter
     * @param isCasting true to set the entity into casting mode and start the animation.
     */
    public void setCasting(boolean isCasting){
        this.entityData.set(DATA_CASTING, isCasting);
    }

    /**
     * A getter for the casting data parameter
     * @return true if the entity is in casting mode.
     */
    public boolean isCasting(){
        return this.entityData.get(DATA_CASTING);
    }

    /**
     * Copied from the PlayerEntity
     * Modifies variables that control the cape later on in the renderer.
     */
    private void moveCloak() {
        this.xCloakO = this.xCloak;
        this.yCloakO = this.yCloak;
        this.zCloakO = this.zCloak;
        double d0 = this.getX() - this.xCloak;
        double d1 = this.getY() - this.yCloak;
        double d2 = this.getZ() - this.zCloak;
        if (d0 > 10.0D) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }

        if (d2 > 10.0D) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }

        if (d1 > 10.0D) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }

        if (d0 < -10.0D) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }

        if (d2 < -10.0D) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }

        if (d1 < -10.0D) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }

        this.xCloak += d0 * 0.25D;
        this.zCloak += d2 * 0.25D;
        this.yCloak += d1 * 0.25D;
    }

    /**
     * The normal update method.
     */
    @Override
    public void tick() {
        super.tick();
        this.moveCloak();
    }

    /**
     * Used to finalize the normal spawning of mobs such as from eggs or normal world spawning but not commands.
     * @param serverWorld The server world instance.
     * @param difficulty The difficulty of the current world.
     * @param spawnReason How was this entity was spawned.
     * @param livingEntityData The entity data attached to it for further use upon spawning.
     * @param NBTTag The NBT tag that holds persisted data.
     * @return ILivingEntityData that holds information about the entity spawning.
     */
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverWorld, DifficultyInstance difficulty, MobSpawnType spawnReason,
                                           @Nullable SpawnGroupData livingEntityData, @Nullable CompoundTag NBTTag) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Registration.THUNDERSTAFF.get()));
        return super.finalizeSpawn(serverWorld, difficulty, spawnReason, livingEntityData, NBTTag);
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::shouldAnimate));
        data.addAnimationController(new AnimationController<>(this, "movement", 1, this::shouldMove));
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
     * The predicate for the movement animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState shouldMove(AnimationEvent<E> event)
    {
        if(isDeadOrDying()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
            return PlayState.CONTINUE;
        }
        if(event.isMoving()) {
            if(Objects.requireNonNull(getAttribute(Attributes.MOVEMENT_SPEED)).hasModifier(MobultionEndermanEntity.SPEED_MODIFIER_ATTACKING)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("running", true));
            }else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    /**
     * The predicate for the animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState shouldAnimate(AnimationEvent<E> event)
    {
        if(isCreepy() && !isDeadOrDying()) {
            if(isCasting()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", false));
            }else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("rage", true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
}
