package com.github.NeRdTheNed.ReciPic.RecipeWranglers;

import com.github.NeRdTheNed.ReciPic.ReciPic;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/** Only for testing purposes TODO Cleanup, better documentation */
public class AllwaysNullWrangler extends AbstractRecipeWrangler {

    @Override
    public ItemStack[] wrangleRecipeType(IRecipe input) {
        ReciPic.ReciPicLog.debug("concern");
        return null;
    }

}
