package dev.mcpecommander.mobultion.datagen;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.data.*;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;

/* McpeCommander created on 01/08/2021 inside the package - dev.mcpecommander.mobultion.datagen */
public class RecipesGen extends RecipeProvider {

    public RecipesGen(DataGenerator datagen) {
        super(datagen);
    }

    /**
     * Add the recipes in this method and don't call super.
     * @param finishedRecipe Used in the .save(); when finishing building a recipe.
     */
    @Override
    protected void buildCraftingRecipes(@Nonnull Consumer<FinishedRecipe> finishedRecipe) {
        ShapelessRecipeBuilder.shapeless(Registration.CORRUPTEDBONEMEAL.get(), 3)
                .requires(Registration.CORRUPTEDBONE.get()).unlockedBy("has_corrupted_bonemeal",
                        has(Registration.CORRUPTEDBONE.get())).save(finishedRecipe);
        ShapelessRecipeBuilder.shapeless(Items.BLAZE_ROD, 1).requires(Registration.FLAMINGLEG.get(), 4)
                .unlockedBy("has_flaming_leg", has(Registration.FLAMINGLEG.get())).save(finishedRecipe);
        ShapelessRecipeBuilder.shapeless(Items.BLAZE_POWDER, 1).requires(Registration.FLAMINGLEG.get(), 2)
                .unlockedBy("has_flaming_leg", has(Registration.FLAMINGLEG.get())).save(finishedRecipe);
        ShapelessRecipeBuilder.shapeless(Registration.FANGNECKLACE.get()).requires(Tags.Items.STRING)
                .requires(Registration.FANG.get(), 5).unlockedBy("has_fang", has(Registration.FANG.get()))
                .save(finishedRecipe);
        ShapedRecipeBuilder.shaped(Registration.HYPNOEMITTER.get()).define('g', Registration.GLASSSHOT_ITEM.get())
                .define('h', Registration.HALO.get()).define('m', Registration.MAGICGOOP.get())
                .pattern(" h ").pattern("hgh").pattern(" m ").unlockedBy("has_magic_goop",
                        has(Registration.MAGICGOOP.get())).save(finishedRecipe);

    }
}
