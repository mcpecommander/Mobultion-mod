package mcpecommander.mobultion.particle;

import mcpecommander.mobultion.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureStitcherBreathFX
{
  @SubscribeEvent
  public void stitcherEventPre(TextureStitchEvent event) {
    ResourceLocation confuse = new ResourceLocation(Reference.MOD_ID ,"entity/confuse_particle");
    event.getMap().registerSprite(confuse);
    ResourceLocation lava = new ResourceLocation(Reference.MOD_ID ,"entity/lava_particle");
    event.getMap().registerSprite(lava);
  }
}