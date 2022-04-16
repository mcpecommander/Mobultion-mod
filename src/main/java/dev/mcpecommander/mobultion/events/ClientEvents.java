package dev.mcpecommander.mobultion.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Quaternion;
import dev.mcpecommander.mobultion.effects.JokernessEffect;
import dev.mcpecommander.mobultion.effects.PlayingCard;
import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 13/07/2021 inside the package - dev.mcpecommander.mobultion.client */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    //TODO: Fix this mess

    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/mob_effect/jokernessscreen.png");

    @SubscribeEvent
    public static void renderEffect(RenderGameOverlayEvent.Post event){
        if(Minecraft.getInstance().player == null) return;
        if(Minecraft.getInstance().player.hasEffect(Registration.JOKERNESS_EFFECT.get()) &&
                event.getType() == RenderGameOverlayEvent.ElementType.ALL){
            JokernessEffect effect = (JokernessEffect) Minecraft.getInstance().player
                    .getEffect(Registration.JOKERNESS_EFFECT.get()).getEffect();
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

    @SubscribeEvent
    public static void renderEntity(RenderLivingEvent.Post<Wolf, WolfModel<Wolf>> event){
        if(event.getEntity() instanceof Wolf &&
                event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() == Registration.HALO.get()){
            PoseStack stack = event.getPoseStack();
            stack.pushPose();
            stack.mulPose(new Quaternion(0,
                    180 + Mth.rotLerp(event.getPartialTick(), -event.getEntity().yBodyRotO,
                            -event.getEntity().yBodyRot), 0, true));
            stack.translate(-0.5, 0.5, -1);
            RenderProperties.get(Registration.HALO.get()).getItemStackRenderer()
                    .renderByItem(event.getEntity().getItemBySlot(EquipmentSlot.HEAD),
                    ItemTransforms.TransformType.HEAD, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), 0);
            stack.popPose();
        }
    }

    private static void renderTextured(float x, float y, float uv, Color color){
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(x, y + 16, -90.0D).uv(0, uv + 0.25f).endVertex();
        bufferbuilder.vertex(x + 16, y + 16, -90.0D).uv(1, uv + 0.25f).endVertex();
        bufferbuilder.vertex(x + 16, y, -90.0D).uv(1, uv).endVertex();
        bufferbuilder.vertex(x, y, -90.0D).uv(0, uv).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }


}
