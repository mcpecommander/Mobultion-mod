package mcpecommander.luggagemod;

import mcpecommander.luggagemod.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LuggageModTab extends CreativeTabs{

	public LuggageModTab() {
		super("tabLuggageMod");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.cheese);
	}

}
