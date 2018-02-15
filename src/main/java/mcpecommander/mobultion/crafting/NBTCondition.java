package mcpecommander.mobultion.crafting;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import mcpecommander.mobultion.Reference;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class NBTCondition implements IRecipeFactory {
    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
    	//System.out.println(json);
        IRecipe recipe = ShapelessOreRecipe.factory(context, json);
        String id = JsonUtils.getString(json, "id", "");
        int lvl = JsonUtils.getInt(json, "lvl", -1);
        //System.out.println(recipe.getIngredients());
        return new NBTRecipe(new ResourceLocation(Reference.MOD_ID, "nbt_crafting"), recipe.getRecipeOutput(), id, lvl, recipe.getIngredients().toArray());
    }

    public static class NBTRecipe extends ShapelessOreRecipe {
    	private int lvl;
    	private String id;

        public NBTRecipe(ResourceLocation group, @Nonnull ItemStack result, String id, int lvl, Object... recipe) {
            super(group, result, recipe);
            this.id = id;
            this.lvl = lvl;
        }

        @Override
        @Nonnull
        public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
            ItemStack newOutput = this.output.copy();

            if (id != null && id != "" && lvl != -1) {
            	if(newOutput.getItem() instanceof ItemEnchantedBook){
	            	Enchantment enc = Enchantment.getEnchantmentByLocation(id);
	            	EnchantmentData data = new EnchantmentData(enc, lvl);
	            	ItemEnchantedBook book = (ItemEnchantedBook) newOutput.getItem();
	            	if(enc != null){
	            		book.addEnchantment(newOutput, data);
	            	}
            	}else{
	            	Enchantment enc = Enchantment.getEnchantmentByLocation(id);
	            	if(enc != null){
	            		newOutput.addEnchantment(enc, lvl);
	            	}
            	}
            }

            return newOutput;
        }
    }
}