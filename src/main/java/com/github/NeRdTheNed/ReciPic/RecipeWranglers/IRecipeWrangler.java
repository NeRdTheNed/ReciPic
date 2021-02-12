package com.github.NeRdTheNed.ReciPic.RecipeWranglers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/** TODO A better way of doing this, probably with generics? */
public interface IRecipeWrangler {
    /** Returns null if this IRecipeWrangler does not handle this type of recipe. Otherwise, returns an ItemStack containing the "formatted" version of this recipe. TODO Better documentation. */
    ItemStack[] wrangleRecipeType(IRecipe input);
}
