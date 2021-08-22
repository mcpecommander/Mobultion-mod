package dev.mcpecommander.mobultion.entities.skeletons.entities;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
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

/* McpeCommander created on 21/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public class ForestSkeletonEntity extends MobultionSkeletonEntity implements IRangedAttackMob {

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public ForestSkeletonEntity(EntityType<? extends MobultionSkeletonEntity> type, World world) {
        super(type, world);
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
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
        this.setItemInHand(Hand.MAIN_HAND, new ItemStack(Registration.FORESTBOW.get()));
        return super.finalizeSpawn(serverWorld, difficulty, spawnReason, livingEntityData, NBTTag);
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
        double motionX = target.getX() - this.getX();
        double motionY = target.getY(0.3333333333333333D) - arrow.getY();
        double motionZ = target.getZ() - this.getZ();
        //Calculates the horizontal distance to add a bit of lift to the arrow to simulate real life height adjustment
        //for far away targets.
        double horizontalDistance = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
        //2 is the vector scaling factor which in turn translates into speed.
        //The last parameter is the error scale. 0 = exact shot.
        arrow.shoot(motionX, motionY + horizontalDistance * 0.2d, motionZ, 2F,
                12 - this.level.getCurrentDifficultyAt(blockPosition()).getSpecialMultiplier() * 12);
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
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("melee", true));

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
