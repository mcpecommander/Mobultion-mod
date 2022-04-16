package dev.mcpecommander.mobultion.entities.zombies.entities;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* McpeCommander created on 30/07/2021 inside the package - dev.mcpecommander.mobultion.entities.zombies.entities */
public class DoctorZombieEntity extends MobultionZombieEntity{

    private final AnimationFactory factory = new AnimationFactory(this);

    public DoctorZombieEntity(EntityType<? extends MobultionZombieEntity> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.75D);
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(@Nonnull DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Registration.HEALTHPACK.get()));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason,
                                           @Nullable SpawnGroupData livingEntityData, @Nullable CompoundTag NBTTag) {
        this.setCanPickUpLoot(false);
        this.populateDefaultEquipmentSlots(difficulty);

        return super.finalizeSpawn(world, difficulty, spawnReason, livingEntityData, NBTTag);
    }

    @Override
    int getMaxDeathCount() {
        return 46;
    }

    @Override
    void deathParticles() {
        //Do not spawn too many particles.
        if (this.deathTime % 4 == 0) {
            for(double i = 0; i <= Math.PI*2; i += Math.PI/18d) {
                this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                        //Math.cos(i) for the initial circle position on the x axis.
                        //* 0.5d to make the circle half as wide.
                        //(random.nextGaussian() * 0.01d - 0.005d) adds a small -0.01 - 0.01 variation to the diameter.
                        this.getX() + Mth.cos((float) i) * 0.5d + (random.nextGaussian() * 0.01d - 0.005d),
                        //this.blockPosition().getY() to make sure the particles spawn at ground level.
                        //(MathHelper.floor(this.getY()) would work too).
                        //+ random.nextGaussian() * 0.02f adds a small height variation.
                        this.blockPosition().getY() + random.nextGaussian() * 0.02f,
                        //Math.sin(i) for the initial circle position on the y axis.
                        //* 0.5d to make the circle half as wide.
                        //(random.nextGaussian() * 0.01d - 0.005d) adds a small -0.01 - 0.01 variation to the diameter.
                        this.getZ() + Mth.sin((float) i) * 0.5d + (random.nextGaussian() * 0.01d - 0.005d),
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
            //TODO: rework the speed stuff.
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
        return this.factory;
    }
}
