package mcpecommander.mobultion.integration;

import mcpecommander.mobultion.Reference;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@JEIPlugin
public class JEI implements IModPlugin {

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		//Hidden until fixed to work with JEI.
		jeiRuntime.getRecipeRegistry()
				.hideRecipe(jeiRuntime.getRecipeRegistry().getRecipeWrapper(
						ForgeRegistries.RECIPES
								.getValue(new ResourceLocation(Reference.MOD_ID, "fang_necklace_repair")),
						VanillaRecipeCategoryUid.CRAFTING));
	}

}
