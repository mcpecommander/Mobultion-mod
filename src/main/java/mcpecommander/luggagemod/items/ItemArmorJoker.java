package mcpecommander.luggagemod.items;

import mcpecommander.luggagemod.LuggageMod;
import mcpecommander.luggagemod.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorJoker extends Item{

	public ItemArmorJoker() {
		this.setRegistryName(Reference.LuggageModItems.HAT.getRegistryName());
		this.setUnlocalizedName(Reference.LuggageModItems.HAT.getUnlocalizedName());
		this.setCreativeTab(LuggageMod.LUGGAGEMOD_TAB);
	}
	
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
