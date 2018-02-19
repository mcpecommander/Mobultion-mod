package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.entity.entities.skeletons.EntityMagmaArrow;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMagmaArrow extends ItemArrow{
	
	public ItemMagmaArrow() {
		this.setRegistryName(Reference.MobultionItems.MAGMAARROW.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.MAGMAARROW.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
	}

	@Override
	public EntityMagmaArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
		EntityMagmaArrow arrow = new EntityMagmaArrow(worldIn, shooter);
        return arrow;
    }

	@Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.EntityPlayer player)
    {
        int enchant = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bow);
        return enchant <= 0 ? false : this.getClass() == ItemMagmaArrow.class;
    }
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
