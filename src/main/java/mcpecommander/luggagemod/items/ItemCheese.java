package mcpecommander.luggagemod.items;

import java.util.List;

import javax.annotation.Nullable;

import mcpecommander.luggagemod.LuggageMod;
import mcpecommander.luggagemod.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCheese extends Item {
	
	 public final int itemUseDuration;
	    /** The amount this food item heals the player. */
	    private final int healAmount;
	    private final float saturationModifier;
	    /** Whether wolves like this food (true for raw and cooked porkchop). */
	    private final boolean isWolfsFavoriteMeat;
	    /** If this field is true, the food can be consumed even if the player don't need to eat. */
	    private boolean alwaysEdible;
	    /** represents the potion effect that will occurr upon eating this food. Set by setPotionEffect */
	    private PotionEffect potionId;
	    /** probably of the set potion effect occurring */
	    private float potionEffectProbability;

	public ItemCheese(int amount, float saturation, boolean isWolfFood) {
		
	        this.itemUseDuration = 5;
	        this.healAmount = amount;
	        this.isWolfsFavoriteMeat = isWolfFood;
	        this.saturationModifier = saturation;
	        this.setCreativeTab(CreativeTabs.FOOD);
	    
		setUnlocalizedName(Reference.LuggageModItems.CHEESE.getUnlocalizedName());
		setRegistryName(Reference.LuggageModItems.CHEESE.getRegistryName());
		
		
		this.setCreativeTab(LuggageMod.LUGGAGEMOD_TAB);

		
	}
	
	
	
	@SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		
		tooltip.add(TextFormatting.UNDERLINE + "this is sparta");
		
    }
	
	@Nullable
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        stack.shrink(1);;

        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            entityplayer.getFoodStats().addStats(healAmount, saturationModifier);
            worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            this.onFoodEaten(stack, worldIn, entityplayer);
            entityplayer.addStat(StatList.getObjectUseStats(this));
        }

        return stack;
    }
	
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        if (!worldIn.isRemote && this.potionId != null && worldIn.rand.nextFloat() < this.potionEffectProbability)
        {
            player.addPotionEffect(new PotionEffect(this.potionId));
        }
    }
	
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if (playerIn.canEat(this.alwaysEdible))
        {
            playerIn.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
        }
        else
        {
            return new ActionResult(EnumActionResult.FAIL, itemStackIn);
        }
    }
	
	public ItemCheese setPotionEffect(PotionEffect p_185070_1_, float p_185070_2_)
    {
        this.potionId = p_185070_1_;
        this.potionEffectProbability = p_185070_2_;
        return this;
    }

    /**
     * Set the field 'alwaysEdible' to true, and make the food edible even if the player don't need to eat.
     */
    public ItemCheese setAlwaysEdible()
    {
        this.alwaysEdible = true;
        return this;
    }
    
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 5;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.EAT;
    }
	
    public String toolTips(EntityPlayer player){
    	if(player.isSneaking()){
    		return "this is sparta";
    	}
    	else return "";
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
