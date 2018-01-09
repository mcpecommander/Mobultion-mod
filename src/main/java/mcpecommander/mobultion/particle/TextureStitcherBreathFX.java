package mcpecommander.mobultion.particle;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureStitcherBreathFX
{
  @SubscribeEvent
  public void stitcherEventPre(TextureStitchEvent event) {
    ResourceLocation confuse = new ResourceLocation("mmm:entity/confuse_particle");
    event.getMap().registerSprite(confuse);
    ResourceLocation lava = new ResourceLocation("mmm:entity/lava_particle");
    event.getMap().registerSprite(lava);
  }
}