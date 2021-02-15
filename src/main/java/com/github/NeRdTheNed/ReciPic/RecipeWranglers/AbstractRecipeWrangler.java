package com.github.NeRdTheNed.ReciPic.RecipeWranglers;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.NeRdTheNed.ReciPic.ReciPic;
import com.github.NeRdTheNed.ReciPic.RecipeWranglerManager;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/** TODO A better way of doing this, probably with generics? */
public abstract class AbstractRecipeWrangler {
    private final static HashMap<ArrayList<ItemStack>, String> oreDictMappings = RecipeWranglerManager.oreDictMappings;

    protected ItemStack getOreDictionaryEntryWithFallback(ArrayList<ItemStack> itemList) {
        final String oreDictName = getOreDictionaryName(itemList);

        if (oreDictName != null) {
            final ItemStack oreDictItem;
            final String appendToName;

            // Display the item representing a single OreDictionary entry as the single entry
            if (itemList.size() == 1) {
                // Bail out early if we aren't displaying single OreDictionary entries
                if (!ReciPic.displaySingleOreDictEntries) {
                    return itemList.get(0);
                }

                oreDictItem = itemList.get(0).copy();
                appendToName = oreDictItem.getDisplayName();
            } else {
                oreDictItem = new ItemStack(ReciPic.wildcardItem);
                appendToName = oreDictName;
            }

            oreDictItem.setStackDisplayName("Any type of " + appendToName);
            return oreDictItem;
        } else {
            return itemList.get(0);
        }
    }

    protected String getOreDictionaryName(ArrayList<ItemStack> itemList) {
        return oreDictMappings.get(itemList);
    }

    protected ItemStack shapedRecipeObjectToItemStack(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof ItemStack) {
            return (ItemStack) object;
        }

        if (object instanceof ArrayList) {
            ItemStack stack;
            @SuppressWarnings("unchecked")
            final ArrayList<ItemStack> asArrayList = (ArrayList<ItemStack>)object;
            stack = getOreDictionaryEntryWithFallback(asArrayList);

            if (stack != null) {
                return stack;
            }
        }

        return null;
        // TODO Better error handling
    }

    /** Returns null if this IRecipeWrangler does not handle this type of recipe. Otherwise, returns an ItemStack containing the "formatted" version of this recipe. TODO Better documentation. */
    public abstract ItemStack[] wrangleRecipeType(IRecipe input);
}
