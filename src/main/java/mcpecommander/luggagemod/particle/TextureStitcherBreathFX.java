package mcpecommander.luggagemod.particle;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureStitcherBreathFX
{
  @SubscribeEvent
  public void stitcherEventPre(TextureStitchEvent event) {
    ResourceLocation confuse = new ResourceLocation("mlm:entity/confuse_particle");
    event.getMap().registerSprite(confuse);
    ResourceLocation lava = new ResourceLocation("mlm:entity/lava_particle");
    event.getMap().registerSprite(lava);
    ResourceLocation effect = new ResourceLocation("mlm:entity/effect");
    event.getMap().registerSprite(effect);
    ResourceLocation effect2 = new ResourceLocation("mlm:entity/effect2");
    event.getMap().registerSprite(effect2);
  }
}