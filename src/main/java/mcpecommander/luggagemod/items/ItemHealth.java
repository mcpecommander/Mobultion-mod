package mcpecommander.luggagemod.items;

import mcpecommander.luggagemod.LuggageMod;
import mcpecommander.luggagemod.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHealth extends Item{
	
	public ItemHealth() {
		this.setRegistryName(Reference.LuggageModItems.HEALTH.getRegistryName());
		this.setUnlocalizedName(Reference.LuggageModItems.HEALTH.getUnlocalizedName());
		
		this.maxStackSize = 16;
		//this.setFull3D();
		this.setCreativeTab(LuggageMod.LUGGAGEMOD_TAB);
		this.setMaxDamage(2);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 50;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.EAT;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(playerIn.getHealth() < playerIn.getMaxHealth()){
			playerIn.setActiveHand(handIn);
			return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}else return new ActionResult(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
		
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		for(int i = 0; i < 360; i += 20){
			double x = Math.sin(i);
			double z = Math.cos(i);
			entityLiving.world.spawnParticle(EnumParticleTypes.HEART, entityLiving.posX + x, entityLiving.posY + (entityLiving.getRNG().nextFloat() * .5), entityLiving.posZ + z, 0, 0, 0);
		}
		entityLiving.heal(4);
		stack.damageItem(1, entityLiving);
		((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, 400);
		return stack;
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
	
}
