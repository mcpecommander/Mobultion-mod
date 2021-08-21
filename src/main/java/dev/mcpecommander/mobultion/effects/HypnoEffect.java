package dev.mcpecommander.mobultion.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.Random;

/* McpeCommander created on 08/08/2021 inside the package - dev.mcpecommander.mobultion.effects */
public class HypnoEffect extends Effect {
    /**
     * Used to randomise the breaking effect instead of a consistent breaking event.
     */
    Random random;

    public HypnoEffect(){
        super(EffectType.HARMFUL, 0x7E0476);
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
        return duration % 4 == 0 && random.nextInt(11) <= amplifier;
    }

    /**
     * Gets called every time the previous method returns true up to once per tick both on the server and client sides.
     * @param affectedEntity The entity being affected by this effect.
     * @param amplifier The level of the effect. 0 being the lowest and representing 1 in-game.
     */
    @Override
    public void applyEffectTick(LivingEntity affectedEntity, int amplifier) {
        if(!affectedEntity.level.isClientSide) return;
        affectedEntity.setDeltaMovement((random.nextDouble() - 0.5d) * (((float)amplifier)/10 + 1) ,
                0, (random.nextDouble() - 0.5d) * (((float)amplifier)/10 + 1));
    }

}
