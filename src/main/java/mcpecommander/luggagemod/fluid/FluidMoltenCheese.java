package mcpecommander.luggagemod.fluid;

import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.fluid.fluidBlock.MoltenCheeseBlock;
import mcpecommander.luggagemod.init.ModBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidMoltenCheese extends Fluid{
	
	static ResourceLocation STILL = new ResourceLocation(Reference.MOD_ID, "blocks/molten_cheese_still");
	static ResourceLocation FLOWING = new ResourceLocation(Reference.MOD_ID, "blocks/molten_cheese_flowing");
	
	public FluidMoltenCheese() {
		super("molten_cheese", STILL, FLOWING);
		this.setLuminosity(1).setViscosity(1412).setDensity(1263).setGaseous(false).setTemperature(355);
		this.setUnlocalizedName("molten_cheese");
		//this.setBlock(ModBlocks.moltenCheese);
		
	}
	
	
	
	
}
