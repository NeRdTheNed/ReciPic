package com.github.NeRdTheNed.ReciPic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.github.NeRdTheNed.ReciPic.RecipeWranglers.AbstractRecipeWrangler;
import com.github.NeRdTheNed.ReciPic.RecipeWranglers.ShapedOreRecipeWrangler;
import com.github.NeRdTheNed.ReciPic.RecipeWranglers.ShapedRecipeWrangler;
import com.github.NeRdTheNed.ReciPic.RecipeWranglers.ShapelessOreRecipeWrangler;
import com.github.NeRdTheNed.ReciPic.RecipeWranglers.ShapelessRecipeWrangler;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

/** WIP, limited to only crafting table recipes for now. */
public class RecipeWranglerManager {

    @SuppressWarnings("unchecked")
    private static final ArrayList<IRecipe> recipeList = (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList();

    public static final HashMap<ArrayList<ItemStack>, String> oreDictMappings = new HashMap<ArrayList<ItemStack>, String>();

    private static final ArrayList<AbstractRecipeWrangler> wranglerList = new ArrayList<AbstractRecipeWrangler>();

    private static final HashMap<ItemStack, ItemStack[]> recipeInputsForItem = new HashMap<ItemStack, ItemStack[]>();

    private static boolean isWrangled = false;

    public static void addWrangler(AbstractRecipeWrangler wrangler) {
        wranglerList.add(wrangler);
    }

    private static void getOreDictionaryMappings() {
        oreDictMappings.clear();
        final String[] oreDictNames = OreDictionary.getOreNames();

        for (final String oreDictName : oreDictNames) {
            oreDictMappings.put(OreDictionary.getOres(oreDictName), oreDictName);
        }
    }

    public static HashMap<ItemStack, ItemStack[]> getWrangledRecipes() {
        if (!isWrangled) {
            wrangleAllRecipes();
        }

        return recipeInputsForItem;
    }

    public static void printRecipesToConsole() {
        final HashMap<ItemStack, ItemStack[]> recipesToPrint = getWrangledRecipes();

        for (final ItemStack stack : recipesToPrint.keySet()) {
            System.out.println("- " + stack);
            System.out.println("    - " + Arrays.asList(recipesToPrint.get(stack)));
        }

        // TODO: Possibly remove duplicates somehow? HashMultimap won't work, as it can't check for object level equality (arrays are only equal if they reference the same array)
        System.out.println("Contained total recipes for items: " + recipesToPrint.size());
    }

    public static void resetWranglersToDefault() {
        wranglerList.clear();
        //wranglerList.add(new AllwaysNullWrangler()); //debug, always returns null
        wranglerList.add(new ShapedOreRecipeWrangler());
        wranglerList.add(new ShapedRecipeWrangler());
        wranglerList.add(new ShapelessOreRecipeWrangler());
        wranglerList.add(new ShapelessRecipeWrangler());
    }

    public static void wrangleAllRecipes() {
        // Clear previous recipe inputs on run
        recipeInputsForItem.clear();

        // Make sure we have wranglers available
        if (wranglerList.isEmpty()) {
            resetWranglersToDefault();
        }

        // Make sure we have up-to-date OreDictionary mappings
        getOreDictionaryMappings();

        // Wrangle and record all recipe inputs
        for (final IRecipe recipe : recipeList) {
            final ItemStack recipeOutput = recipe.getRecipeOutput();

            if (recipeOutput != null) {
                final ItemStack[] recipeInput = wrangleRecipe(recipe);

                if (recipeInput != null) {
                    recipeInputsForItem.put(recipeOutput, recipeInput);
                }
            }
        }

        isWrangled = true;
        //printRecipesToConsole();
    }

    public static ItemStack[] wrangleRecipe(IRecipe recipe) {
        ItemStack[] recipeOutput = null;

        for (int i = 0; ((recipeOutput == null) && (i < wranglerList.size())); i++) {
            final AbstractRecipeWrangler wrangler = wranglerList.get(i);
            recipeOutput = wrangler.wrangleRecipeType(recipe);
        }

        return recipeOutput;
    }

}
