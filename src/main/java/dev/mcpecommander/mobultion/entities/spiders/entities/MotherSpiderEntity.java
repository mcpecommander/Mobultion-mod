package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.entities.spiders.entityGoals.MotherSpiderEggLayingGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
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

    @Override
    public void tick() {
        //System.out.println(this.getId());
        super.tick();
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

    @Override
    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        super.actuallyHurt(p_21240_, p_21241_);
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        return super.hurt(p_21016_, p_21017_);
    }

    @Override
    public void knockback(double p_147241_, double p_147242_, double p_147243_) {
        if(this.isDeadOrDying()) return;
        super.knockback(p_147241_, p_147242_, p_147243_);
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

    @Override
    protected int getMaxDeathTick() {
        return 30;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PREGNANT, (byte)0);
    }

    public void setPregnant(byte pregnancyStatus){
        this.entityData.set(PREGNANT, pregnancyStatus);
    }

    public byte getPregnancyStatus(){
        return this.entityData.get(PREGNANT);
    }

    public int getMiniSpidersCount(){
        return this.miniSpiders.size();
    }

    public void addMiniSpider(UUID id){
        this.miniSpiders.add(id);
    }

    public void removeMiniSpider(UUID id){
        this.miniSpiders.remove(id);
    }

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
