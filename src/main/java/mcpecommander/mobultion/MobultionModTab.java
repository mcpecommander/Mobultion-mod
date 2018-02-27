package mcpecommander.mobultion;

import mcpecommander.mobultion.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class MobultionModTab extends CreativeTabs {

	public MobultionModTab() {
		super("tabMobultionMod");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.hammer);
	}
	
	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> stacks) {
		super.displayAllRelevantItems(stacks);
		for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values())
        {
			if(entitylist$entityegginfo.spawnedID.getResourceDomain().equals(Reference.MOD_ID)){
				ItemStack itemstack = new ItemStack(Items.SPAWN_EGG, 1);
            	ItemMonsterPlacer.applyEntityIdToItemStack(itemstack, entitylist$entityegginfo.spawnedID);
            	stacks.add(itemstack);
			}
        }
	}

}
