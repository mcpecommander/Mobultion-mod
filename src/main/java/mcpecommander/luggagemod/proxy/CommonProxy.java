package mcpecommander.luggagemod.proxy;


import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.blocks.BlockBlinker;
import mcpecommander.luggagemod.blocks.BlockCheese;
import mcpecommander.luggagemod.blocks.BlockCounter;
import mcpecommander.luggagemod.blocks.BlockFakeCheese;
import mcpecommander.luggagemod.blocks.BlockJar;
import mcpecommander.luggagemod.blocks.BlockJokerHat;
import mcpecommander.luggagemod.blocks.BlockPanelLight;
import mcpecommander.luggagemod.blocks.BlockRobinHat;
import mcpecommander.luggagemod.fluid.fluidBlock.MoltenCheeseBlock;
import mcpecommander.luggagemod.init.ModBlocks;
import mcpecommander.luggagemod.init.ModDimensions;
import mcpecommander.luggagemod.init.ModEntities;
import mcpecommander.luggagemod.init.ModFluids;
import mcpecommander.luggagemod.init.ModPotions;
import mcpecommander.luggagemod.init.ModSounds;
import mcpecommander.luggagemod.items.ItemArmorJoker;
import mcpecommander.luggagemod.items.ItemCheese;
import mcpecommander.luggagemod.items.ItemCheeseAndCracker;
import mcpecommander.luggagemod.items.ItemCheeseBlock;
import mcpecommander.luggagemod.items.ItemCracker;
import mcpecommander.luggagemod.items.ItemFireSword;
import mcpecommander.luggagemod.items.ItemHammer;
import mcpecommander.luggagemod.items.ItemHealingWand;
import mcpecommander.luggagemod.items.ItemHealth;
import mcpecommander.luggagemod.items.ItemHeartArrow;
import mcpecommander.luggagemod.items.multiModel.ItemForestBow;
import mcpecommander.luggagemod.items.multiModel.ItemWand;
import mcpecommander.luggagemod.tileEntity.TileEntityAtm;
import mcpecommander.luggagemod.tileEntity.TileEntityBlinker;
import mcpecommander.luggagemod.tileEntity.TileEntityCounter;
import mcpecommander.luggagemod.tileEntity.TileEntityJar;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	
	public static Configuration config;
	
	public CommonProxy(){
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	
	public void preInit(FMLPreInitializationEvent e) {
//		File directory = e.getModConfigurationDirectory();
//        config = new Configuration(new File(directory.getPath(), "mlm.cfg"));
//        SpidersConfig.readConfig();
		ModPotions.init();
		ModDimensions.init();
		ModEntities.init();

    }

    public void init(FMLInitializationEvent e) {
    	
    }

    public void postInit(FMLPostInitializationEvent e) {
//    	if (config.hasChanged()) {
//            config.save();
//        }
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
    	ModFluids.registerFluid();
    	event.getRegistry().registerAll(new BlockFakeCheese(), new BlockJar(), new BlockCounter(), new BlockBlinker(), new BlockPanelLight(), new BlockCheese(), new MoltenCheeseBlock(), new BlockRobinHat(), new BlockJokerHat());
    	GameRegistry.registerTileEntity(TileEntityJar.class, Reference.MOD_ID + "TileEntityJar");
		GameRegistry.registerTileEntity(TileEntityCounter.class, Reference.MOD_ID + "TileEntityCounter");
		GameRegistry.registerTileEntity(TileEntityBlinker.class, Reference.MOD_ID + "TileEntityBlinker");
		GameRegistry.registerTileEntity(TileEntityAtm.class, Reference.MOD_ID + "TileEntityAtm");
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
    	//ItemBlocks
    	event.getRegistry().register(new ItemBlock(ModBlocks.fakeCheese).setRegistryName(Reference.LuggageModBlocks.FAKECHEESE.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.jar).setRegistryName(Reference.LuggageModBlocks.JAR.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.counter).setRegistryName(Reference.LuggageModBlocks.COUNTER.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.blinker).setRegistryName(Reference.LuggageModBlocks.BLINKER.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.panelLight).setRegistryName(Reference.LuggageModBlocks.PANELLIGHT.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.robinHat).setRegistryName(Reference.LuggageModBlocks.ROBINHAT.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.jokerHat).setRegistryName(Reference.LuggageModBlocks.JOKERHAT.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.moltenCheese).setRegistryName(Reference.LuggageModBlocks.MOLTENCHEESE.getRegistryName()));
        //has its own cheeseItem class
        ItemBlock item = new ItemCheeseBlock(4, 0.6f, false);
        item.setRegistryName(Reference.LuggageModBlocks.CHEESE.getRegistryName());
        event.getRegistry().register(item);

        //Items
        event.getRegistry().registerAll(new ItemCheese(0, 0, false), new ItemCracker(),  new ItemCheeseAndCracker(), new ItemWand(), new ItemForestBow(), new ItemHealingWand(), new ItemHeartArrow(), new ItemArmorJoker(), new ItemHammer(), new ItemFireSword(), new ItemHealth());
    }
    
    @SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> event)
	{
    	event.getRegistry().register(ModPotions.potionHypnotize);
    	event.getRegistry().register(ModPotions.potionFreeze);
    	event.getRegistry().register(ModPotions.potionJokerness);
	}
    
    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event){
    	event.getRegistry().register(ModSounds.fire_arrow_shot);
    	event.getRegistry().register(ModSounds.entity_respawn);
    	event.getRegistry().register(ModSounds.magma_remains_death);
    	event.getRegistry().register(ModSounds.forest_skeleton_shoot);
    	event.getRegistry().register(ModSounds.joker_ambient);
    }

}
