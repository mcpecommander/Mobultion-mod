package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMagicTouch extends ItemFood{
	
	public ItemMagicTouch(){
		super(0, false);
		this.setRegistryName(Reference.MobultionItems.MAGICTOUCH.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.MAGICTOUCH.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
		this.setAlwaysEdible();
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		if(!worldIn.isRemote)
		player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getRandomObject(itemRand), 600));
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
