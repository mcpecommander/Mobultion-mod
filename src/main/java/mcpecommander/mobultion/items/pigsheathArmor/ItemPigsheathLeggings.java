package mcpecommander.mobultion.items.pigsheathArmor;

import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModItems;
import mcpecommander.mobultion.particle.VomitParticle;
import mcpecommander.mobultion.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPigsheathLeggings extends ItemArmor{

	public ItemPigsheathLeggings() {
		super(CommonProxy.PIG_SHEATH, 1, EntityEquipmentSlot.LEGS);
		this.setRegistryName(Reference.MobultionItems.PIGSHEATHLEGGINGS.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.PIGSHEATHLEGGINGS.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if((player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() || !(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemPigsheathHelmet)) &&
				(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() || !(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemPigsheathTunic))){
			if(world.isRemote && this.itemRand.nextInt(10) == 0){
				this.spawnParticles(world, player);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void spawnParticles(World world, EntityPlayer player){
		VomitParticle particle = new VomitParticle(world, player.posX + (this.itemRand.nextDouble() - 0.5), player.posY + player.height, player.posZ + (this.itemRand.nextDouble() - 0.5), (this.itemRand.nextGaussian() - 0.5d)/100d, 0.05, (this.itemRand.nextGaussian() - 0.5d)/100d, this.itemRand.nextFloat() + 0.8f);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return new ResourceLocation(Reference.MOD_ID, "textures/entity/pigsheath_leggings.png").toString();
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
