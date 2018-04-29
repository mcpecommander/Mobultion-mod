package mcpecommander.mobultion.items;


import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.render.IRenderBauble;
import mcpecommander.mobultion.MobultionMod;
import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
@Optional.Interface(modid = "baubles", iface = "baubles.api.render.IRenderBauble")
public class ItemFangNecklace extends Item implements IBauble, IRenderBauble{
	
	public ItemFangNecklace() {
		this.setRegistryName(Reference.MobultionItems.FANGNECKLACE.getRegistryName());
		this.setUnlocalizedName(Reference.MobultionItems.FANGNECKLACE.getUnlocalizedName());
		this.setCreativeTab(MobultionMod.MOBULTION_TAB);
		this.setMaxDamage(5);
		this.setMaxStackSize(1);
		this.setNoRepair();
		
		this.addPropertyOverride(new ResourceLocation(Reference.MOD_ID, "fangs"), new IItemPropertyGetter() {
			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				return stack.getItemDamage();
			}
		});
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(GuiScreen.isShiftKeyDown()){
			tooltip.add("Fangs: " + stack.getItemDamage());
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}
	
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if((player.getActivePotionEffect(MobEffects.NIGHT_VISION) == null || player.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration() < 280))
		player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0));
	}
	
	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		if((player.getActivePotionEffect(MobEffects.NIGHT_VISION) != null && player.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration() <= 300))
			player.removePotionEffect(MobEffects.NIGHT_VISION);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return stack.getItemDamage() !=5;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		 if (this.isInCreativeTab(tab))
	        {
	            items.add(new ItemStack(this));
	            items.add(new ItemStack(this, 1, 5));
	        }
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(entityIn instanceof EntityPlayerMP){
			if((isSelected || ((EntityPlayer) entityIn).getHeldItemOffhand() == stack) && stack.getItemDamage() == stack.getMaxDamage()){
				if(((EntityLivingBase) entityIn).getActivePotionEffect(MobEffects.NIGHT_VISION) == null || ((EntityLivingBase) entityIn).getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration() < 280){
					((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0));
					if(stack.getTagCompound() == null){
						NBTTagCompound tag = new NBTTagCompound();
						stack.setTagCompound(tag);
					}
					if(stack.getTagCompound() != null){
						stack.getTagCompound().setBoolean("wasSelected", true);
					}
				}
			}
			if (!isSelected && ((EntityPlayer) entityIn).getHeldItemOffhand() != stack && stack.getTagCompound() != null && stack.getTagCompound().getBoolean("wasSelected")){
				if(((EntityLivingBase) entityIn).getActivePotionEffect(MobEffects.NIGHT_VISION) != null && ((EntityLivingBase) entityIn).getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration() <= 300){
					((EntityLivingBase) entityIn).removePotionEffect(MobEffects.NIGHT_VISION);
					stack.getTagCompound().setBoolean("wasSelected", false);
				}
			}
		}
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1d - (double)stack.getItemDamage() / (double)stack.getMaxDamage();
	}
	
	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return itemstack.getItemDamage() == itemstack.getMaxDamage();
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		final ModelResourceLocation model = new ModelResourceLocation(this.getRegistryName() , "inventory");
		final ModelResourceLocation model1 = new ModelResourceLocation(this.getRegistryName() + "_1", "inventory");
		final ModelResourceLocation model2 = new ModelResourceLocation(this.getRegistryName() + "_2", "inventory");
		final ModelResourceLocation model3 = new ModelResourceLocation(this.getRegistryName() + "_3", "inventory");
		final ModelResourceLocation model4 = new ModelResourceLocation(this.getRegistryName() + "_4", "inventory");
		final ModelResourceLocation model5 = new ModelResourceLocation(this.getRegistryName() + "_5", "inventory");

		ModelBakery.registerItemVariants(this, model, model1, model2, model3, model4, model5);

		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return model;
			}
		});
	}

	@Override
    @SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if (type != RenderType.BODY) return;

        Minecraft mc = FMLClientHandler.instance().getClient();
        ItemStack necklace = new ItemStack(ModItems.fangNecklace, 1, 5);
        TransformType transform = TransformType.NONE;

        Helper.rotateIfSneaking(player);

        GlStateManager.scale(0.4, 0.4, 0.4);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.translate(0.0, -0.3, !player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() ? -0.5 : -0.3);

        mc.getRenderItem().renderItem(necklace, transform);
		
	}

	
}
