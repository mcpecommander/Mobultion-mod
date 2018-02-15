package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHolyShard extends ItemFood{
	
	public ItemHolyShard() {
		super(2, 1f, false);
		this.setRegistryName(Reference.MobultionItems.HOLYSHARD.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.HOLYSHARD.getUnlocalizedName());
		this.setAlwaysEdible();
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		if(!worldIn.isRemote){
			player.setAbsorptionAmount(player.getAbsorptionAmount() + 1);
		}
		super.onFoodEaten(stack, worldIn, player);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
