package mcpecommander.luggagemod.items.multiModel;

import java.util.List;

import javax.annotation.Nullable;

import mcpecommander.luggagemod.LuggageMod;
import mcpecommander.luggagemod.Reference;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWand extends Item {

	public ItemWand() {
		setUnlocalizedName(Reference.LuggageModItems.WAND.getUnlocalizedName());
		setRegistryName(Reference.LuggageModItems.WAND.getRegistryName());


		this.setCreativeTab(LuggageMod.LUGGAGEMOD_TAB);

	}

	public boolean isBlue(ItemStack stack) {
		return getTagCompoundSafe(stack).hasKey("blue");
	}

	private NBTTagCompound getTagCompoundSafe(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		return tagCompound;
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		final ModelResourceLocation onModel = new ModelResourceLocation(this.getRegistryName() + "_on", "inventory");
		final ModelResourceLocation offModel = new ModelResourceLocation(this.getRegistryName() + "_off", "inventory");

		ModelBakery.registerItemVariants(this, onModel, offModel);

		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				if (isBlue(stack)) {
					return onModel;
				} else {
					return offModel;
				}
			}
		});
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick( World world, EntityPlayer playerIn,
			EnumHand hand) {
		ItemStack stack = new ItemStack(this);
		stack = playerIn.getHeldItem(hand);
		int slot = playerIn.inventory.getSlotFor(new ItemStack(Items.DIAMOND));
		if (!world.isRemote) {
			if (playerIn.isSneaking() && slot != -1) {
				ItemStack cost = playerIn.inventory.getStackInSlot(slot);
				if (isBlue(stack)) {

					getTagCompoundSafe(stack).removeTag("blue");
				} else {
					getTagCompoundSafe(stack).setBoolean("blue", true);
					cost.shrink(1);
				}
			}else return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (isBlue(stack)) {
			tooltip.add("Activated");
		}
	}

}
