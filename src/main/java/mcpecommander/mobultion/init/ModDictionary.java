package mcpecommander.mobultion.init;

import net.minecraftforge.oredict.OreDictionary;

public class ModDictionary {
	
	public static void registerDict(){
		OreDictionary.registerOre("enderpearl", ModItems.enderBlaze);
		OreDictionary.registerOre("enderpearl", ModItems.enderFlake);
		OreDictionary.registerOre("enderpearl", ModItems.enderGlassShot);
	}

}
