package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFang extends Item{
	
	public ItemFang() {
		this.setRegistryName(Reference.MobultionItems.FANG.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.FANG.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
