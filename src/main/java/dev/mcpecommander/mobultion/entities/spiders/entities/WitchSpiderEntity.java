package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.entities.spiders.entityGoals.MobultionSpiderRangedGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.List;

/* Created by McpeCommander on 2021/06/18 */
public class WitchSpiderEntity extends MobultionSpiderEntity{

    private final List<BlockPos> attackPositions = new ArrayList<>();

    /**
     * A data parameter to help keep the target in sync and to be saved (in another method) when quiting the game.
     */
    private static final EntityDataAccessor<Byte> ATTACK_MODE = SynchedEntityData.defineId(WitchSpiderEntity.class, EntityDataSerializers.BYTE);

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public WitchSpiderEntity(EntityType<WitchSpiderEntity> mob, Level world) {
        super(mob, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_MODE, (byte)0);
    }

    public void setAttackMode(byte mode){
        this.entityData.set(ATTACK_MODE, mode);
    }

    public byte getAttackMode(){
        return this.entityData.get(ATTACK_MODE);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MobultionSpiderRangedGoal(this, 1.1, 50, 12));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 2D);
    }

    @Override
    public @NotNull ItemStack getItemInHand(@NotNull InteractionHand hand) {
        return ItemStack.EMPTY;
    }

    @Override
    public void startUsingItem(@NotNull InteractionHand hand) {
        if (!this.isUsingItem()) {
            this.useItemRemaining = 22;
            if (!this.level.isClientSide) {
                this.setLivingEntityFlag(1, true);
                this.setLivingEntityFlag(2, hand == InteractionHand.OFF_HAND);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target) {
        target.isVisuallyCrawling();
        switch (4/*this.level.random.nextInt(4) + 1*/) {
            case 1 -> {
                this.setAttackMode((byte)1);
                this.level.setBlock(target.blockPosition(), Blocks.SANDSTONE.defaultBlockState(), Block.UPDATE_ALL);
                this.level.setBlock(target.blockPosition().above(), Blocks.SANDSTONE.defaultBlockState(), Block.UPDATE_ALL);
                this.clearAttackPositions();
                this.attackPositions.clear();
                this.attackPositions.add(target.blockPosition());
                this.attackPositions.add(target.blockPosition().above());
            }
            case 2 -> {
                this.setAttackMode((byte)2);
                this.level.setBlock(target.blockPosition(), Blocks.POWDER_SNOW.defaultBlockState(), Block.UPDATE_ALL);
                this.level.setBlock(target.blockPosition().above(), Blocks.POWDER_SNOW.defaultBlockState(), Block.UPDATE_ALL);
                this.clearAttackPositions();
                this.attackPositions.clear();
                this.attackPositions.add(target.blockPosition());
                this.attackPositions.add(target.blockPosition().above());
            }
            case 3 -> {
                this.setAttackMode((byte)3);
                this.level.setBlock(target.blockPosition(), BaseFireBlock.getState(level, target.blockPosition()), Block.UPDATE_ALL);
                this.clearAttackPositions();
                this.attackPositions.clear();
                this.attackPositions.add(target.blockPosition());
            }
            case 4 -> {
                this.setAttackMode((byte)4);
                //this.level.setBlock(target.blockPosition().above(2), Blocks.DAMAGED_ANVIL.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }

    private void clearAttackPositions() {
        for(BlockPos pos : this.attackPositions){
            if(this.attackPositions.size() == 2){
                if(this.level.getBlockState(pos).is(Blocks.POWDER_SNOW) ||
                        this.level.getBlockState(pos).is(Blocks.SANDSTONE)){
                    this.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
            }else{
                if(this.level.getBlockState(pos).is(Blocks.FIRE)){
                    this.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
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
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", false));
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
