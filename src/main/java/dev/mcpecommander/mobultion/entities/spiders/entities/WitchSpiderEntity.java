package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.entities.spiders.entityGoals.WitchSpiderAttackGoal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/* Created by McpeCommander on 2021/06/18 */
public class WitchSpiderEntity extends MobultionSpiderEntity{

    private Vec3 attackPosition;
    public static final float ATTACK_1 = 80;
    public static final float ATTACK_2 = 15;
    public static final float ATTACK_3 = 10;
    public static final float ATTACK_4 = 40;
    /**
     * A data parameter to help keep the target in sync and to be saved (in another method) when quiting the game.
     */
    private static final EntityDataAccessor<Byte> ATTACK_MODE = SynchedEntityData.defineId(WitchSpiderEntity.class, EntityDataSerializers.BYTE);

    /**
     * A data parameter to sync the target entity to client. It doesn't need saving.
     */
    private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(WitchSpiderEntity.class, EntityDataSerializers.INT);

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public WitchSpiderEntity(EntityType<WitchSpiderEntity> mob, Level world) {
        super(mob, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new WitchSpiderAttackGoal(this, 1.1, 50, 12));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 2D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_MODE, (byte)0);
        this.entityData.define(TARGET, -1);
    }

    public void setAttackMode(byte mode){
        this.entityData.set(ATTACK_MODE, mode);
    }

    public byte getAttackMode(){
        return this.entityData.get(ATTACK_MODE);
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return super.getTarget() != null ? super.getTarget() : (LivingEntity) this.level.getEntity(this.entityData.get(TARGET));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        this.entityData.set(TARGET, target != null ? target.getId() : -1);
    }

    @Override
    protected void updateUsingItem(ItemStack itemStack) {
        //Vanilla
        itemStack.onUseTick(this.level, this, this.getUseItemRemainingTicks());
        if (--this.useItemRemaining == 0 && !this.level.isClientSide && !itemStack.useOnRelease()) {
            this.completeUsingItem();
        }
        //Mine
        if(this.getTarget() instanceof Player player) {
            if (this.getAttackMode() == 1) {
                if (this.level.isClientSide) {
                    for (int i = 0; i < 10; i++) {
                        this.level.addParticle(ParticleTypes.SNOWFLAKE,
                                player.getX() + Mth.cos((float) (i / 10f * Math.PI * 2)),
                                player.getY() + 0.3f + (getUseItemRemainingTicks()/ATTACK_1) * 1.2f,
                                player.getZ() + Mth.sin((float) (i / 10f * Math.PI * 2)),
                                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F,
                                0.05F,
                                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F);
                    }
                } else {
                    if(this.getTarget().canFreeze()) this.getTarget().setTicksFrozen(150);
                }
            }
            if(this.getAttackMode() == 2){
                if (this.level.isClientSide) {
                    if(this.attackPosition == null || this.getUseItemRemainingTicks() == ATTACK_2-1){
                        this.attackPosition = this.position();
                    }
                    for (int i = 0; i < 4; i++) {
                        double xPos = player.getX() + Mth.cos((float) (i / 4f * Math.PI * 2)) * 5f;
                        double yPos = player.getY() + 3;
                        double zPos = player.getZ() + Mth.sin((float) (i / 4f * Math.PI * 2)) * 5f;
                        this.level.addParticle(ParticleTypes.FLAME,
                                Mth.lerp(this.getTicksUsingItem()/ATTACK_2, this.attackPosition.x, xPos),
                                Mth.lerp(this.getTicksUsingItem()/ATTACK_2, this.attackPosition.y, yPos),
                                Mth.lerp(this.getTicksUsingItem()/ATTACK_2, this.attackPosition.z, zPos),
                                0,
                                0.1F,
                                0);
                    }
                } else {
                    if(this.getTicksUsingItem() >= ATTACK_2 - 2){
                        int i = this.level.random.nextInt(4);
                        LargeFireball fireball = new LargeFireball(this.level, this,
                                -Mth.cos((float) (i / 4f * Math.PI * 2)) * 5f,
                                -1.75,
                                -Mth.sin((float) (i / 4f * Math.PI * 2)) * 5f,
                                0);
                        fireball.setPos(
                                player.getX() + Mth.cos((float) (i / 4f * Math.PI * 2)) * 5f,
                                player.getY() + 3,
                                player.getZ() + Mth.sin((float) (i / 4f * Math.PI * 2)) * 5f);
                        this.level.addFreshEntity(fireball);
                    }
                }
            }
            if(this.getAttackMode() == 3){
                if(this.level.isClientSide){
                    for(int i = 0; i < 5 && this.getTicksUsingItem() >= ATTACK_3 - 3; ++i) {
                        double xSpeed = this.random.nextGaussian() * 0.02D;
                        double ySpeed = this.random.nextGaussian() * 0.02D;
                        double zSpeed = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                                this.getRandomX(1.0D),
                                this.getRandomY() + 0.4D,
                                this.getRandomZ(1.0D), xSpeed, ySpeed, zSpeed);
                    }
                }else{
                    this.heal(0.4f * this.getMaxHealth());
                }
            }
            if(this.getAttackMode() == 4 && this.getUseItemRemainingTicks() == ATTACK_4-1 && !this.level.isClientSide){
                this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, (int)ATTACK_4, 1));
            }
        }
    }

    @Override
    public int getTicksUsingItem() {
        switch (this.getAttackMode()){
            case 1 -> {
                return this.isUsingItem() ? (int) (ATTACK_1 - this.getUseItemRemainingTicks()) : 0;
            }
            case 2 -> {
                return this.isUsingItem() ? (int) (ATTACK_2 - this.getUseItemRemainingTicks()) : 0;
            }
            case 3 ->{
                return this.isUsingItem() ? (int) (ATTACK_3 - this.getUseItemRemainingTicks()) : 0;
            }
            case 4 ->{
                return this.isUsingItem() ? (int) (ATTACK_4 - this.getUseItemRemainingTicks()) : 0;
            }
            default ->{
                return super.getTicksUsingItem();
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> dataParameter) {
        super.onSyncedDataUpdated(dataParameter);
        if(ATTACK_MODE.equals(dataParameter) && this.level.isClientSide){
            switch (this.getAttackMode()){
                case 0 ->
                    this.useItemRemaining = 0;
                case 1 ->
                    this.useItemRemaining = (int) ATTACK_1;
                case 2 ->
                    this.useItemRemaining = (int) ATTACK_2;
                case 3 ->
                    this.useItemRemaining = (int) ATTACK_3;
                case 4 ->
                    this.useItemRemaining = (int) ATTACK_4;

            }
        }

    }

    @Override
    public @NotNull ItemStack getItemInHand(@NotNull InteractionHand hand) {
        return ItemStack.EMPTY;
    }

    @Override
    public void startUsingItem(@NotNull InteractionHand hand) {
        if (!this.isUsingItem()) {
            switch (this.getAttackMode()){
                case 1 ->
                        this.useItemRemaining = (int) ATTACK_1;
                case 2 ->
                        this.useItemRemaining = (int) ATTACK_2;
                case 3 ->
                        this.useItemRemaining = (int) ATTACK_3;
                case 4 ->
                        this.useItemRemaining = (int) ATTACK_4;
                default ->
                        this.useItemRemaining = 40;
            }
            if (!this.level.isClientSide) {
                this.setLivingEntityFlag(1, true);
                this.setLivingEntityFlag(2, hand == InteractionHand.OFF_HAND);
            }
        }
    }


    /**
     * The predicate for the normal controller (movement, death etc)
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState predicateController(AnimationEvent<E> event)
    {
        if(this.isDeadOrDying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
            return PlayState.CONTINUE;
        }
        if(this.isUsingItem()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", true));
            return PlayState.CONTINUE;
        }
        if(event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    /**
     * The predicate for the idle controller that only controls Idle animation which can play at the same time as the
     * movement animation. Only one animation can be played by one controller and that is why we need two controllers
     * and two predicates for the idle animation.
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event)
    {
        if(this.isDeadOrDying()) return PlayState.STOP;
        AnimationController controller = factory.getOrCreateAnimationData(this.getUUID().hashCode())
                .getAnimationControllers().get("controller");
        if(controller.getCurrentAnimation() == null || controller.getCurrentAnimation().animationName.equals("move")){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "idle", 0, this::predicateIdle));
        AnimationController<WitchSpiderEntity> controller = new AnimationController<>(this, "controller",
                0, this::predicateController);
        controller.registerParticleListener(this::particleListener);
        data.addAnimationController(controller);
    }

    @Override
    protected int getMaxDeathTick() {
        return 30;
    }

    /**
     * The particle listener which gets called every time there is a particle effect in the current animation.
     * @param event: gives access to the locator name, particle name and script data.
     */
    private <T extends IAnimatable> void particleListener(ParticleKeyFrameEvent<T> event) {
        for(int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
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
