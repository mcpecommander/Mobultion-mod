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
import net.minecraft.util.SoundEvent;
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

/* McpeCommander created on 21/06/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public class JokerSkeletonEntity extends MobultionSkeletonEntity implements IRangedAttackMob {

    //TODO: Should dance when it kills something or at least whistle and laugh.
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public JokerSkeletonEntity(EntityType<JokerSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new RangedBowAttackGoal<>(this, 1.0D, 10, 15.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_ON_LAND_SELECTOR));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return Registration.JOKER_SOUND.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 150;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    protected void populateDefaultEquipmentSlots(@Nonnull DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(difficulty);
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Registration.HEARTARROW_ITEM.get()));
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Registration.JOKERHAT.get()));
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(@Nonnull IServerWorld world, @Nonnull DifficultyInstance difficulty,
                                           @Nonnull SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityData,
                                           @Nullable CompoundNBT NBTTag) {
        this.populateDefaultEquipmentSlots(difficulty);
        this.setCanPickUpLoot(false);
        return super.finalizeSpawn(world, difficulty, spawnReason, livingEntityData, NBTTag);
    }

    public void performRangedAttack(LivingEntity shooter, float power) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(Hand.MAIN_HAND));
        AbstractArrowEntity arrow = this.getArrow(itemstack, power);
        arrow.setBaseDamage(0.5d);
        double d0 = shooter.getX() - this.getX();
        double d1 = shooter.getY(0.3333333333333333D) - arrow.getY();
        double d2 = shooter.getZ() - this.getZ();
        //Calculates the horizontal distance to add a bit of lift to the arrow to simulate real life height adjustment
        //for far away targets.
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        //1.6 is the vector scaling factor which in turn translates into speed.
        //The last parameter is the error scale. 0 = exact shot.
        arrow.shoot(d0, d1 + d3 * 0.3d, d2, 1.1F,
                18 - this.level.getCurrentDifficultyAt(blockPosition()).getSpecialMultiplier() * 12);
        this.playSound(Registration.HARP_SOUND.get(), 1.0F, this.getRandom().nextFloat() * 0.4F + 0.8F);
        this.level.addFreshEntity(arrow);
    }

    @Override
    protected int getMaxDeathTime() {
        return 50;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "movement", 0, this::movementPredicate));
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::attackPredicate));
    }

    /**
     *
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
     *
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event)
    {
        if(this.isDeadOrDying()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
            return PlayState.CONTINUE;
        }
        if(this.getTarget() != null){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("aim", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
