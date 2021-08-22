package dev.mcpecommander.mobultion.entities.skeletons.entities;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;

/* McpeCommander created on 23/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public class HeartArrowEntity extends AbstractArrowEntity implements IAnimatable {

    /**
     * The animation factory, for more information check GeckoLib.
     */
    private final AnimationFactory factory = new AnimationFactory(this);

    public HeartArrowEntity(EntityType<HeartArrowEntity> type, World world) {
        super(type, world);
    }

    /**
     * What kind of item you get when the player intersects the arrow entity.
     * @return item stack instance of the item to be picked up.
     */
    @Nonnull
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Registration.HEARTARROW_ITEM.get());
    }

    /**
     * Specific to arrows and gets called after an entity gets hit by this arrow and the hit registers. So if the
     * player blocked the arrow, this doesn't get called nor for invincible entities either.
     * @param attackedEntity The entity who got hit by this projectile.
     */
    protected void doPostHurtEffects(@Nonnull LivingEntity attackedEntity) {
        super.doPostHurtEffects(attackedEntity);
        EffectInstance effectinstance = new EffectInstance(Registration.JOKERNESS_EFFECT.get(), 10 * 20, 0);
        attackedEntity.addEffect(effectinstance);
    }

    /**
     * Register the animation controller here and any other particle/sound listeners.
     * @param data: Animation data that adds animation controllers.
     */
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, event -> {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("screw", true));
            return PlayState.CONTINUE;
        }));
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
     * The spawning packet that is sent to client side to make it tick and render on the client side.
     * DO NOT USE the vanilla spawning packet because it doesn't work.
     * @return The spawning packet to be sent to the client.
     */
    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
