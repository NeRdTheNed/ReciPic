package com.github.NeRdTheNed.ReciPic.RecipeWranglers;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedOreRecipeWrangler extends AbstractRecipeWrangler {

    @Override
    public ItemStack[] wrangleRecipeType(IRecipe input) {
        if (input instanceof ShapedOreRecipe) {
            int craftingInputSlot = 0;
            final ShapedOreRecipe shapedOreRecipe = (ShapedOreRecipe) input;
            final ItemStack[] outputRecipeStack = new ItemStack[9];
            // We have to use reflection to get the dimensions of a ShapedOreRecipe. Is this really necessary???
            final int reflectionWidth = ObfuscationReflectionHelper.<Integer, ShapedOreRecipe>getPrivateValue(ShapedOreRecipe.class, shapedOreRecipe, "width");
            final int reflectionHeight = ObfuscationReflectionHelper.<Integer, ShapedOreRecipe>getPrivateValue(ShapedOreRecipe.class, shapedOreRecipe, "height");

            for (int height = 0; height < reflectionHeight; height++) {
                for (int width = 0; width < reflectionWidth; width++) {
                    final ItemStack stack = shapedRecipeObjectToItemStack(shapedOreRecipe.getInput()[craftingInputSlot]);

                    if (stack != null) {
                        outputRecipeStack[width + (height * 3)] = stack;
                    }

                    craftingInputSlot++;
                }
            }

            return outputRecipeStack;
        }

        return null;
    }
}
