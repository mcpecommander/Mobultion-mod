package dev.mcpecommander.mobultion.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.Nonnull;
import java.util.Random;

/* McpeCommander created on 16/08/2021 inside the package - dev.mcpecommander.mobultion.effects */
public class CorruptionEffect extends Effect {

    Random random;

    public CorruptionEffect(){
        super(EffectType.HARMFUL, 0x927006);
        this.random = new Random();
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 30 == 0 && random.nextInt(11) <= amplifier;
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity affectedEntity, int amplifier) {
        if(!affectedEntity.level.isClientSide && affectedEntity instanceof PlayerEntity
                && !((PlayerEntity) affectedEntity).abilities.instabuild) {
            ((PlayerEntity) affectedEntity).inventory.items.forEach(itemStack -> {
                if(!itemStack.isEmpty() && random.nextFloat() + (((float)amplifier) / 10) > 0.8f){
                    itemStack.hurt(1 + amplifier, random, (ServerPlayerEntity) affectedEntity);
                }
            });
        }
    }
}
