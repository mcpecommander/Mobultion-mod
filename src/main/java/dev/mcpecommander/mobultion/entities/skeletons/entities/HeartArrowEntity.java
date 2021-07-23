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
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/* McpeCommander created on 23/07/2021 inside the package - dev.mcpecommander.mobultion.entities.skeletons.entities */
public class HeartArrowEntity extends AbstractArrowEntity implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public HeartArrowEntity(EntityType<HeartArrowEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Registration.HEARTARROW_ITEM.get());
    }

    protected void doPostHurtEffects(LivingEntity attackedEntity) {
        super.doPostHurtEffects(attackedEntity);
        EffectInstance effectinstance = new EffectInstance(Registration.JOKERNESS_EFFECT.get(), 10 * 20, 0);
        attackedEntity.addEffect(effectinstance);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    /**
     * The spawning packet that is send to client side to make it tick and render on the client side.
     * DO NOT USE the vanilla spawning packet because it doesn't work.
     * @return The spawning packet to be sent to the client.
     */
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
