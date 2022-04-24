package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.entities.spiders.entityGoals.MotherSpiderEggLayingGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.entities */
public class MotherSpiderEntity extends MobultionSpiderEntity{

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    /**
     * A list of children mini spiders that this mother spider own.
     */
    private final List<UUID> miniSpiders = new ArrayList<>();

    /**
     * A data parameter to help keep the pregnancy status in sync and to be saved (in another method) when quiting the game.
     */
    private static final EntityDataAccessor<Byte> PREGNANT = SynchedEntityData.defineId(MotherSpiderEntity.class, EntityDataSerializers.BYTE);

    public MotherSpiderEntity(EntityType<? extends MobultionSpiderEntity> type, Level world) {
        super(type, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new MotherSpiderEggLayingGoal(this));
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

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "movement", 0, this::movementPredicate));
    }

    /**
     * The predicate for animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState movementPredicate(AnimationEvent<E> event)
    {
        if(this.isDeadOrDying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
            return PlayState.CONTINUE;
        }
        if(event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    /**
     * Calculates and applies the knock-back after this entity has gotten hurt. Has a forge hook for modders to affect
     * it via an event. Even knock-back resistance is accounted for here.
     * @param strength The strength of the knock-back. Normal values are somewhere around 0.4-0.5
     * @param xVec The x-ratio of the knock-back. Usually just the x-pos of the attacker - the x-pos of this.
     * @param zVec The z-ratio of the knock-back. Usually just the z-pos of the attacker - the z-pos of this.
     */
    @Override
    public void knockback(double strength, double xVec, double zVec) {
        //For animation’s sake, don't do knock-back calculations if the last hit killed this.
        if(this.isDeadOrDying()) return;
        super.knockback(strength, xVec, zVec);
    }

    /**
     * The predicate for animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        if(this.isDeadOrDying()) {
            return PlayState.STOP;
        }
        if(this.getPregnancyStatus() == 1){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("pregnant", false));
            return PlayState.CONTINUE;
        }else if (getPregnancyStatus() == 2){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("pregnant_hold", true));
            return PlayState.CONTINUE;
        }else if ((getPregnancyStatus() == 3)){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("birth", false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    /**
     * The amount of ticks the entity ticks after it gets killed.
     * @return an integer of total death ticks
     */
    @Override
    protected int getMaxDeathTick() {
        return 30;
    }

    /**
     * Register/define the default value of the data parameter here.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PREGNANT, (byte)0);
    }

    /**
     * A public setter of the gestation status. If the value supplied is different from the old one, it will be synced
     * to the client
     * @param pregnancyStatus A byte value of the gestation status. 0: not pregnant. 1: eggs growing. 2: eggs ready to
     *                        be laid. 3: eggs laying.
     */
    public void setPregnant(byte pregnancyStatus){
        this.entityData.set(PREGNANT, pregnancyStatus);
    }

    /**
     * Retrieve the gestation status. Guaranteed to be the same on both the client and the server.
     * @return the gestation status 0: not pregnant. 1: eggs growing. 2: eggs ready to be laid. 3: eggs laying.
     */
    public byte getPregnancyStatus(){
        return this.entityData.get(PREGNANT);
    }

    /**
     * Return the size of the owned mini spiders list.
     * @return int of the amount of mini spiders this entity owns.
     */
    public int getMiniSpidersCount(){
        return this.miniSpiders.size();
    }

    /**
     * A helper method that is called mainly from the mini spiders class to register themselves as owned by this mother.
     * @param id The UUID of the mini spider to be owned by this.
     */
    public void addMiniSpider(UUID id){
        this.miniSpiders.add(id);
    }

    /**
     * A helper method that is called from the mini spiders class to remove them from the mini spiders list in case they
     * die or get discarded.
     * @param id The UUID of the mini spider that is getting removed.
     */
    public void removeMiniSpider(UUID id){
        this.miniSpiders.remove(id);
    }

    /**
     * Gets called when an entity changes dimension by travelling through an end portal or a nether portal.
     * @param serverLevel The new dimension level being travelled to.
     * @param teleporter A forge interface for handling entity teleportation.
     * @return The new entity with the new position in the new dimension
     */
    @Nullable
    @Override
    public Entity changeDimension(@NotNull ServerLevel serverLevel, @NotNull ITeleporter teleporter) {
        Entity entity = super.changeDimension(serverLevel, teleporter);
        if(entity instanceof MotherSpiderEntity mother) mother.revalidateMiniSpiders();
        return entity;
    }

    /**
     * Should be used as an absolut last resort
     */
    public void revalidateMiniSpiders(){
        if (this.level instanceof ServerLevel serverLevel) {
            List<UUID> newList = new ArrayList<>();
            for (UUID i : this.miniSpiders) {
                Entity miniSpider = serverLevel.getEntity(i);
                if (miniSpider instanceof MiniSpiderEntity) {
                    newList.add(i);
                }
            }
            if (!this.miniSpiders.equals(newList)) {
                this.miniSpiders.clear();
                this.miniSpiders.addAll(newList);
            }
        }
    }

    /**
     * The final step in removing an entity from the current loaded entities in the world.
     * @param removalReason An enum of reasons for the removal which has 2 values about saving it for later reloading
     *                      or permanently destroying the entity.
     */
    @Override
    public void remove(@NotNull RemovalReason removalReason) {
        super.remove(removalReason);
        if (!this.level.isClientSide && removalReason.shouldDestroy()) {
            for (UUID id : this.miniSpiders) {
                if(((ServerLevel)this.level).getEntity(id) instanceof MiniSpiderEntity spider){
                    spider.setOwnerID(null);
                }
            }
        }
    }

    /**
     * Reads the NBT tag and gets any pieces of saved data and applies it to the entity.
     * @param NBTTag The NBT tag that holds the saved data.
     */
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag NBTTag) {
        super.readAdditionalSaveData(NBTTag);
        if(NBTTag.contains("mobultion:pregnant", Tag.TAG_BYTE)){
            setPregnant(NBTTag.getByte("mobultion:pregnant"));
        }
        if(NBTTag.contains("mobultion:minispidercount", Tag.TAG_INT)){
            this.miniSpiders.clear();
            for(int i = 0; i < NBTTag.getInt("mobultion:minispidercount"); i++){
                this.miniSpiders.add(NBTTag.getUUID("mobultion:minispiders"+i));
            }
        }
    }

    /**
     * Writing extra pieces of data to the NBT tag which is persisted
     * @param NBTTag The tag where the additional data will be written to.
     */
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag NBTTag) {
        super.addAdditionalSaveData(NBTTag);
        NBTTag.putByte("mobultion:pregnant", getPregnancyStatus());
        NBTTag.putInt("mobultion:minispidercount", this.miniSpiders.size());
        for (int i = 0; i < this.miniSpiders.size(); i++){
            NBTTag.putUUID("mobultion:minispiders"+i, this.miniSpiders.get(i));
        }
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
