package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.spiders.EntityHypnoBall;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHypnoBall extends Item{
	
	public ItemHypnoBall() {
		this.setRegistryName(Reference.MobultionItems.HYPNOBALL.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.HYPNOBALL.getUnlocalizedName());
		
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote){
			Vec3d vec = playerIn.getLookVec();
			EntityHypnoBall hypno = new EntityHypnoBall(worldIn, playerIn, vec.x, vec.y, vec.z);
			worldIn.spawnEntity(hypno);
			hypno.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, .5f, 1.5f);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1;
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
