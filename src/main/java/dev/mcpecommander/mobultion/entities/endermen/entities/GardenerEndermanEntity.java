package dev.mcpecommander.mobultion.entities.endermen.entities;

import dev.mcpecommander.mobultion.entities.endermen.entityGoals.GardenerEndermanGardenGoal;
import dev.mcpecommander.mobultion.entities.endermen.entityGoals.GardenerEndermanPosFinderGoal;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Objects;

/* McpeCommander created on 11/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entities */
public class GardenerEndermanEntity extends MobultionEndermanEntity{

    private static final DataParameter<Boolean> DATA_GARDENING = EntityDataManager.defineId(GardenerEndermanEntity.class, DataSerializers.BOOLEAN);
    private BlockPos targetPos;
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public GardenerEndermanEntity(EntityType<? extends MobultionEndermanEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.2D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, MobultionEndermanEntity.class,
                8.0F, 0.6D, 1.2D, livingEntity -> !(livingEntity instanceof GardenerEndermanEntity)));
        this.goalSelector.addGoal(2, new GardenerEndermanGardenGoal(this, 20));
        this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1.0D, 80));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(0, new GardenerEndermanPosFinderGoal(this, 20));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_GARDENING, false);
    }

    public void setGardening(boolean isGardening){
        this.entityData.set(DATA_GARDENING, isGardening);
    }

    public boolean isGardening(){
        return this.entityData.get(DATA_GARDENING);
    }

    public BlockPos getTargetPos(){
        return this.targetPos;
    }

    public void setTargetPos(BlockPos targetPos){
        this.targetPos = targetPos;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        if(targetPos != null) NBTTag.putIntArray("mobultion:targetpos", new int[]{getTargetPos().getX(),
                getTargetPos().getY(), getTargetPos().getZ()});
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(NBTTag.contains("mobultion:targetpos", Constants.NBT.TAG_INT_ARRAY)){
            setTargetPos(new BlockPos(NBTTag.getIntArray("mobultion:targetpos")[0],
                    NBTTag.getIntArray("mobultion:targetpos")[1], NBTTag.getIntArray("mobultion:targetpos")[2]));
        }
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4F).add(Attributes.FOLLOW_RANGE, 64.0D);
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
    protected void addDeathParticles() {

    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficulty, SpawnReason spawnReason,
                                           @Nullable ILivingEntityData livingEntityData, @Nullable CompoundNBT NBTTag) {
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Registration.HAYHATBLOCK_ITEM.get()));
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BONE_MEAL));
        return super.finalizeSpawn(serverWorld, difficulty, spawnReason, livingEntityData, NBTTag);
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
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
        if(event.isMoving() && !isGardening()) {
            if(Objects.requireNonNull(getAttribute(Attributes.MOVEMENT_SPEED)).hasModifier(MobultionEndermanEntity.SPEED_MODIFIER_ATTACKING)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("running", true));
            }else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
            }
            return PlayState.CONTINUE;
        }else if(isGardening()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bonemeal", false));
        }
        return PlayState.STOP;
    }

    /**
     * Getter for the animation factory. Client side only but not null on the server.
     * @return AnimationFactory
     */
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {
        return false;
    }

    public static GardenerEndermanGardenGoal.GardeningState checkPos(World level, BlockPos pos){
        BlockState blockState = level.getBlockState(pos);
        if(blockState.is(Tags.Blocks.DIRT)){
            BlockState above = level.getBlockState(pos.above());
            if(above.getBlock() instanceof IGrowable){
                return GardenerEndermanGardenGoal.GardeningState.BONEMEAL;
            }else if(above.getBlock() instanceof FlowerBlock){
                return GardenerEndermanGardenGoal.GardeningState.PICKING;
            } else if(above.isAir(level, pos.above())){
                return GardenerEndermanGardenGoal.GardeningState.PLANTING;
            }

        }else if(blockState.is(Blocks.FARMLAND) && blockState.getValue(FarmlandBlock.MOISTURE) < 7){
            return GardenerEndermanGardenGoal.GardeningState.WATERING;
        }
        return GardenerEndermanGardenGoal.GardeningState.NONE;
    }

}
