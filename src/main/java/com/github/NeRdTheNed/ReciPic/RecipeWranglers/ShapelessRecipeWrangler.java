package com.github.NeRdTheNed.ReciPic.RecipeWranglers;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;

public class ShapelessRecipeWrangler extends AbstractRecipeWrangler {

    @Override
    public ItemStack[] wrangleRecipeType(IRecipe input) {
        if (input instanceof ShapelessRecipes) {
            final ShapelessRecipes shapelessRecipe = (ShapelessRecipes) input;
            @SuppressWarnings("unchecked")
            final List<ItemStack> recipeItems = shapelessRecipe.recipeItems;
            // TODO Make this safe
            return recipeItems.toArray(new ItemStack[9]);
        }

        return null;
    }
}
