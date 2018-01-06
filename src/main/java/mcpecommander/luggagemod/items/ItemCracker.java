package mcpecommander.luggagemod.items;

import mcpecommander.luggagemod.LuggageMod;
import mcpecommander.luggagemod.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCracker extends ItemFood {

	public ItemCracker() {
		super(3, 0.5F, false);
		setUnlocalizedName(Reference.LuggageModItems.CRACKER.getUnlocalizedName());
		setRegistryName(Reference.LuggageModItems.CRACKER.getRegistryName());
		
		this.setCreativeTab(LuggageMod.LUGGAGEMOD_TAB);

	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
