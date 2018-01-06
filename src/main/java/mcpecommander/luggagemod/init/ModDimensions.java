package mcpecommander.luggagemod.init;

import mcpecommander.luggagemod.LuggageMod;
import mcpecommander.luggagemod.Reference;
import mcpecommander.luggagemod.dimension.CheeseWorldProvider;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Config;

public class ModDimensions {
	public static DimensionType cheeseDimensionType;

    public static void init() {
        registerDimensionTypes();
        registerDimensions();
    }

    private static void registerDimensionTypes() {
        cheeseDimensionType = DimensionType.register(Reference.MOD_ID, "_test", 100, CheeseWorldProvider.class, false);
    }

    private static void registerDimensions() {
        DimensionManager.registerDimension(100, cheeseDimensionType);
    }
}
