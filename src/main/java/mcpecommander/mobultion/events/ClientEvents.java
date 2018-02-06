package mcpecommander.mobultion.events;

import java.util.Random;

import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
import com.leviathanstudio.craftstudio.common.animation.IAnimated;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.model.ModelCraftStudioSon;
import mcpecommander.mobultion.init.ModPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ClientEvents {

	@SubscribeEvent
	public static void onOverlayRender(RenderGameOverlayEvent.Post e){	
		if(ModPotions.potionJokerness.isReady && e.getType() == ElementType.ALL){
			ModPotions.potionJokerness.render(Minecraft.getMinecraft(), e.getResolution(), new Random());
		}
		if(ModPotions.potionVomit.isReady && !Minecraft.getMinecraft().isGamePaused() && e.getType() == ElementType.PORTAL && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0){
			ModPotions.potionVomit.renderEffect(e, ModPotions.potionVomit.mul);
		}
		if(ModPotions.potionFreeze.isReady && !Minecraft.getMinecraft().isGamePaused() && e.getType() == ElementType.PORTAL && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0){
			ModPotions.potionFreeze.renderEffect(e, ModPotions.potionFreeze.mul);
		}
	}	
	
}
