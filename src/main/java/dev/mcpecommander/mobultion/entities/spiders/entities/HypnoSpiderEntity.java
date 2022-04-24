package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.entities.spiders.entityGoals.MobultionSpiderRangedGoal;
import net.minecraft.core.particles.ParticleTypes;
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
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;

/* Created by McpeCommander on 2021/06/18 */
public class HypnoSpiderEntity extends MobultionSpiderEntity{

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public HypnoSpiderEntity(EntityType<HypnoSpiderEntity> mob, Level world) {
        super(mob, world);
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MobultionSpiderRangedGoal(this, 1.1, 20, 12));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
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
        } else if(event.isMoving()){
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
        AnimationController controller = factory.getOrCreateAnimationData(this.getUUID().hashCode()).getAnimationControllers()
                .get("controller");
        //System.out.println(controller.getCurrentAnimation());
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
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicateController));
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
     * Ticks on both sides when isDeadOrDying() is true.
     */
    @Override
    protected void tickDeath() {
        if(this.deathTime++ == getMaxDeathTick()){
            this.discard();
            for(int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level.addParticle(ParticleTypes.POOF,
                        this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)
                        , d0, d1, d2);
            }
        }
    }

    //A hacky way to make sure the item use tick passes instead of having to sync a one-off variable to the client.
    /**
     * Gets the item this entity is holding based on which hand is supplied.
     * @param hand The hand slot for which the entity is holding an item in.
     * @return An item stack of the item being held in the supplied hand.
     */
    @Override
    public @NotNull ItemStack getItemInHand(@NotNull InteractionHand hand) {
        return ItemStack.EMPTY;
    }

    /**
     * When this is called, the duration of the item in supplied hand is calculated, then the state is synced to the
     * client for the hand-using animation to be run.
     * @param hand The hand in which the item is going to be used.
     */
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

    //Usually an interface method for the ranged mobs but I want to skip having more interfaces.
    /**
     * Gets called from the AI attack goal, so it is only on the server.
     * @param target The living entity that this entity is targeting with whatever is in the method.
     */
    @Override
    public void performRangedAttack(LivingEntity target) {
        HypnoWaveEntity wave = new HypnoWaveEntity(this);
        double xVel = target.getX() - this.getX();
        double yVel = target.getY(2d/3d) - wave.getY();
        double zVel = target.getZ() - this.getZ();
        //1F is the vector scaling factor which in turn translates into speed.
        //The last parameter is the error scale. 0 = exact shot.
        wave.shoot(xVel, yVel, zVel, 1F, 0);
        this.level.addFreshEntity(wave);
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
     * Weather this mob can be affected by the potion effect in the params.
     * @param effect: the potion effect instance.
     * @return true if the mob can be affected.
     */
    @Override
    public boolean canBeAffected(@Nonnull MobEffectInstance effect) {
        return true;
    }
}
