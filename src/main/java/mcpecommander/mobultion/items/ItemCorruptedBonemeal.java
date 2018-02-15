package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCorruptedBonemeal extends Item {

	public ItemCorruptedBonemeal() {
		this.setRegistryName(Reference.MobultionItems.CORRUPTEDBONEMEAL.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.CORRUPTEDBONEMEAL.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);

	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);
		// System.out.println(worldIn.isRemote);
		if (!worldIn.isRemote) {
			if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
				return EnumActionResult.FAIL;
			} else {
				if (itemRand.nextFloat() < 0.7f) {
					ItemDye.applyBonemeal(itemstack, worldIn, pos, player, hand);
					WorldServer world = (WorldServer) worldIn;
					for (int i = 0; i < 16; ++i) {
						double d0 = itemRand.nextGaussian() * 0.02D;
						double d1 = itemRand.nextGaussian() * 0.02D;
						double d2 = itemRand.nextGaussian() * 0.02D;
						world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
								(double) ((float) pos.getX() + itemRand.nextFloat()),
								(double) pos.getY() + (double) itemRand.nextFloat()
										* worldIn.getBlockState(pos).getBoundingBox(worldIn, pos).maxY,
								(double) ((float) pos.getZ() + itemRand.nextFloat()), 1, d0, d1, d2, 0.01d);
					}

					return EnumActionResult.SUCCESS;
				} else {
					if (worldIn.getBlockState(pos).getBlock() instanceof IGrowable) {
						WorldServer world = (WorldServer) worldIn;
						for (int i = 0; i < 8; ++i) {
							double d0 = itemRand.nextGaussian() * 0.02D;
							double d1 = itemRand.nextGaussian() * 0.02D;
							double d2 = itemRand.nextGaussian() * 0.02D;
							world.spawnParticle(EnumParticleTypes.SPIT,
									(double) ((float) pos.getX() + itemRand.nextFloat()),
									(double) pos.getY() + (double) itemRand.nextFloat()
											* worldIn.getBlockState(pos).getBoundingBox(worldIn, pos).maxY,
									(double) ((float) pos.getZ() + itemRand.nextFloat()), 1, d0, d1, d2, 0.01d);
						}
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
						itemstack.shrink(1);
						return EnumActionResult.SUCCESS;
					}
				}
			}
		}
		return EnumActionResult.FAIL;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
