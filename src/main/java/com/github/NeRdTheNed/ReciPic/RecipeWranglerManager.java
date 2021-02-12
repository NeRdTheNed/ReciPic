package com.github.NeRdTheNed.ReciPic;

import java.util.ArrayList;

import com.github.NeRdTheNed.ReciPic.RecipeWranglers.IRecipeWrangler;
import com.github.NeRdTheNed.ReciPic.RecipeWranglers.ShapedRecipeWrangler;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

/** WIP */
public class RecipeWranglerManager {

    public static ArrayList<IRecipe> recipeList;

    private static ArrayList<IRecipeWrangler> wranglerList = new ArrayList<IRecipeWrangler>();

    public static void addWrangler(IRecipeWrangler wrangler) {
        wranglerList.add(wrangler);
    }

    @SuppressWarnings("unchecked")
    public static void loadAllRecipes() {
        recipeList = (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList();
    }

    public static void resetWranglersToDefault() {
        wranglerList.clear();
        //wranglerList.add(new AllwaysNullWrangler()); //debug, always returns null
        wranglerList.add(new ShapedRecipeWrangler());
        //wranglerList.add(new AllwaysNullWrangler()); //debug, always returns null
    }

    public static ItemStack[] wrangleRecipe(IRecipe recipe) {
        ItemStack[] recipeOutput = null;

        for (int i = 0; ((recipeOutput == null) && (i < wranglerList.size())); i++) {
            final IRecipeWrangler wrangler = wranglerList.get(i);
            recipeOutput = wrangler.wrangleRecipeType(recipe);
        }

        return recipeOutput;
    }

}
