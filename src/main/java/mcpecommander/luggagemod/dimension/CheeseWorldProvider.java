package mcpecommander.luggagemod.dimension;

import mcpecommander.luggagemod.init.ModDimensions;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class CheeseWorldProvider extends WorldProvider{
	

	
	@Override
	public DimensionType getDimensionType() {
		// TODO Auto-generated method stub
		return ModDimensions.cheeseDimensionType;
	}
	
	@Override
	public boolean isSkyColored() {

		return false;
	}

	@Override
    public String getSaveFolder() {
        return "Cheese";
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new CheeseChunkGenerator(world);
    }
	
}
