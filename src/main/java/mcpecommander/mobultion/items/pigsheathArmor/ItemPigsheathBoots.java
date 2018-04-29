package mcpecommander.mobultion.items.pigsheathArmor;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.particle.VomitParticle;
import mcpecommander.mobultion.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPigsheathBoots extends ItemArmor{
	

	public ItemPigsheathBoots() {
		super(CommonProxy.PIG_SHEATH, 1, EntityEquipmentSlot.FEET);
		this.setRegistryName(Reference.MobultionItems.PIGSHEATHBOOTS.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.PIGSHEATHBOOTS.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if((player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() || !(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemPigsheathHelmet)) &&
				(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty() || !(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemPigsheathLeggings)) &&
				(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() || !(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemPigsheathTunic))){
			if(world.isRemote && Item.itemRand.nextInt(10) == 0){
				this.spawnParticles(world, player);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void spawnParticles(World world, EntityPlayer player){
		VomitParticle particle = new VomitParticle(world, player.posX + (Item.itemRand.nextDouble() - 0.5), player.posY + player.height, player.posZ + (Item.itemRand.nextDouble() - 0.5), (Item.itemRand.nextGaussian() - 0.5d)/100d, 0.05, (Item.itemRand.nextGaussian() - 0.5d)/100d, Item.itemRand.nextFloat() + 0.8f);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return new ResourceLocation(Reference.MOD_ID, "textures/entity/pigsheath_tunic.png").toString();
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
