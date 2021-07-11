package dev.mcpecommander.mobultion.entities.endermen.entities;

import dev.mcpecommander.mobultion.entities.endermen.entityGoals.EndermanFindStaringPlayerGoal;
import dev.mcpecommander.mobultion.entities.endermen.entityGoals.GlassEndermanShotsAttackGoal;
import dev.mcpecommander.mobultion.particles.PortalParticle;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Objects;

/* McpeCommander created on 02/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entities */
public class GlassEndermanEntity extends MobultionEndermanEntity{

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);
    /**
     * The color data parameter to sync the color to the client side.
     */
    private static final DataParameter<Integer> DATA_COLOR = EntityDataManager.defineId(GlassEndermanEntity.class, DataSerializers.INT);
    /**
     * The amount of balls data parameter to sync the amount of balls to the client side.
     */
    private static final DataParameter<Byte> DATA_BALLS = EntityDataManager.defineId(GlassEndermanEntity.class, DataSerializers.BYTE);

    public GlassEndermanEntity(EntityType<? extends MobultionEndermanEntity> type, World world) {
        super(type, world);
        //Set the priority to negative to signal that this entity avoids water at all costs.
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
    }

    /**
     * Register/define the default value of the data parameter here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR, Color.WHITE.getRGB());
        this.entityData.define(DATA_BALLS, (byte)3);
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
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficulty,
                                           SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityData,
                                           @Nullable CompoundNBT NBTTag) {
        setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
        return super.finalizeSpawn(serverWorld, difficulty, spawnReason, livingEntityData, NBTTag);
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
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new GlassEndermanShotsAttackGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<PlayerEntity>(this, PlayerEntity.class,
                8.0F, 0.6D, 1.2D, livingEntity -> getBalls() <= 1){
            @Override
            public void start() {
                super.start();
                //Sets the target to null to make sure the speed modifier is off.
                if(getTarget() != null) setTarget(null);
            }
        });
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new EndermanFindStaringPlayerGoal(this, livingEntity -> getBalls() > 0));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CreeperEntity.class, 10, true, false, livingEntity -> getBalls() > 0));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this){
            @Override
            public boolean canUse() {
                //Makes sure that player is not targeted if the enderman doesn't have balls to shoot.
                return super.canUse() && getBalls() > 0;
            }
        });
        this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));
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
     * Sets the balls data parameter and syncs it to the client.
     * @param amount the amount of balls that this enderman have.
     */
    public void setBalls(byte amount){
        this.entityData.set(DATA_BALLS, amount);
    }

    /**
     * Gets the amount of balls this enderman has (synced on both sides).
     * @return byte ball amount of this enderman.
     */
    public byte getBalls(){
        return this.entityData.get(DATA_BALLS);
    }

    /**
     * Decreases the amount of balls by one clamped between 0 and 3.
     */
    public void useBall(){
        this.entityData.set(DATA_BALLS, (byte) MathHelper.clamp(getBalls() - 1, 0, 3));
    }

    /**
     * Reads the NBT tag and gets any pieces of saved data and applies it to the entity.
     * @param NBTTag The NBT tag that holds the saved data.
     */
    @Override
    public void readAdditionalSaveData(CompoundNBT NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(NBTTag.contains("mobultion:color", Constants.NBT.TAG_INT))this.setColor(new Color(NBTTag.getInt("mobultion:color")));
        if(NBTTag.contains("mobultion:balls", Constants.NBT.TAG_BYTE))this.setBalls(NBTTag.getByte("mobultion:balls"));
    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param NBTTag The tag where the additional data will be written to.
     */
    @Override
    public void addAdditionalSaveData(CompoundNBT NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        NBTTag.putInt("mobultion:color", getColor().getRGB());
        NBTTag.putByte("mobultion:balls", getBalls());
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 28.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    /**
     * Another update method for mobs that only ticks on the server, has multiple uses.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if(this.tickCount % 150 == 0){
            this.setBalls((byte) MathHelper.clamp(getBalls() + 1, 0, 3));
        }
    }

    /**
     * The amount of ticks the entity ticks after it gets killed.
     * @return an integer of total death ticks
     */
    @Override
    protected int maxDeathAge() {
        return 30;
    }

    /**
     * Gets called every tick on the client side after the entity dies until its removed.
     */
    @Override
    protected void addDeathParticles() {
        if(this.deathTime == 25){
            for(int i = 0; i < 40; i++) {
                double finalX = this.getX() + Math.cos(random.nextFloat() * Math.PI * 2) * 2;
                double finalY = this.getY(0.5f) + (random.nextFloat() * 2 - 1);
                double finalZ = this.getZ() + Math.sin(random.nextFloat() * Math.PI * 2) * 2;
                Vector3d speed = new Vector3d(finalX - this.getX(),
                        finalY - getY(2f/3f),
                        finalZ - this.getZ()).normalize();
                this.level.addParticle(new PortalParticle.PortalParticleData((this.getColor().getRed()/255f),
                                (this.getColor().getGreen()/255f), (this.getColor().getBlue()/255f),0.5f
                                ,(float) finalX, (float) finalY, (float) finalZ),
                        this.getX(), getY(2f/3f), this.getZ(),
                        speed.x/20f, speed.y/20f, speed.z/20f);
            }
        }
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 1, this::shouldAnimate));
        data.addAnimationController(new AnimationController<>(this, "movement", 1, this::shouldMove));
    }

    /**
     * The predicate for the movement animations.
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
        if(isDeadOrDying()) return PlayState.STOP;
        if(isCreepy()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("rage", true));
        }else{
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        }
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
}
