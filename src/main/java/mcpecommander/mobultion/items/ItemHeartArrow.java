package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.skeletons.EntityHeartArrow;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHeartArrow extends ItemArrow{
	
	public ItemHeartArrow() {
		setUnlocalizedName(Reference.ModItems.HEARTARROW.getUnlocalizedName());
		setRegistryName(Reference.ModItems.HEARTARROW.getRegistryName());
		
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
	}
	@Override
	public EntityHeartArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
		EntityHeartArrow arrow = new EntityHeartArrow(worldIn, shooter);
        return arrow;
    }

	@Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.EntityPlayer player)
    {
        int enchant = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY, bow);
        return enchant <= 0 ? false : this.getClass() == ItemHeartArrow.class;
    }
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
