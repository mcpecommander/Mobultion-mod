package mcpecommander.mobultion.crafting;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import mcpecommander.mobultion.Reference;
import mcpecommander.mobultion.items.ItemFang;
import mcpecommander.mobultion.items.ItemFangNecklace;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class FangRepairType implements IRecipeFactory {
	
	
    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        IRecipe recipe = ShapelessOreRecipe.factory(context, json);
        NonNullList<Ingredient> list = recipe.getIngredients();
        return new FangRepairRecipe(new ResourceLocation(Reference.MOD_ID, "fang_repair_crafting"), list, recipe.getRecipeOutput() );
    }

    public static class FangRepairRecipe extends ShapelessOreRecipe{

        public FangRepairRecipe(ResourceLocation group, NonNullList<Ingredient> input ,@Nonnull ItemStack result) {
            super(group, input, result);
            
        }
        
        @Override
        public boolean isDynamic() {
        	return true;
        }
        
        @Override
        public boolean matches(InventoryCrafting inv, World world) {
        	int numberOfNecklaces = 0;
        	int numberOfFangs = 0;
        	int meta = 9;
        	for(int i = 0; i < inv.getSizeInventory(); i++){
        		ItemStack item = inv.getStackInSlot(i).copy();
        		if(item.getItem() instanceof ItemFang){
        			numberOfFangs++;
        		}
        		if(item.getItem() instanceof ItemFangNecklace){
        			numberOfNecklaces++;
        			meta = item.getItemDamage();
        		}
        	}
        	boolean flag = numberOfNecklaces == 1 && numberOfFangs != 0  && numberOfFangs <= (5 - meta);
        	return flag;
        }

        @Override
        @Nonnull
        public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
            ItemStack newOutput = this.output.copy();

            for(int i = 0; i < var1.getSizeInventory(); i++){
            	ItemStack item = var1.getStackInSlot(i).copy();
            	if(!item.isEmpty() && item.getItem() instanceof ItemFangNecklace){
            		if(item.getItemDamage() < 5){
            			newOutput.setItemDamage(getFangs(var1) + item.getItemDamage());
            		}
            	}
            }
            

            return newOutput;
        }
        
        private int getFangs(@Nonnull InventoryCrafting var1){
        	int p = 0;
        	for(int i = 0; i < var1.getSizeInventory(); i++){
        		ItemStack item = var1.getStackInSlot(i).copy();
        		if(!item.isEmpty() && item.getItem() instanceof ItemFang){
        			p++;
        		}
        	}
        	return p;
        }
		
    }
}