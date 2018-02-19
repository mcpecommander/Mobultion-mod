package mcpecommander.mobultion.proxy;

import mcpecommander.mobultion.enchantments.EnchantmentBlessed;
import mcpecommander.mobultion.events.SpawnEvent;
import mcpecommander.mobultion.init.ModDictionary;
import mcpecommander.mobultion.init.ModSounds;
import mcpecommander.mobultion.items.ItemCorruptedBone;
import mcpecommander.mobultion.items.ItemCorruptedBonemeal;
import mcpecommander.mobultion.items.ItemEnderBlaze;
import mcpecommander.mobultion.items.ItemEnderFlake;
import mcpecommander.mobultion.items.ItemFang;
import mcpecommander.mobultion.items.ItemFangNecklace;
import mcpecommander.mobultion.items.ItemFireSword;
import mcpecommander.mobultion.items.ItemForestBow;
import mcpecommander.mobultion.items.ItemFork;
import mcpecommander.mobultion.items.ItemHammer;
import mcpecommander.mobultion.items.ItemHat;
import mcpecommander.mobultion.items.ItemHealingWand;
import mcpecommander.mobultion.items.ItemHealth;
import mcpecommander.mobultion.items.ItemHeartArrow;
import mcpecommander.mobultion.items.ItemHolyShard;
import mcpecommander.mobultion.items.ItemHypnoBall;
import mcpecommander.mobultion.items.ItemKnife;
import mcpecommander.mobultion.items.ItemMagmaArrow;
import mcpecommander.mobultion.potion.PotionBlessed;
import mcpecommander.mobultion.potion.PotionFreeze;
import mcpecommander.mobultion.potion.PotionHypnotize;
import mcpecommander.mobultion.potion.PotionJokerness;
import mcpecommander.mobultion.potion.PotionVomit;
import net.minecraft.enchantment.Enchantment;
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
				new ItemEnderFlake(), new ItemEnderBlaze(), new ItemCorruptedBone(), new ItemCorruptedBonemeal(),
				new ItemHolyShard(), new ItemHypnoBall(), new ItemFangNecklace(), new ItemFang(), new ItemMagmaArrow());
	}

	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> e) {
		e.getRegistry().register(new PotionHypnotize());
		e.getRegistry().register(new PotionFreeze());
		e.getRegistry().register(new PotionJokerness());
		e.getRegistry().register(new PotionVomit());
		e.getRegistry().register(new PotionBlessed());
	}

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<SoundEvent> e) {
		e.getRegistry().register(ModSounds.fire_arrow_shot);
		e.getRegistry().register(ModSounds.entity_respawn);
		e.getRegistry().register(ModSounds.magma_remains_death);
		e.getRegistry().register(ModSounds.forest_skeleton_shoot);
		e.getRegistry().register(ModSounds.joker_ambient);
		e.getRegistry().register(ModSounds.ravenous_eating);
		e.getRegistry().register(ModSounds.burp);
		e.getRegistry().register(ModSounds.shield_block);
		e.getRegistry().register(ModSounds.vampire_bite);
		e.getRegistry().register(ModSounds.bat_morph);
		e.getRegistry().register(ModSounds.puke);
		e.getRegistry().register(ModSounds.spit);
	}

	@SubscribeEvent
	public void registerEnchantments(RegistryEvent.Register<Enchantment> e) {
		e.getRegistry().register(new EnchantmentBlessed());
	}

}
