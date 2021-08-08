package dev.mcpecommander.mobultion.datagen;

import dev.mcpecommander.mobultion.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

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
        ShapelessRecipeBuilder.shapeless(Items.BLAZE_ROD, 1).requires(Registration.FLAMINGLEG.get(), 4)
                .unlockedBy("has_flaming_leg", has(Registration.FLAMINGLEG.get())).save(finishedRecipe);
        ShapelessRecipeBuilder.shapeless(Items.BLAZE_POWDER, 1).requires(Registration.FLAMINGLEG.get(), 2)
                .unlockedBy("has_flaming_leg", has(Registration.FLAMINGLEG.get())).save(finishedRecipe);
        ShapelessRecipeBuilder.shapeless(Registration.FANGNECKLACE.get()).requires(Tags.Items.STRING)
                .requires(Registration.FANG.get(), 5).unlockedBy("has_fang", has(Registration.FANG.get()))
                .save(finishedRecipe);

    }
}
