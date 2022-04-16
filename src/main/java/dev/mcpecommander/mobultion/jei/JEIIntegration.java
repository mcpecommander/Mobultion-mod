package dev.mcpecommander.mobultion.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Objects;

import static dev.mcpecommander.mobultion.Mobultion.MODID;

/* McpeCommander created on 10/08/2021 inside the package - dev.mcpecommander.mobultion.jei */
@JeiPlugin
public class JEIIntegration implements IModPlugin {

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MODID, "main");
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        for(Item item : ForgeRegistries.ITEMS.getValues()){
            if(!Objects.requireNonNull(item.getRegistryName()).getNamespace().equals(MODID)) continue;
            registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM_STACK,
                    new TranslatableComponent(item.getRegistryName().toString().concat(".info")));
        }
    }

}
