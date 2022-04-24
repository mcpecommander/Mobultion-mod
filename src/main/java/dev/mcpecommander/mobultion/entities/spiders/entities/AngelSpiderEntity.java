package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.entities.spiders.entityGoals.AngelSpiderHealGoal;
import dev.mcpecommander.mobultion.entities.spiders.entityGoals.AngelSpiderTargetGoal;
import dev.mcpecommander.mobultion.particles.HealParticle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

/* Created by McpeCommander on 2021/06/18 */
public class AngelSpiderEntity extends MobultionSpiderEntity {

    /**
     * A data parameter to help keep the target in sync and to be saved (in another method) when quiting the game.
     */
    private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(AngelSpiderEntity.class, EntityDataSerializers.INT);
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public AngelSpiderEntity(EntityType<AngelSpiderEntity> mob, Level world) {
        super(mob, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.6D, 0.8D));
        this.goalSelector.addGoal(3, new AngelSpiderHealGoal(this));
        this.targetSelector.addGoal(1, new AngelSpiderTargetGoal(this));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    /**
     *
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        if(this.getTarget() != null){
            if(tickCount % 3 == 0){
                level.addParticle(new HealParticle.HealParticleData(1f, 1f, 1f, 1f),
                        this.getX(), this.getEyeY(), this.getZ(),
                        this.getTarget().getX(), this.getTarget().getEyeY(), this.getTarget().getZ());
            }
            if(event.getController().getCurrentAnimation() == null ||
                    (event.getController().getCurrentAnimation() != null &&
                            event.getController().getCurrentAnimation().animationName.equals("move"))) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", false));
            }
            if(event.getController().getCurrentAnimation() != null &&
                    event.getController().getCurrentAnimation().animationName.equals("attack")
                    && event.getController().getAnimationState() == AnimationState.Stopped){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("attack_hold", true));
            }
            return PlayState.CONTINUE;
        }
        if(event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
        }else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
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
     * Register/define the default value of the data parameter here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET, -1);
    }

    /**
     * Try to see if the target is not null otherwise try to retrieve it via the data parameter.
     * @return the current LivingEntity that this mob is targeting.
     */
    @Nullable
    @Override
    public LivingEntity getTarget() {
        LivingEntity target = super.getTarget();
        return target != null ? target : (LivingEntity) this.level.getEntity(this.entityData.get(TARGET));
    }

    /**
     * Write the target ID into the data parameter for syncing easy retrieval purposes later.
     * @param target A living entity or null if we want to reset the target.
     */
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (target != null) {
            this.entityData.set(TARGET, target.getId());
        } else {
            this.entityData.set(TARGET, -1);
        }
    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param NBTTag The tag where the additional data will be written to.
     */
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        if(getTarget() != null) {
            NBTTag.putUUID("mobultion:target", getTarget().getUUID());
        }
    }

    /**
     * Reads the NBT tag and gets any pieces of saved data and applies it to the entity.
     * @param NBTTag The NBT tag that holds the saved data.
     */
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(NBTTag.hasUUID("mobultion:target") && !this.level.isClientSide){
            this.setTarget((LivingEntity) ((ServerLevel)this.level).getEntity(NBTTag.getUUID("mobultion:target")));
        }
    }

    /**
     * The amount of ticks the entity ticks after it gets killed.
     * @return an integer of total death ticks
     */
    @Override
    protected int getMaxDeathTick() {
        return 20;
    }

    /**
     * Weather this mob can be affected by the potion effect in the params.
     * @param effect: the potion effect instance.
     * @return true if the mob can be affected.
     */
    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance effect) {
        return true;
    }

}
