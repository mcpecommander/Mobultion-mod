package dev.mcpecommander.mobultion.datagen;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/* McpeCommander created on 01/08/2021 inside the package - dev.mcpecommander.mobultion.datagen */
public class RecipesGen extends RecipeProvider {

    public RecipesGen(DataGenerator datagen) {
        super(datagen);
    }

    @Override
    protected void buildShapelessRecipes(@Nonnull Consumer<IFinishedRecipe> finishedRecipe) {
        ShapelessRecipeBuilder.shapeless(Registration.CORRUPTEDBONEMEAL.get(), 3)
                .requires(Registration.CORRUPTEDBONE.get()).unlockedBy("has_corrupted_bonemeal",
                        has(Registration.CORRUPTEDBONE.get())).save(finishedRecipe);
    }
}
