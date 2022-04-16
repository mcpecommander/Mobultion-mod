package dev.mcpecommander.mobultion.entities.endermen.entities;

import dev.mcpecommander.mobultion.Mobultion;
import dev.mcpecommander.mobultion.entities.endermen.EndermenConfig;
import dev.mcpecommander.mobultion.entities.endermen.entityGoals.GardenerEndermanGardenGoal;
import dev.mcpecommander.mobultion.entities.endermen.entityGoals.GardenerEndermanPosFinderGoal;
import dev.mcpecommander.mobultion.particles.FlowerParticle;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* McpeCommander created on 11/07/2021 inside the package - dev.mcpecommander.mobultion.entities.endermen.entities */
public class GardenerEndermanEntity extends MobultionEndermanEntity{

    /**
     * A data parameter that syncs the gardening boolean to the client for animation sake. Much better than sending a packet.
     */
    private static final EntityDataAccessor<Boolean> DATA_GARDENING = SynchedEntityData.defineId(GardenerEndermanEntity.class, EntityDataSerializers.BOOLEAN);
    /**
     * A data parameter that syncs a derp state for animation sake.
     */
    private static final EntityDataAccessor<Boolean> DATA_DERP = SynchedEntityData.defineId(GardenerEndermanEntity.class, EntityDataSerializers.BOOLEAN);
    /**
     * A data parameter for AI debugging purposes.
     */
    private static final EntityDataAccessor<List<BlockPos>> DATA_DEBUG = SynchedEntityData.defineId(GardenerEndermanEntity.class, Registration.BLOCKPOS_LIST);
    /**
     * The target position that this entity is targeting for its gardening purposes.
     */
    private BlockPos targetPos;
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public GardenerEndermanEntity(EntityType<? extends MobultionEndermanEntity> type, Level world) {
        super(type, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<Player>(this, Player.class,
                8.0F, 1.3D, 1.5D){
            @Override
            public void start() {
                super.start();
                setDerp(true);
            }

            @Override
            public void stop() {
                super.stop();
                setDerp(false);
            }
        });
        this.goalSelector.addGoal(1, new AvoidEntityGoal<MobultionEndermanEntity>(this, MobultionEndermanEntity.class,
                8.0F, 1.3D, 1.5D, livingEntity -> !(livingEntity instanceof GardenerEndermanEntity)){
            @Override
            public void start() {
                super.start();
                setDerp(true);
            }

            @Override
            public void stop() {
                super.stop();
                setDerp(false);
            }
        });
        this.goalSelector.addGoal(2, new GardenerEndermanGardenGoal(this, 30));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D, 80){
            @Override
            public void start() {
                super.start();
                //Only works when debug is on.
                setDebugRoad(MobultionEndermanEntity.getPathNodes((MobultionEndermanEntity) this.mob));
            }
        });
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new GardenerEndermanPosFinderGoal(this, 150, EndermenConfig.GARDENER_RADIUS.get()));

    }

    /**
     * The check for entity spawning naturally.
     * @param type The entity type that is being spawned.
     * @param world The world in which the entity is being spawned.
     * @param reason The spawning reason which can an egg, generation, mob spawner and so on.
     * @param pos The position where the entity is being spawned.
     * @param random The random instance.
     * @return true if the entity can be spawned on this location and world.
     */
    public static boolean checkMobSpawnRules(EntityType<? extends Mob> type, LevelAccessor world,
                                             MobSpawnType reason, BlockPos pos, Random random) {
        return world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP) && random.nextInt(10) == 0;
    }

    /**
     * Register/define the default value of the data parameter here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_GARDENING, false);
        this.entityData.define(DATA_DERP, false);
        this.entityData.define(DATA_DEBUG, new ArrayList<>());
    }

    /**
     * Set the debugging path using a list of block positions instead of the nodes.
     * @param positions the list of block positions to add to the debugging path.
     */
    public void setDebugRoad(List<BlockPos> positions){
        if(!Mobultion.DEBUG) return;
        this.entityData.set(DATA_DEBUG, positions);
    }

    /**
     * Gets the debugging path on both sides.
     * @return the debugging path as a block positions list.
     */
    public List<BlockPos> getDebugRoad(){
        return this.entityData.get(DATA_DEBUG);
    }

    /**
     * Is called whenever a data parameter is changed and is being synced between the server and client.
     * @param syncedParameter The parameter that is being synced.
     */
    @Override
    public void onSyncedDataUpdated(@Nonnull EntityDataAccessor<?> syncedParameter) {
        super.onSyncedDataUpdated(syncedParameter);
        if(syncedParameter.equals(DATA_GARDENING) && this.level.isClientSide){
            if(isGardening()) {
                for (int i = 0; i < 5; i++) {
                    this.level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                            this.getX() + Math.cos(-yBodyRot) * 2 + (random.nextFloat() * 0.5f - 0.25f),
                            this.getY() + (random.nextFloat() * 0.5f - 0.25f),
                            this.getZ() + Math.sin(-yBodyRot) * 2 + (random.nextFloat() * 0.5f - 0.25f),
                            0, -0.1, 0);
                }
            }
        }
    }

    /**
     * Sets this entity gardening data parameter.
     */
    public void setGardening(boolean isGardening){
        this.entityData.set(DATA_GARDENING, isGardening);
    }

    /**
     * Gets the gardening data parameter.
     * @return true if this entity is gardening.
     */
    public boolean isGardening(){
        return this.entityData.get(DATA_GARDENING);
    }

    /**
     * Sets this entity derping data parameter.
     */
    public void setDerp(boolean isDerping){
        this.entityData.set(DATA_DERP, isDerping);
    }

    /**
     * Gets the derping data parameter.
     * @return true if this entity is derp walking.
     */
    public boolean isDerping(){
        return this.entityData.get(DATA_DERP);
    }

    /**
     * Get the target position that this entity wants to garden. Can be null.
     * @return Block position of the dirt block or crop block that is going to be gardened.
     */
    @Nullable
    public BlockPos getTargetPos(){
        return this.targetPos;
    }

    /**
     * Set the target position for this entity to garden or null to reset the gardening.
     * @param targetPos The block position to make the target or null to reset.
     */
    public void setTargetPos(BlockPos targetPos){
        this.targetPos = targetPos;
    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param NBTTag The tag where the additional data will be written to.
     */
    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        if(targetPos != null) NBTTag.putIntArray("mobultion:targetpos", new int[]{targetPos.getX(),
                targetPos.getY(), targetPos.getZ()});
    }

    /**
     * Reads the NBT tag and gets any pieces of saved data and applies it to the entity.
     * @param NBTTag The NBT tag that holds the saved data.
     */
    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(NBTTag.contains("mobultion:targetpos", Tag.TAG_INT_ARRAY)){
            setTargetPos(new BlockPos(NBTTag.getIntArray("mobultion:targetpos")[0],
                    NBTTag.getIntArray("mobultion:targetpos")[1], NBTTag.getIntArray("mobultion:targetpos")[2]));
        }
    }

    /**
     * Gets the block "walk" value which is used in path finding to make sure that the best path is chosen.
     * Gets for example in sun fleeing AI goal.
     * @param pos The block position the walking value is being tested for.
     * @param world The world instance.
     * @return A float value where higher is better and anything under 0.0f (not 0.0f) blocks natural spawning.
     */
    @Override
    public float getWalkTargetValue(@Nonnull BlockPos pos, LevelReader world) {
        return world.getBlockState(pos).is(BlockTags.DIRT) ? 15f : (15f - world.getBrightness(pos));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, EndermenConfig.GARDENER_HEALTH.get())
                .add(Attributes.MOVEMENT_SPEED, EndermenConfig.GARDENER_SPEED.get());
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
        if(this.deathTime >= 10 && this.deathTime < 20){
            for(int i = 0; i < 5; i++) {
                double finalX = this.getX() + Math.cos(random.nextFloat() * Math.PI * 2) * 2;
                double finalY = this.getY(1f) + (random.nextFloat() * 4 - 2);
                double finalZ = this.getZ() + Math.sin(random.nextFloat() * Math.PI * 2) * 2;
                Vec3 speed = new Vec3(finalX - this.getX(),
                        finalY - getY(2f/3f),
                        finalZ - this.getZ()).normalize();
                this.level.addParticle(new FlowerParticle.FlowerParticleData(1f, 1f, 1f, 1f, 3f),
                        this.getX(), getY(2f/3f), this.getZ(),
                        speed.x/10f, speed.y/10f, speed.z/10f);
            }
        }
    }

    /**
     * Gets called on the client side each tick to spawn ambient particles.
     */
    @Override
    protected void addAmbientParticles() {
        if(tickCount % 2 == 0) return;
        this.level.addParticle(new FlowerParticle.FlowerParticleData(1f,1f,1f,1f, 1f),
                this.getRandomX(0.5D),
                this.getRandomY() - 0.25D,
                this.getRandomZ(0.5D),
                this.random.nextDouble() * 0.1 - 0.05,
                this.random.nextDouble() * 0.1 - 0.05,
                this.random.nextDouble() * 0.1 - 0.05);
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
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor serverWorld, @Nonnull DifficultyInstance difficulty,
                                           @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData livingEntityData,
                                           @Nullable CompoundTag NBTTag) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Registration.HAYHAT_ITEM.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BONE_MEAL));
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
            if(isDerping()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("derp", true));
            }else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
            }
            return PlayState.CONTINUE;
        }
        if(isGardening()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bonemeal", false));
            return PlayState.CONTINUE;
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

    /**
     * Whether this entity can trample the current farmland.
     * @param state The block state of the farmland that this entity landed on.
     * @param pos The positions of this farmland.
     * @param fallDistance How much this entity fell down.
     * @return true if the farmland was trampled and returned to its dirt state.
     */
    @Override
    public boolean canTrample(@Nonnull BlockState state, @Nonnull BlockPos pos, float fallDistance) {
        return false;
    }

    /**
     * Gets the {@link GardeningState} of this position.
     * @param level The world in which the position is being tested.
     * @param pos The position being tested.
     * @return {@link GardeningState} enum of what to do if the block is to be gardened.
     */
    public static GardeningState checkPos(Level level, BlockPos pos){
        BlockState blockState = level.getBlockState(pos);
        if(blockState.is(BlockTags.DIRT)){
            BlockState above = level.getBlockState(pos.above());
            if(above.getBlock() instanceof BonemealableBlock){
                return GardeningState.BONEMEAL;
            }else if(above.getBlock() instanceof FlowerBlock){
                return GardeningState.PICKING;
            } else if(above.isAir()){
                return GardeningState.PLANTING;
            }

        }else if(blockState.is(Blocks.FARMLAND) && blockState.getValue(FarmBlock.MOISTURE) < 7){
            return GardeningState.WATERING;
        }
        return GardeningState.NONE;
    }

    /**
     * The gardening state that this enderman can have for its target goal.
     */
    public enum GardeningState{
        BONEMEAL, WATERING, PLANTING, PICKING, NONE
    }

}
