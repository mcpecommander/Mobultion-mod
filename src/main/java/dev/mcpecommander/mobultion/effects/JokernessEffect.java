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

    @Override
    public int getColor() {
        return randomColor(random).getRGB();
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 2 == 0;
    }

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

    private static Color randomColor(Random random){
        return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), Math.max(random.nextFloat(), 0.5f));
    }
}
