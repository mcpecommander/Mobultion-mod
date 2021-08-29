package dev.mcpecommander.mobultion.entities.skeletons.entities;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* McpeCommander created on 22/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public class MagmaSkeletonEntity extends MobultionSkeletonEntity implements IRangedAttackMob {

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public MagmaSkeletonEntity(EntityType<? extends MobultionSkeletonEntity> type, World world) {
        super(type, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(2, new RangedBowAttackGoal<>(this, 1.0D, 10, 15.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_ON_LAND_SELECTOR));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 24)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Registration.RANGED_DAMAGE.get(), 1.5D);
    }

    /**
     * Whether this entity should be burning from the sun when exposed to it.
     * @return true if the entity should be burning now.
     */
    @Override
    protected boolean isSunBurnTick() {
        return false;
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
    public ILivingEntityData finalizeSpawn(@Nonnull IServerWorld serverWorld, @Nonnull DifficultyInstance difficulty,
                                           @Nonnull SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityData,
                                           @Nullable CompoundNBT NBTTag) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        return super.finalizeSpawn(serverWorld, difficulty, spawnReason, livingEntityData, NBTTag);
    }

    /**
     * Used to create an instance of the arrow entity for when this entity fires or shoots an arrow.
     * @param bow The bow item being used by this entity right now.
     * @param power The power of the bow usually given in the attack goal or AI.
     * @return An arrow entity instance
     */
    @Override
    protected AbstractArrowEntity getArrow(ItemStack bow, float power) {
        AbstractArrowEntity arrowEntity = super.getArrow(bow, power);
        arrowEntity.setSecondsOnFire(50);
        return arrowEntity;
    }

    /**
     * Gets called from ranged attack goals/AI. The projectile entity and its parameters get initiated here.
     * @param target The entity being targeted.
     * @param power The power of the arrow, usually defined from the goal itself for each mob.
     */
    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(Hand.MAIN_HAND));
        AbstractArrowEntity arrow = this.getArrow(itemstack, power);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(1d/3d) - arrow.getY();
        double d2 = target.getZ() - this.getZ();
        //Calculates the horizontal distance to add a bit of lift to the arrow to simulate real life height adjustment
        //for far away targets.
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        //1.6 is the vector scaling factor which in turn translates into speed.
        //The last parameter is the error scale. 0 = exact shot.
        arrow.shoot(d0, d1 + d3 * 0.2d, d2, 1.6F, 3);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    /**
     * The amount of ticks the entity ticks after it gets killed.
     * @return an integer of total death ticks
     */
    @Override
    protected int getMaxDeathTime() {
        return 50;
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
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
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
            return PlayState.CONTINUE;
        }
        if(isAggressive()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("aim", true));
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

    /**
     * Getter for the animation factory. Client side only but not null on the server.
     * @return AnimationFactory
     */
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
