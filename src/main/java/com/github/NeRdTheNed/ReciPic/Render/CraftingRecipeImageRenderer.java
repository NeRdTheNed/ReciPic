package com.github.NeRdTheNed.ReciPic.Render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.Dimension;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class CraftingRecipeImageRenderer extends RecipeRenderer {

    // The crafting table texture is 176 × 166
    public final static int craftingImageHeight = 166;
    public final static int craftingImageWidth = 176;

    private final static Dimension craftingImageDimension = new Dimension(craftingImageWidth, craftingImageHeight);

    // TODO reorganize & document
    private final static int sideMargin = 8;
    private final static int updownMargin = 84 - itemSize;

    private final static ResourceLocation craftingTableResourceLocation = new ResourceLocation("textures/gui/container/crafting_table.png");

    @Override
    public void drawRecipeBackgroud(int x, int y) {
        // Draw crafting table background
        drawBackgroundImage(x, y, craftingImageWidth, craftingImageHeight, craftingTableResourceLocation);
        // Draw a grey box over the "player inventory slots" portion of the image
        Gui.drawRect(x + 7, y + 83, x + 169, y + 159, 0xFFC6C6C6);
    }

    @Override
    public void drawRecipeOverlay(int x, int y, ItemStack output, ItemStack[] inputStacks) {
        // Draw items on top of crafting grid
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        for (int recipeSlotHeight = 0; recipeSlotHeight < 3; recipeSlotHeight++) {
            for (int recipeSlotWidth = 0; recipeSlotWidth < 3; recipeSlotWidth++) {
                final ItemStack stack = inputStacks[recipeSlotWidth + (recipeSlotHeight * 3)];
                // Calculate item location relative to the slot in the crafting table and draw
                drawItemStackAtLocation(x + (30 + (recipeSlotWidth * 18)), y + (17 + (recipeSlotHeight * 18)), stack);
            }
        }

        // Draw recipe output
        drawItemStackAtLocation(x + 124, y + 35, output);
        // Lighting is good for items, not so good for GUIs
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        // Draw output item name (TODO Decide on colour to use (0xFF404040?))
        guiRef.drawCenteredString(fontRendererRef, output.getDisplayName(), x + (craftingImageWidth / 2), y + 6, 0xFFFFFFFF);
        // Draw ingredient names
        sortAndDrawItemsAndText(x + sideMargin, y + updownMargin, craftingImageWidth - (sideMargin * 2), inputStacks);
    }

    @Override
    protected Dimension getDimension() {
        // TODO Auto-generated method stub
        return craftingImageDimension;
    }

}
