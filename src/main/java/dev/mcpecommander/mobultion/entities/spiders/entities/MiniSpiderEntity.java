package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.entities.spiders.entityGoals.FollowMotherGoal;
import dev.mcpecommander.mobultion.entities.spiders.entityGoals.MiniSpiderMeleeGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.UUID;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.entities */
public class MiniSpiderEntity extends MobultionSpiderEntity{

    private UUID ownerID;
    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public MiniSpiderEntity(EntityType<? extends MobultionSpiderEntity> type, Level world) {
        super(type, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new MiniSpiderMeleeGoal(this, 1.0F, 0.5F, 0.9F));
        this.goalSelector.addGoal(4, new FollowMotherGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 26.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 2D);
    }

    public void setOwnerID(UUID ownerID) {
        this.ownerID = ownerID;
        if (this.level instanceof ServerLevel && ((ServerLevel)this.level).getEntity(ownerID) instanceof MotherSpiderEntity mother){
            mother.addMiniSpider(this.getUUID());
        }
    }

    public UUID getOwnerID() {
        return ownerID;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(NBTTag.hasUUID("mobultion:ID")){
            this.ownerID = NBTTag.getUUID("mobultion:ID");
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        NBTTag.putUUID("mobultion:ID", this.ownerID);
    }

    @Override
    public void remove(@NotNull RemovalReason removalReason) {
        super.remove(removalReason);
        if (this.level instanceof ServerLevel && ((ServerLevel)this.level).getEntity(ownerID) instanceof MotherSpiderEntity mother
                && removalReason.shouldDestroy()){
            mother.removeMiniSpider(this.getUUID());
        }
    }

    @Nullable
    @Override
    public Entity changeDimension(@NotNull ServerLevel level, @NotNull ITeleporter teleporter) {
        if (this.level instanceof ServerLevel && ((ServerLevel)this.level).getEntity(ownerID) instanceof MotherSpiderEntity mother){
            mother.removeMiniSpider(this.getUUID());
        }
        Entity entity = super.changeDimension(level, teleporter);
        if(entity instanceof MiniSpiderEntity spider){
            spider.setOwnerID(null);
        }
        return entity;
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
     * The predicate for animation controller, I didn't use two different controllers because all the animations overlap
     * each other so there is no need of two concurrent animations.
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        if(isDeadOrDying()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
            return PlayState.CONTINUE;
        }
        if(event.isMoving() && this.isOnGround() && !this.isPassenger()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
            return PlayState.CONTINUE;
        }
        if(!this.isOnGround() && !this.isPassenger()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", false));
            return PlayState.CONTINUE;
        }
        if(this.isPassenger()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack_hold", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    /**
     * The y-offset for the entity while riding another entity.
     * @return a double of the position that should be added or subtracted from the y position of passenger entity.
     * 1 equals one block
     */
    @Override
    public double getMyRidingOffset() {
        return -0.3d;
    }

    /**
     * A custom death timer for the custom death animation I have made.
     * @return an integer of the ticks being ticked until the entity is removed.
     */
    @Override
    protected int getMaxDeathTick() {
        return 30;
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
