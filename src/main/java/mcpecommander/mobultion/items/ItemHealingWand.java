package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHealingWand extends Item {

	public ItemHealingWand() {
		this.setRegistryName(Reference.MobultionItems.HEALINGWAND.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.HEALINGWAND.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
		this.maxStackSize = 1;
		this.setMaxDamage(5);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 200;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.getHealth() < playerIn.getMaxHealth()) {
			playerIn.setActiveHand(handIn);
			return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		} else
			return new ActionResult(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));

	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		if (count % 3 == 0 && player.world.isRemote) {
			double pitch = ((player.rotationPitch + 90) * Math.PI) / 180;
			double yaw = ((player.rotationYawHead + 90) * Math.PI) / 180;
			double z = Math.sin(yaw);
			double x = Math.cos(yaw);
			double y = Math.cos(pitch);
			double d0 = (double) (16135577 >> 16 & 255) / 255.0D;
			double d1 = (double) (16135577 >> 8 & 255) / 255.0D;
			double d2 = (double) (16135577 >> 0 & 255) / 255.0D;
			player.world.spawnParticle(EnumParticleTypes.SPELL_MOB, player.posX + (x * 0.8D),
					player.posY + player.getEyeHeight() + y, player.posZ + (z * 0.8D), d0, d1, d2);
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		for (int i = 0; i < 360; i += 20) {
			double x = Math.sin(i);
			double z = Math.cos(i);
			entityLiving.world.spawnParticle(EnumParticleTypes.HEART, entityLiving.posX + x,
					entityLiving.posY + (entityLiving.getRNG().nextFloat() * .5), entityLiving.posZ + z, 0, 0, 0);
		}
		entityLiving.heal(4);
		stack.damageItem(1, entityLiving);
		((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, 400);
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
