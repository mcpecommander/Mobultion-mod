package mcpecommander.mobultion.events;

import java.util.Random;

import mcpecommander.mobultion.init.ModPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class ClientEvents {

	@SubscribeEvent
	public static void onOverlayRender(RenderGameOverlayEvent.Post e){	
		if(ModPotions.potionJokerness.isReady && e.getType() == ElementType.ALL){
			ModPotions.potionJokerness.render(Minecraft.getMinecraft(), e.getResolution(), new Random());
		}
	}
	
	//@SubscribeEvent
	public static void onLivingRender(RenderLivingEvent.Post<EntityLivingBase> e){
		
	}
	
	
}
