package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCorruptedBone extends Item{

	public ItemCorruptedBone(){
		this.setRegistryName(Reference.MobultionItems.CORRUPTEDBONE.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.CORRUPTEDBONE.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
		
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}