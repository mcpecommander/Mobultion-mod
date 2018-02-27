package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFork extends Item{

	public ItemFork(){
		this.setRegistryName(Reference.MobultionItems.FORK.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.FORK.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
		this.setMaxDamage(100);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(!worldIn.isRemote){
			EntityPlayerMP player = (EntityPlayerMP) entityIn;
			if(player.getHeldItemOffhand() == stack && !player.getActiveItemStack().isEmpty() && player.getActiveItemStack().getItem() instanceof ItemFood){
				if(player.getItemInUseCount() <= player.getActiveItemStack().getMaxItemUseDuration() /2){
					player.getActiveItemStack().onItemUseFinish(worldIn, player);
					player.resetActiveHand();
					stack.damageItem(1, player);
					return;
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
