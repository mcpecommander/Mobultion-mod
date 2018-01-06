package mcpecommander.luggagemod.items;

import com.google.common.collect.Sets;

import mcpecommander.luggagemod.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemTool;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHammer extends ItemTool{
			
	public ItemHammer() {
		super(1.0F, -2.8F, ToolMaterial.IRON, Sets.newHashSet(Blocks.COBBLESTONE));
		this.setUnlocalizedName(Reference.LuggageModItems.HAMMER.getUnlocalizedName());
		this.setRegistryName(Reference.LuggageModItems.HAMMER.getRegistryName());
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
