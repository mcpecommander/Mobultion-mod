package mcpecommander.mobultion.events;

import java.util.Random;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModPotions;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Side.CLIENT)
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
