package mcpecommander.mobultion.proxy;

import mcpecommander.mobultion.events.SpawnEvent;
import mcpecommander.mobultion.init.ModDictionary;
import mcpecommander.mobultion.init.ModPotions;
import mcpecommander.mobultion.init.ModSounds;
import mcpecommander.mobultion.items.ItemEnderBlaze;
import mcpecommander.mobultion.items.ItemEnderFlake;
import mcpecommander.mobultion.items.ItemFireSword;
import mcpecommander.mobultion.items.ItemForestBow;
import mcpecommander.mobultion.items.ItemFork;
import mcpecommander.mobultion.items.ItemHammer;
import mcpecommander.mobultion.items.ItemHat;
import mcpecommander.mobultion.items.ItemHealingWand;
import mcpecommander.mobultion.items.ItemHealth;
import mcpecommander.mobultion.items.ItemHeartArrow;
import mcpecommander.mobultion.items.ItemKnife;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonProxy {

	public CommonProxy() {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.TERRAIN_GEN_BUS.register(new SpawnEvent());
	}

	public void preInit(FMLPreInitializationEvent e) {
		ModPotions.init();

	}

	public void init(FMLInitializationEvent e) {
		ModDictionary.registerDict();
	}

	public void postInit(FMLPostInitializationEvent e) {
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(new ItemForestBow(), new ItemHealingWand(), new ItemHeartArrow(),
				new ItemHammer(), new ItemFireSword(), new ItemHealth(), new ItemFork(), new ItemKnife(), new ItemHat(),
				new ItemEnderFlake(), new ItemEnderBlaze());
	}

	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry().register(ModPotions.potionHypnotize);
		event.getRegistry().register(ModPotions.potionFreeze);
		event.getRegistry().register(ModPotions.potionJokerness);
		event.getRegistry().register(ModPotions.potionVomit);
		event.getRegistry().register(ModPotions.potionBlessed);
	}

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(ModSounds.fire_arrow_shot);
		event.getRegistry().register(ModSounds.entity_respawn);
		event.getRegistry().register(ModSounds.magma_remains_death);
		event.getRegistry().register(ModSounds.forest_skeleton_shoot);
		event.getRegistry().register(ModSounds.joker_ambient);
		event.getRegistry().register(ModSounds.ravenous_eating);
		event.getRegistry().register(ModSounds.burp);
		event.getRegistry().register(ModSounds.shield_block);
		event.getRegistry().register(ModSounds.vampire_bite);
		event.getRegistry().register(ModSounds.bat_morph);
		event.getRegistry().register(ModSounds.puke);
		event.getRegistry().register(ModSounds.spit);
	}

}
