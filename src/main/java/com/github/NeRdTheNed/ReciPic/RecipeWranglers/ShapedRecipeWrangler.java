package com.github.NeRdTheNed.ReciPic.RecipeWranglers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

public class ShapedRecipeWrangler extends AbstractRecipeWrangler {

    @Override
    public ItemStack[] wrangleRecipeType(IRecipe input) {
        if (input instanceof ShapedRecipes) {
            int craftingInputSlot = 0;
            final ShapedRecipes shapedRecipe = (ShapedRecipes) input;
            final ItemStack[] outputRecipeStack = new ItemStack[9];

            for (int height = 0; height < shapedRecipe.recipeHeight; height++) {
                for (int width = 0; width < shapedRecipe.recipeWidth; width++) {
                    if (shapedRecipe.recipeItems[craftingInputSlot] != null) {
                        outputRecipeStack[width + (height * 3)] = shapedRecipe.recipeItems[craftingInputSlot];
                    }

                    craftingInputSlot++;
                }
            }

            return outputRecipeStack;
        }

        return null;
    }
}
