package com.github.NeRdTheNed.ReciPic.RecipeWranglers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessOreRecipeWrangler extends AbstractRecipeWrangler {

    @Override
    public ItemStack[] wrangleRecipeType(IRecipe input) {
        if (input instanceof ShapelessOreRecipe) {
            int currentSlot = 0;
            final ItemStack[] outputRecipeStack = new ItemStack[9];
            final ShapelessOreRecipe shapelessOreRecipe = (ShapelessOreRecipe) input;

            for (final Object recipeInput : shapelessOreRecipe.getInput()) {
                final ItemStack stack = shapedRecipeObjectToItemStack(recipeInput);

                if (stack != null) {
                    outputRecipeStack[currentSlot] = stack;
                    currentSlot++;
                }

                // TODO Error handling
            }

            return outputRecipeStack;
        }

        return null;
    }
}
