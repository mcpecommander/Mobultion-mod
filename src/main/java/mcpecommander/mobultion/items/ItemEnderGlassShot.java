package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.endermen.EntityEnderProjectile;
import mcpecommander.mobultion.mobConfigs.EndermenConfig;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnderGlassShot extends Item{
	
	public ItemEnderGlassShot() {
		this.maxStackSize = 16;
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
		
		this.setRegistryName(Reference.MobultionItems.ENDERGLASSSHOT.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.ENDERGLASSSHOT.getUnlocalizedName());
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDERPEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        playerIn.getCooldownTracker().setCooldown(this, 10);

        if (!worldIn.isRemote)
        {
            EntityEnderProjectile glass = new EntityEnderProjectile(worldIn, playerIn, 4.5f, .02f);
            glass.setType((byte)2);
            glass.setThornsDamage(2f);
            glass.setBreakChance((float) EndermenConfig.endermen.glass.breakChance);
            glass.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.7F, 0.7F);
            worldIn.spawnEntity(glass);
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
