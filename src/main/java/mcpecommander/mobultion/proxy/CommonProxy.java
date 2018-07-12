package mcpecommander.mobultion.proxy;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.enchantments.EnchantmentBlessed;
import mcpecommander.mobultion.events.SpawnEvent;
import mcpecommander.mobultion.gen.GenTest;
import mcpecommander.mobultion.gui.GuiProxy;
import mcpecommander.mobultion.init.ModDictionary;
import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.init.ModPotionTypes;
import mcpecommander.mobultion.init.ModSounds;
import mcpecommander.mobultion.items.ItemCorruptedBone;
import mcpecommander.mobultion.items.ItemCorruptedBonemeal;
import mcpecommander.mobultion.items.ItemEnderBlaze;
import mcpecommander.mobultion.items.ItemEnderFlake;
import mcpecommander.mobultion.items.ItemEnderGlassShot;
import mcpecommander.mobultion.items.ItemFang;
import mcpecommander.mobultion.items.ItemFangNecklace;
import mcpecommander.mobultion.items.ItemFireSword;
import mcpecommander.mobultion.items.ItemFlamingChip;
import mcpecommander.mobultion.items.ItemForestBow;
import mcpecommander.mobultion.items.ItemFork;
import mcpecommander.mobultion.items.ItemHammer;
import mcpecommander.mobultion.items.ItemHat;
import mcpecommander.mobultion.items.ItemHayHat;
import mcpecommander.mobultion.items.ItemHealingWand;
import mcpecommander.mobultion.items.ItemHealth;
import mcpecommander.mobultion.items.ItemHeartArrow;
import mcpecommander.mobultion.items.ItemHolyShard;
import mcpecommander.mobultion.items.ItemHypnoBall;
import mcpecommander.mobultion.items.ItemKnife;
import mcpecommander.mobultion.items.ItemMagicTouch;
import mcpecommander.mobultion.items.ItemMagmaArrow;
import mcpecommander.mobultion.items.ItemNetherRuby;
import mcpecommander.mobultion.items.ItemPigmanFlesh;
import mcpecommander.mobultion.items.ItemSorcererBreath;
import mcpecommander.mobultion.items.ItemSpawnChanger;
import mcpecommander.mobultion.items.ItemSpineAsh;
import mcpecommander.mobultion.items.ItemThunderWand;
import mcpecommander.mobultion.items.ItemWitherSpine;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathBoots;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathHelmet;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathLeggings;
import mcpecommander.mobultion.items.pigsheathArmor.ItemPigsheathTunic;
import mcpecommander.mobultion.potion.PotionBlessed;
import mcpecommander.mobultion.potion.PotionFreeze;
import mcpecommander.mobultion.potion.PotionHypnotize;
import mcpecommander.mobultion.potion.PotionInvert;
import mcpecommander.mobultion.potion.PotionJokerness;
import mcpecommander.mobultion.potion.PotionVomit;
import mcpecommander.mobultion.potion.potionTypes.PotionHypnotizeType;
import mcpecommander.mobultion.potion.potionTypes.PotionJokernessType;
import mcpecommander.mobultion.potion.potionTypes.PotionLongHypnotizeType;
import mcpecommander.mobultion.potion.potionTypes.PotionLongJokernessType;
import mcpecommander.mobultion.potion.potionTypes.PotionStrongHypnotizeType;
import mcpecommander.mobultion.potion.potionTypes.PotionWitherType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	private Potion potionJokerness;
	private Potion potionHypnotize;
	public static ArmorMaterial PIG_SHEATH;
	public ItemPigmanFlesh pigmanFlesh;

	public CommonProxy() {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.TERRAIN_GEN_BUS.register(new SpawnEvent());
	}

	public void preInit(FMLPreInitializationEvent e) {
		//GameRegistry.registerWorldGenerator(new GenTest(), 1);
	}

	public void init(FMLInitializationEvent e) {
		ModDictionary.registerDict();
		NetworkRegistry.INSTANCE.registerGuiHandler(MobultionMod.instance, new GuiProxy());
	}

	public void postInit(FMLPostInitializationEvent e) {
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		pigmanFlesh = new ItemPigmanFlesh();
		event.getRegistry().registerAll(new ItemForestBow(), new ItemHealingWand(), new ItemHeartArrow(),
				new ItemHammer(), new ItemFireSword(), new ItemHealth(), new ItemFork(), new ItemKnife(), new ItemHat(),
				new ItemEnderFlake(), new ItemEnderBlaze(), new ItemEnderGlassShot(), new ItemCorruptedBone(), new ItemCorruptedBonemeal(),
				new ItemHolyShard(), new ItemHypnoBall(), new ItemFangNecklace(), new ItemFang(), new ItemMagmaArrow(),
				new ItemMagicTouch(), new ItemSorcererBreath(), new ItemSpineAsh(), new ItemWitherSpine(),
				pigmanFlesh, new ItemFlamingChip(), new ItemNetherRuby());
		PIG_SHEATH = EnumHelper.addArmorMaterial("mobultion:pigsheath", "something", 120, new int[]{1,2,3,1}, 20, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0f).setRepairItem(new ItemStack(pigmanFlesh));
		event.getRegistry().registerAll( new ItemPigsheathTunic(), new ItemPigsheathHelmet(), new ItemPigsheathLeggings(),
				new ItemPigsheathBoots(), new ItemThunderWand(), new ItemHayHat(), new ItemSpawnChanger());
	}

	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> e) {
		potionJokerness = new PotionJokerness();
		potionHypnotize = new PotionHypnotize();
		e.getRegistry().register(potionHypnotize);
		e.getRegistry().register(new PotionFreeze());
		e.getRegistry().register(potionJokerness);
		e.getRegistry().register(new PotionVomit());
		e.getRegistry().register(new PotionBlessed());
		e.getRegistry().register(new PotionInvert());
	}

	@SubscribeEvent
	public void registerPotionType(RegistryEvent.Register<PotionType> e) {
		e.getRegistry().register(new PotionJokernessType(potionJokerness));
		e.getRegistry().register(new PotionLongJokernessType(potionJokerness));
		e.getRegistry().register(new PotionHypnotizeType(potionHypnotize));
		e.getRegistry().register(new PotionLongHypnotizeType(potionHypnotize));
		e.getRegistry().register(new PotionStrongHypnotizeType(potionHypnotize));
		e.getRegistry().register(new PotionWitherType(MobEffects.WITHER));
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> e) {
		PotionHelper.addMix(PotionTypes.AWKWARD, ModItems.hat, ModPotionTypes.potionJokernessType);
		PotionHelper.addMix(ModPotionTypes.potionJokernessType, Items.REDSTONE, ModPotionTypes.potionLongJokernessType);
		PotionHelper.addMix(PotionTypes.AWKWARD, ModItems.sorcererBreath, ModPotionTypes.potionHypnotizeType);
		PotionHelper.addMix(ModPotionTypes.potionHypnotizeType, Items.REDSTONE, ModPotionTypes.potionLongHypnotizeType);
		PotionHelper.addMix(ModPotionTypes.potionHypnotizeType, Items.GLOWSTONE_DUST,
				ModPotionTypes.potionStrongHypnotizeType);
		PotionHelper.addMix(PotionTypes.MUNDANE, ModItems.spineAsh, ModPotionTypes.potionWitherType);

		GameRegistry.addSmelting(ModItems.witherSpine, new ItemStack(ModItems.spineAsh, 3), 0.2f);

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
		e.getRegistry().register(ModSounds.vampire_death);
	}

	@SubscribeEvent
	public void registerEnchantments(RegistryEvent.Register<Enchantment> e) {
		e.getRegistry().register(new EnchantmentBlessed());
	}

}
