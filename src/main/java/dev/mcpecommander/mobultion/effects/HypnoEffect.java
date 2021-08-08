package dev.mcpecommander.mobultion.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.Random;

/* McpeCommander created on 08/08/2021 inside the package - dev.mcpecommander.mobultion.effects */
public class HypnoEffect extends Effect {

    Random random;

    public HypnoEffect(){
        super(EffectType.HARMFUL, 0x7E0476);
        this.random = new Random();
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 4 == 0 && random.nextInt(11) <= amplifier;
    }

    @Override
    public void applyEffectTick(LivingEntity affectedEntity, int amplifier) {
        if(!affectedEntity.level.isClientSide) return;
        affectedEntity.setDeltaMovement((random.nextDouble() - 0.5d) * (((float)amplifier)/10 + 1) ,
                0, (random.nextDouble() - 0.5d) * (((float)amplifier)/10 + 1));
    }

}
