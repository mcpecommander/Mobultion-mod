package mcpecommander.luggagemod.init;

import mcpecommander.luggagemod.blocks.BlockBlinker;
import mcpecommander.luggagemod.blocks.BlockCheese;
import mcpecommander.luggagemod.blocks.BlockCounter;
import mcpecommander.luggagemod.blocks.BlockFakeCheese;
import mcpecommander.luggagemod.blocks.BlockJar;
import mcpecommander.luggagemod.blocks.BlockJokerHat;
import mcpecommander.luggagemod.blocks.BlockPanelLight;
import mcpecommander.luggagemod.blocks.BlockRobinHat;
import mcpecommander.luggagemod.fluid.fluidBlock.MoltenCheeseBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
	
	@GameRegistry.ObjectHolder("mlm:block_cheese")
	public static BlockCheese cheese;
	@GameRegistry.ObjectHolder("mlm:block_fake_cheese")
	public static BlockFakeCheese fakeCheese;
	@GameRegistry.ObjectHolder("mlm:block_jar")
	public static BlockJar jar;
	@GameRegistry.ObjectHolder("mlm:block_counter")
	public static BlockCounter counter;
	@GameRegistry.ObjectHolder("mlm:block_blinker")
	public static BlockBlinker blinker;
	@GameRegistry.ObjectHolder("mlm:block_panel_light")
	public static BlockPanelLight panelLight;
	@GameRegistry.ObjectHolder("mlm:block_robin_hat")
	public static BlockRobinHat robinHat;
	@GameRegistry.ObjectHolder("mlm:block_joker_hat")
	public static BlockJokerHat jokerHat;
	@GameRegistry.ObjectHolder("mlm:fluid_block_molten_cheese")
	public static MoltenCheeseBlock moltenCheese;
	
	

	@SideOnly(Side.CLIENT)
    public static void initModels() {
		//cheese = new BlockCheese();
        cheese.initModel();
        //fakeCheese = new BlockFakeCheese();
        fakeCheese.initModel();
        //jar = new BlockJar();
        jar.initModel();
        //counter = new BlockCounter();
        counter.initModel();
        //blinker = new BlockBlinker();
        blinker.initModel();
        //panelLight = new BlockPanelLight();
        panelLight.initModel();
        //atm= new BlockAtm();
        robinHat.initModel();
        jokerHat.initModel();
        moltenCheese.initModel();
    }
	
	
}
