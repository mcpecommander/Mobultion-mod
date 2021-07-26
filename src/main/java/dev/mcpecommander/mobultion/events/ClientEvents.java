package dev.mcpecommander.mobultion.events;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.mcpecommander.mobultion.effects.JokernessEffect;
import dev.mcpecommander.mobultion.effects.PlayingCard;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 13/07/2021 inside the package - dev.mcpecommander.mobultion.client */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/mob_effect/jokernessscreen.png");

    @SubscribeEvent
    public static void renderDebug(RenderGameOverlayEvent.Post event){
        if(Minecraft.getInstance().player == null) return;
        if(Minecraft.getInstance().player.hasEffect(Registration.JOKERNESS_EFFECT.get()) &&
                event.getType() == RenderGameOverlayEvent.ElementType.ALL){
            JokernessEffect effect = (JokernessEffect) Minecraft.getInstance().player.getEffect(Registration.JOKERNESS_EFFECT.get()).getEffect();
            Set<PlayingCard> copy = new HashSet<>(effect.effectFixers);
            effect.effectFixers.forEach(playingCard -> {
                renderTextured(playingCard.getPosX(), playingCard.getPosY(), playingCard.getUv(), playingCard.getColor());
                if(!Minecraft.getInstance().isPaused()){
                    playingCard.setColor(new Color(playingCard.getColor().getRed(),
                            playingCard.getColor().getGreen(), playingCard.getColor().getBlue(),
                            Math.max(playingCard.getColor().getAlpha() - 1, 0)));
                    if(playingCard.getColor().getAlpha() == 0) copy.remove(playingCard);
                }
            });
            effect.effectFixers = copy;

        }
    }

    private static void renderTextured(float x, float y, float uv, Color color){
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
        Minecraft.getInstance().getTextureManager().bind(TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(x, y + 16, -90.0D).uv(0, uv + 0.25f).endVertex();
        bufferbuilder.vertex(x + 16, y + 16, -90.0D).uv(1, uv + 0.25f).endVertex();
        bufferbuilder.vertex(x + 16, y, -90.0D).uv(1, uv).endVertex();
        bufferbuilder.vertex(x, y, -90.0D).uv(0, uv).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }


}