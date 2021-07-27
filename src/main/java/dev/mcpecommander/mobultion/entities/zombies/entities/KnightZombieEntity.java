package dev.mcpecommander.mobultion.entities.zombies.entities;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

/* McpeCommander created on 26/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.entities */
public class KnightZombieEntity extends MobultionZombieEntity {

    private static final DataParameter<Boolean> LEADER_DATA = EntityDataManager.defineId(KnightZombieEntity.class, DataSerializers.BOOLEAN);
    private final AnimationFactory factory = new AnimationFactory(this);

    public KnightZombieEntity(EntityType<KnightZombieEntity> type, World world) {
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
    public void addAdditionalSaveData(CompoundNBT NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        NBTTag.putBoolean("mobultion:leader", isLeader());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        this.setLeader(NBTTag.getBoolean("mobultion:leader"));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.75D);
    }

    //TODO: check if I want to keep
    @Override
    public boolean hurt(DamageSource damageSource, float hurtAmount) {
        if (!super.hurt(damageSource, hurtAmount)) {
            return false;
        }
        if(!level.isClientSide){
            ServerWorld serverworld = (ServerWorld)this.level;
            LivingEntity attacker = this.getTarget();
            if (attacker == null && damageSource.getEntity() instanceof LivingEntity) {
                attacker = (LivingEntity)damageSource.getEntity();
            }

            if (attacker != null && this.level.getDifficulty() == Difficulty.HARD
                    && (double)this.random.nextFloat() < this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).getValue()
                    && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {

                ZombieEntity zombieentity = new ZombieEntity(EntityType.ZOMBIE, level);

                for(int tries = 0; tries < 50; ++tries) {
                    int newX = this.blockPosition().getX() + MathHelper.nextInt(this.random, 7, 40)
                            * MathHelper.nextInt(this.random, -1, 1);
                    int newY = this.blockPosition().getY() + MathHelper.nextInt(this.random, 7, 40)
                            * MathHelper.nextInt(this.random, -1, 1);
                    int newZ = this.blockPosition().getZ() + MathHelper.nextInt(this.random, 7, 40)
                            * MathHelper.nextInt(this.random, -1, 1);
                    BlockPos blockpos = new BlockPos(newX, newY, newZ);
                    EntityType<?> entitytype = zombieentity.getType();
                    EntitySpawnPlacementRegistry.PlacementType placementType = EntitySpawnPlacementRegistry.getPlacementType(entitytype);
                    if (WorldEntitySpawner.isSpawnPositionOk(placementType, this.level, blockpos, entitytype)
                            && EntitySpawnPlacementRegistry.checkSpawnRules(entitytype, serverworld, SpawnReason.REINFORCEMENT, blockpos,
                            this.level.random)) {
                        zombieentity.setPos(newX, newY, newZ);
                        if (!this.level.hasNearbyAlivePlayer(newX, newY, newZ, 7.0D) && this.level.isUnobstructed(zombieentity)
                                && this.level.noCollision(zombieentity) && !this.level.containsAnyLiquid(zombieentity.getBoundingBox())) {
                            zombieentity.setTarget(attacker);
                            zombieentity.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(zombieentity.blockPosition()),
                                    SpawnReason.REINFORCEMENT, null, null);
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
    public boolean doHurtTarget(Entity target) {
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
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
        int i = this.random.nextInt(10);
        if (i == 0) {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
        } else {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }

    }

    @Override
    public void killed(ServerWorld world, LivingEntity killedEntity) {
        if (killedEntity instanceof PlayerEntity) {
            KnightZombieEntity follower = new KnightZombieEntity(Registration.KNIGHTZOMBIE.get(), world);
            follower.setPos(killedEntity.position().x(), killedEntity.position().y(), killedEntity.position().z());
            follower.setRot(killedEntity.yRot, killedEntity.xRot);
            follower.finalizeSpawn(world, world.getCurrentDifficultyAt(follower.blockPosition()),
                    SpawnReason.CONVERSION, null, null);
            world.addFreshEntity(follower);
        }
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason spawnReason,
                                           @Nullable ILivingEntityData livingEntityData, @Nullable CompoundNBT NBTTag) {
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * difficulty.getSpecialMultiplier());
        this.populateDefaultEquipmentSlots(difficulty);
        this.populateDefaultEquipmentEnchantments(difficulty);
        this.setLeader(spawnReason == SpawnReason.NATURAL && random.nextInt(10) == 0);

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
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime >= 2 && this.deathTime % 4 == 0) {
            for(double i = 0; i <= Math.PI*2; i += Math.PI/18) {
                this.level.addParticle(ParticleTypes.FLAME,
                        this.getX() + Math.cos(i) * 0.5d + (random.nextGaussian() * 0.01d - 0.005d),
                        this.blockPosition().getY() + random.nextGaussian() * 0.02f,
                        this.getZ() + Math.sin(i) * 0.5d + (random.nextGaussian() * 0.01d - 0.005d),
                        this.random.nextGaussian() * 0.001d - 0.0005d, 0.02d,
                        this.random.nextGaussian() * 0.001d - 0.0005d);
            }
        }
        if(this.deathTime == 46){
            this.remove();
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
