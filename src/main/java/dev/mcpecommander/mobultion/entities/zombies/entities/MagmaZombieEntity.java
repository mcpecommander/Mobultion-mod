package dev.mcpecommander.mobultion.entities.zombies.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;

/* McpeCommander created on 29/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.entities */
public class MagmaZombieEntity extends MobultionZombieEntity{

    private final AnimationFactory factory = new AnimationFactory(this);

    public MagmaZombieEntity(EntityType<? extends MobultionZombieEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.75D);
    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity target) {
        boolean flag = super.doHurtTarget(target);
        if (flag) {
            target.setSecondsOnFire(10);
        }
        return flag;
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    @Override
    void deathParticles() {
        //Do not spawn too many particles.
        if (this.deathTime % 4 == 0) {
            for(double i = 0; i <= Math.PI*2; i += Math.PI/18d) {
                this.level.addParticle(ParticleTypes.FLAME,
                        //Math.cos(i) for the initial circle position on the x axis.
                        //* 0.5d to make the circle half as wide.
                        //(random.nextGaussian() * 0.01d - 0.005d) adds a small -0.01 - 0.01 variation to the diameter.
                        this.getX() + MathHelper.cos((float) i) * 0.5d + (random.nextGaussian() * 0.01d - 0.005d),
                        //this.blockPosition().getY() to make sure the particles spawn at ground level.
                        //(MathHelper.floor(this.getY()) would work too).
                        //+ random.nextGaussian() * 0.02f adds a small height variation.
                        this.blockPosition().getY() + random.nextGaussian() * 0.02f,
                        //Math.sin(i) for the initial circle position on the y axis.
                        //* 0.5d to make the circle half as wide.
                        //(random.nextGaussian() * 0.01d - 0.005d) adds a small -0.01 - 0.01 variation to the diameter.
                        this.getZ() + MathHelper.sin((float) i) * 0.5d + (random.nextGaussian() * 0.01d - 0.005d),
                        //Small random speeds on the x and z axis to make it feel alive.
                        this.random.nextGaussian() * 0.002d - 0.001d,
                        //Constant upwards speed.
                        0.02d,
                        this.random.nextGaussian() * 0.002d - 0.001d);
            }
        }
    }

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

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
