package dev.mcpecommander.mobultion.entities.endermen.entities;

import dev.mcpecommander.mobultion.entities.endermen.entityGoals.EndermanFindStaringPlayerGoal;
import dev.mcpecommander.mobultion.entities.endermen.entityGoals.GlassEndermanShotsAttackGoal;
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
    private static final DataParameter<Integer> DATA_COLOR = EntityDataManager.defineId(GlassEndermanEntity.class, DataSerializers.INT);
    private static final DataParameter<Byte> DATA_BALLS = EntityDataManager.defineId(GlassEndermanEntity.class, DataSerializers.BYTE);

    public GlassEndermanEntity(EntityType<? extends MobultionEndermanEntity> type, World world) {
        super(type, world);
        //Set the priority to negative to signal that this entity avoids water at all costs.
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR, Color.WHITE.getRGB());
        this.entityData.define(DATA_BALLS, (byte)3);
    }

    public void setColor(Color color){
        this.entityData.set(DATA_COLOR, color.getRGB());
    }

    public Color getColor(){
        return new Color(this.entityData.get(DATA_COLOR));
    }

    public void setBalls(byte amount){
        this.entityData.set(DATA_BALLS, amount);
    }

    public byte getBalls(){
        return this.entityData.get(DATA_BALLS);
    }

    public void useBall(){
        this.entityData.set(DATA_BALLS, (byte) MathHelper.clamp(getBalls() - 1, 0, 3));
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(NBTTag.contains("mobultion:color", Constants.NBT.TAG_INT))this.setColor(new Color(NBTTag.getInt("mobultion:color")));
        if(NBTTag.contains("mobultion:balls", Constants.NBT.TAG_BYTE))this.setBalls(NBTTag.getByte("mobultion:balls"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        NBTTag.putInt("mobultion:color", getColor().getRGB());
        NBTTag.putByte("mobultion:balls", getBalls());
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance,
                                           SpawnReason spawnReason, @Nullable ILivingEntityData entityData,
                                           @Nullable CompoundNBT NBTTag) {
        setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
        return super.finalizeSpawn(serverWorld, difficultyInstance, spawnReason, entityData, NBTTag);
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
//        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new EndermanFindStaringPlayerGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CreeperEntity.class, 10, true, false, null));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 28.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

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
        if(isCreepy()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("rage", true));
        }else{
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
