package mcpecommander.mobultion.proxy;

import mcpecommander.mobultion.events.SpawnEvent;
import mcpecommander.mobultion.init.ModPotions;
import mcpecommander.mobultion.init.ModSounds;
import mcpecommander.mobultion.items.ItemFireSword;
import mcpecommander.mobultion.items.ItemForestBow;
import mcpecommander.mobultion.items.ItemHammer;
import mcpecommander.mobultion.items.ItemHealingWand;
import mcpecommander.mobultion.items.ItemHealth;
import mcpecommander.mobultion.items.ItemHeartArrow;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.reflect.internal.Trees.New;

public class CommonProxy {

	public CommonProxy() {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.TERRAIN_GEN_BUS.register(new SpawnEvent());
	}

	public void preInit(FMLPreInitializationEvent e) {
		ModPotions.init();
		// ModEntities.init();

	}

	public void init(FMLInitializationEvent e) {

	}

	public void postInit(FMLPostInitializationEvent e) {
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(new ItemForestBow(), new ItemHealingWand(), new ItemHeartArrow(),
				new ItemHammer(), new ItemFireSword(), new ItemHealth());
	}

	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry().register(ModPotions.potionHypnotize);
		event.getRegistry().register(ModPotions.potionFreeze);
		event.getRegistry().register(ModPotions.potionJokerness);
	}

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(ModSounds.fire_arrow_shot);
		event.getRegistry().register(ModSounds.entity_respawn);
		event.getRegistry().register(ModSounds.magma_remains_death);
		event.getRegistry().register(ModSounds.forest_skeleton_shoot);
		event.getRegistry().register(ModSounds.joker_ambient);
	}

}
