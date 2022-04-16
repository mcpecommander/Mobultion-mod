package dev.mcpecommander.mobultion.entities.zombies.entities;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* McpeCommander created on 26/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.entities */
public class KnightZombieEntity extends MobultionZombieEntity {

    private static final EntityDataAccessor<Boolean> LEADER_DATA = SynchedEntityData.defineId(KnightZombieEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimationFactory factory = new AnimationFactory(this);

    public KnightZombieEntity(EntityType<KnightZombieEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEADER_DATA, false);
    }

    public boolean isLeader(){
        return this.entityData.get(LEADER_DATA);
    }

    public void setLeader(boolean isLeader){
        this.entityData.set(LEADER_DATA, isLeader);
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        NBTTag.putBoolean("mobultion:leader", isLeader());
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        this.setLeader(NBTTag.getBoolean("mobultion:leader"));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.75D);
    }

    //TODO: check if I want to keep
    @Override
    public boolean hurt(@Nonnull DamageSource damageSource, float hurtAmount) {
        if (!super.hurt(damageSource, hurtAmount)) {
            return false;
        }
        if(!level.isClientSide){
            ServerLevel serverworld = (ServerLevel)this.level;
            LivingEntity attacker = this.getTarget();
            if (attacker == null && damageSource.getEntity() instanceof LivingEntity) {
                attacker = (LivingEntity)damageSource.getEntity();
            }

            if (attacker != null && this.level.getDifficulty() == Difficulty.HARD
                    && (double)this.random.nextFloat() < this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).getValue()
                    && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {

                Zombie zombieentity = new Zombie(EntityType.ZOMBIE, level);

                for(int tries = 0; tries < 50; ++tries) {
                    int newX = this.blockPosition().getX() + Mth.nextInt(this.random, 7, 40)
                            * Mth.nextInt(this.random, -1, 1);
                    int newY = this.blockPosition().getY() + Mth.nextInt(this.random, 7, 40)
                            * Mth.nextInt(this.random, -1, 1);
                    int newZ = this.blockPosition().getZ() + Mth.nextInt(this.random, 7, 40)
                            * Mth.nextInt(this.random, -1, 1);
                    BlockPos blockpos = new BlockPos(newX, newY, newZ);
                    EntityType<?> entitytype = zombieentity.getType();
                    SpawnPlacements.Type placementType = SpawnPlacements.getPlacementType(entitytype);
                    if (NaturalSpawner.isSpawnPositionOk(placementType, this.level, blockpos, entitytype)
                            && SpawnPlacements.checkSpawnRules(entitytype, serverworld, MobSpawnType.REINFORCEMENT, blockpos,
                            this.level.random)) {
                        zombieentity.setPos(newX, newY, newZ);
                        if (!this.level.hasNearbyAlivePlayer(newX, newY, newZ, 7.0D) && this.level.isUnobstructed(zombieentity)
                                && this.level.noCollision(zombieentity) && !this.level.containsAnyLiquid(zombieentity.getBoundingBox())) {
                            zombieentity.setTarget(attacker);
                            zombieentity.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(zombieentity.blockPosition()),
                                    MobSpawnType.REINFORCEMENT, null, null);
                            serverworld.addFreshEntityWithPassengers(zombieentity);
                            this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(
                                    new AttributeModifier("Zombie reinforcement caller charge", -0.05F,
                                            AttributeModifier.Operation.ADDITION));
                            zombieentity.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(
                                    new AttributeModifier("Zombie reinforcement callee charge", -0.05F,
                                            AttributeModifier.Operation.ADDITION));
                            break;
                        }
                    }
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity target) {
        boolean flag = super.doHurtTarget(target);
        if (flag) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                target.setSecondsOnFire(2 * (int)f);
            }
        }
        return flag;
    }


    @Override
    protected void populateDefaultEquipmentSlots(@Nonnull DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
        int i = this.random.nextInt(10);
        if (i == 0) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }

    }

    @Override
    public void killed(@Nonnull ServerLevel world, @Nonnull LivingEntity killedEntity) {
        if (killedEntity instanceof Player && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            KnightZombieEntity follower = new KnightZombieEntity(Registration.KNIGHTZOMBIE.get(), world);
            follower.setPos(killedEntity.position().x(), killedEntity.position().y(), killedEntity.position().z());
            follower.setRot(killedEntity.getYRot(), killedEntity.getXRot());
            follower.finalizeSpawn(world, world.getCurrentDifficultyAt(follower.blockPosition()),
                    MobSpawnType.CONVERSION, null, null);
            world.addFreshEntity(follower);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason,
                                           @Nullable SpawnGroupData livingEntityData, @Nullable CompoundTag NBTTag) {
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * difficulty.getSpecialMultiplier());
        this.populateDefaultEquipmentSlots(difficulty);
        this.populateDefaultEquipmentEnchantments(difficulty);
        this.setLeader(spawnReason == MobSpawnType.NATURAL && random.nextInt(10) == 0);

        if(isLeader()){
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier(
                    "Leader bonus", 0.1D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(
                    "Leader bonus", 10 * random.nextGaussian(), AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(new AttributeModifier(
                    "Leader bonus", 0.2D, AttributeModifier.Operation.ADDITION));
        }

        return super.finalizeSpawn(world, difficulty, spawnReason, livingEntityData, NBTTag);
    }

    @Override
    int getMaxDeathCount() {
        return 46;
    }

    @Override
    void deathParticles() {
        //Do not spawn too many particles.
        if (this.deathTime % 4 == 0) {
            for(double i = 0; i <= Math.PI*2; i += Math.PI/18d) {
                this.level.addParticle(ParticleTypes.FLAME,
                        //Math.cos(i) for the initial circle position on the x axis.
                        //* 0.5d to make the circle half as wide.
                        //(random.nextGaussian() * 0.01d - 0.005d) adds a small -0.01 - 0.01 variation to the diameter.
                        this.getX() + Mth.cos((float) i) * 0.5d + (random.nextGaussian() * 0.01d - 0.005d),
                        //this.blockPosition().getY() to make sure the particles spawn at ground level.
                        //(MathHelper.floor(this.getY()) would work too).
                        //+ random.nextGaussian() * 0.02f adds a small height variation.
                        this.blockPosition().getY() + random.nextGaussian() * 0.02f,
                        //Math.sin(i) for the initial circle position on the y axis.
                        //* 0.5d to make the circle half as wide.
                        //(random.nextGaussian() * 0.01d - 0.005d) adds a small -0.01 - 0.01 variation to the diameter.
                        this.getZ() + Mth.sin((float) i) * 0.5d + (random.nextGaussian() * 0.01d - 0.005d),
                        //Small random speeds on the x and z axis to make it feel alive.
                        this.random.nextGaussian() * 0.002d - 0.001d,
                        //Constant upwards speed.
                        0.02d,
                        this.random.nextGaussian() * 0.002d - 0.001d);
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "movement", 0, this::movementPredicate));
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::controllerPredicate));
    }

    /**
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState controllerPredicate(AnimationEvent<E> event)
    {
        if(isDeadOrDying()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    /**
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState movementPredicate(AnimationEvent<E> event)
    {
        if(isDeadOrDying()) return PlayState.STOP;
        if(event.isMoving()){
            //TODO: rework the speed stuff.
            if(this.animationSpeed > 0.6){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("running", true));
            }else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
            }
        }else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
