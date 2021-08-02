package dev.mcpecommander.mobultion.entities.spiders.entities;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/* McpeCommander created on 18/06/2021 inside the package - dev.mcpecommander.mobultion.entities.spiders.entities */
public class WitherSpiderEntity extends MobultionSpiderEntity{

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);
    float prevHealth;
    int timer = -1;
    boolean isDroppingHead1, isDroppingHead2 = false;

    public WitherSpiderEntity(EntityType<WitherSpiderEntity> mob, World world) {
        super(mob, world);
        prevHealth = this.getMaxHealth();
    }

    /**
     * Register the AI/goals here. Server side only.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();

    }

    /**
     * Gets called in the main class to init the attributes.
     * @see dev.mcpecommander.mobultion.Mobultion
     * @return AttributeModifierMap.MutableAttribute
     */
    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 60.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    /**
     * The predicate for animation controller
     * @param event: The animation event that includes the bone animations and animation status
     * @return PlayState.CONTINUE or PlayState.STOP depending on which needed.
     */
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        if (prevHealth != this.getHealth()){
            if (prevHealth > 2f/3f * this.getMaxHealth() &&
                    this.getHealth() <= 2f/3f * this.getMaxHealth() &&
                    this.getHealth() > 1f/3f * this.getMaxHealth()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("head1_drop", false));

                prevHealth = this.getHealth();
                return PlayState.CONTINUE;
            }
            if (prevHealth > 1f/3f * this.getMaxHealth() &&
                    this.getHealth() <= 1f/3f * this.getMaxHealth()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("head2_drop", false));

                prevHealth = this.getHealth();
                return PlayState.CONTINUE;
            }
        }
        prevHealth = this.getHealth();

        if(this.isDeadOrDying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
            return PlayState.CONTINUE;
        }
        if(event.isMoving()){
            if(event.getController().getCurrentAnimation() == null){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
                return PlayState.CONTINUE;
            }
        }else{
            if(event.getController().getCurrentAnimation() == null ||
                    !event.getController().getCurrentAnimation().animationName.equals("move")){
                return PlayState.CONTINUE;
            }else{
                return PlayState.STOP;
            }
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
     * Runs on the server only and does the actual damage calculations and lowers the entity's health.
     * @param damageSource: Type of damage the entity received.
     * @param amount: the amount of damage the entity received.
     */
    @Override
    protected void actuallyHurt(DamageSource damageSource, float amount) {
        if(isDroppingHead1 ||isDroppingHead2) return;
        float prev = this.getHealth();
        super.actuallyHurt(damageSource, MathHelper.clamp(amount, 0, 1f/3f * this.getMaxHealth()));
        if(prev < 1f/3f * this.getMaxHealth() || this.isDeadOrDying()) return;
        if(prev > 2f/3f * this.getMaxHealth()
                && this.getHealth() < 2f/3f * this.getMaxHealth()
                && this.getHealth() > 1f/3f * this.getMaxHealth()){
            timer = 15;
            isDroppingHead1 = true;
        }
        if(prev > 1f/3f * this.getMaxHealth()
                && this.getHealth() <= 1f/3f * this.getMaxHealth()){
            timer = 15;
            isDroppingHead2 = true;
        }
    }

    @Override
    protected int getMaxDeathTick() {
        return 35;
    }

    /**
     * The main update method which ticks on both sides all the time until the entity is removed.
     * Ticks on the server side only when the entity is not rendered.
     */
    @Override
    public void tick() {
        super.tick();
        if(!this.level.isClientSide && (isDroppingHead1 || isDroppingHead2)){
            if(timer == 0){
                Vector3d pos = new Vector3d(isDroppingHead1 ? 1d : -1d, 0.0d, 1d);
                pos = pos.yRot((float) Math.toRadians(-this.yBodyRot)).add(this.position());
                WitherHeadBugEntity bug = new WitherHeadBugEntity(Registration.WITHERHEADBUG.get(), this.level);
                bug.setPos(pos.x, pos.y, pos.z);
                this.level.addFreshEntity(bug);
                isDroppingHead1 = false;
                isDroppingHead2 = false;
            }
            timer --;
        }
    }

    /**
     * Removes the entity from the level. Gets called when the death timer has reached 20 or if the entity is despawned.
     */
    @Override
    public void remove() {
        super.remove();
        if(!this.level.isClientSide) {
            AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
            areaeffectcloudentity.setOwner(this);
            areaeffectcloudentity.setRadius(3.0F);
            areaeffectcloudentity.setRadiusOnUse(-0.5F);
            areaeffectcloudentity.setWaitTime(10);
            areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());
            areaeffectcloudentity.addEffect(new EffectInstance(Effects.WITHER, 160));
            areaeffectcloudentity.setPotion(Potions.EMPTY);

            this.level.addFreshEntity(areaeffectcloudentity);
        }
    }

    /**
     * Weather this mob can be affected by the potion effect in the params.
     * @param effectInstance: the potion effect instance.
     * @return true if the mob can be affected.
     */
    @Override
    public boolean canBeAffected(EffectInstance effectInstance) {
        Effect effect = effectInstance.getEffect();
        return effect != Effects.REGENERATION && effect != Effects.POISON && effect != Effects.WITHER;
    }
}
