package mcpecommander.mobultion;

import mcpecommander.mobultion.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class MobultionModTab extends CreativeTabs {

	public MobultionModTab() {
		super("tabMobultioMod");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.hammer);
	}

}
