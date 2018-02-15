package mcpecommander.mobultion.items;


import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.Baubles;
import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public class ItemFangNecklace extends Item implements IBauble{
	
	public ItemFangNecklace() {
		this.setRegistryName(Reference.MobultionItems.FANGNECKLACE.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.FANGNECKLACE.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
}
