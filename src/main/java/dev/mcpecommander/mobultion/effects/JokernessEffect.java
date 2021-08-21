package dev.mcpecommander.mobultion.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/* McpeCommander created on 18/07/2021 inside the package - dev.mcpecommander.mobultion.effects */
public class JokernessEffect extends Effect {

    Random random;
    public Set<PlayingCard> effectFixers;

    public JokernessEffect() {
        super(EffectType.HARMFUL, 0xffffff);
        this.random = new Random();
        effectFixers = new HashSet<>();
    }

    /**
     * Gets called once to get the color of this effect for the particle display.
     * @return a color in int format.
     */
    @Override
    public int getColor() {
        return randomColor(random).getRGB();
    }

    /**
     * Gets called to check if the applyEffectTick method should be called or not.
     * @param duration The duration remaining in ticks.
     * @param amplifier The level of the effect where 0 is level 1 in-game.
     * @return true if the applyEffectTick method should called.
     */
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 2 == 0;
    }

    /**
     * Gets called every time the previous method returns true up to once per tick both on the server and client sides.
     * @param affectedEntity The entity being affected by this effect.
     * @param amplifier The level of the effect. 0 being the lowest and representing 1 in-game.
     */
    @Override
    public void applyEffectTick(LivingEntity affectedEntity, int amplifier) {
        if(!affectedEntity.level.isClientSide) return;
        int amount = effectFixers.size();
        for(int i = 0; i < 20 - amount; i++){
            effectFixers.add(new PlayingCard(random.nextInt(Minecraft.getInstance().getWindow().getGuiScaledWidth() - 32),
                    random.nextInt(Minecraft.getInstance().getWindow().getGuiScaledHeight() - 32), randomColor(random),
                    0.25f * random.nextInt(4)));
        }
    }

    /**
     * Lazy method to get a random color
     * @param random The randomizer since the effect doesn't have a static random instance.
     * @return A random color with 50% transparency.
     */
    private static Color randomColor(Random random){
        return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), Math.max(random.nextFloat(), 0.5f));
    }
}
