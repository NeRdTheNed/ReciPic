package com.github.NeRdTheNed.ReciPic.RecipeWranglers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/** Only for testing purposes TODO Cleanup, better documentation */
public class AllwaysNullWrangler extends AbstractRecipeWrangler {

    @Override
    public ItemStack[] wrangleRecipeType(IRecipe input) {
        System.out.println("concern");
        return null;
    }

}
