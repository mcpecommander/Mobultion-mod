package dev.mcpecommander.mobultion.effects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import javax.annotation.Nonnull;
import java.util.Random;

/* McpeCommander created on 16/08/2021 inside the package - dev.mcpecommander.mobultion.effects */
public class CorruptionEffect extends MobEffect {

    /**
     * Used to randomise the breaking effect instead of a consistent breaking event.
     */
    Random random;

    public CorruptionEffect(){
        super(MobEffectCategory.HARMFUL, 0x927006);
        this.random = new Random();
    }

    /**
     * Gets called to check if the applyEffectTick method should be called or not.
     * @param duration The duration remaining in ticks.
     * @param amplifier The level of the effect where 0 is level 1 in-game.
     * @return true if the applyEffectTick method should called.
     */
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 30 == 0 && random.nextInt(11) <= amplifier;
    }

    /**
     * Gets called every time the previous method returns true up to once per tick both on the server and client sides.
     * @param affectedEntity The entity being affected by this effect.
     * @param amplifier The level of the effect. 0 being the lowest and representing 1 in-game.
     */
    @Override
    public void applyEffectTick(@Nonnull LivingEntity affectedEntity, int amplifier) {
        if(!affectedEntity.level.isClientSide && affectedEntity instanceof Player
                && !((Player) affectedEntity).getAbilities().instabuild) {
            ((Player) affectedEntity).getInventory().items.forEach(itemStack -> {
                if(!itemStack.isEmpty() && random.nextFloat() + (((float)amplifier) / 10) > 0.8f){
                    itemStack.hurt(1 + amplifier, random, (ServerPlayer) affectedEntity);
                }
            });
        }
    }
}
