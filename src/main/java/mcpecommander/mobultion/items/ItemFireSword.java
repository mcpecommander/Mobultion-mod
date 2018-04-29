package mcpecommander.mobultion.items;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFireSword extends ItemSword{

	public ItemFireSword() {
		super(ToolMaterial.DIAMOND);
		this.setRegistryName(Reference.MobultionItems.FIRESWORD.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.FIRESWORD.getUnlocalizedName());
		
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(isSelected && stack.getItemDamage() > 1350){
			if(entityIn instanceof EntityPlayer){
				if(worldIn.rand.nextInt(100) == 0){
					stack.damageItem(1, (EntityLivingBase) entityIn);
				}else if(worldIn.rand.nextInt(500) == 0){
					entityIn.setFire(7);
				}
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return false;
    }
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		target.setFire(7);
		return true;
	}

	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
