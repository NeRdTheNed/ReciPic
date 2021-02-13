package com.github.NeRdTheNed.ReciPic;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class RecipeImageRenderer {

    // Crafting table is 176 × 166
    public final static int craftingImageHeight = 166;
    public final static int craftingImageWidth = 176;

    private final static Minecraft mineCraft = Minecraft.getMinecraft();
    private final static Gui guiRef = new Gui();
    private final static RenderItem itemRenderRef = new RenderItem();
    private final static FontRenderer fontRendererRef = mineCraft.fontRenderer;

    public static void drawRecipe(int x, int y, ItemStack[] recipeStacks) {
        // Just in case
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // Draw crafting table background
        mineCraft.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/crafting_table.png"));
        guiRef.drawTexturedModalRect(x, y, 0, 0, craftingImageWidth, craftingImageHeight);
        // Draw a grey box over the "player inventory slots" portion of the image
        Gui.drawRect(x + 7, y + 83, x + 169, y + 159, 0xFFC6C6C6);
        // Draw items on top of crafting grid
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        for (int recipeHeight = 0; recipeHeight < 3; recipeHeight++) {
            for (int recipeWidth = 0; recipeWidth < 3; recipeWidth++) {
                final ItemStack stack = recipeStacks[recipeWidth + (recipeHeight * 3)];

                if (stack != null) {
                    itemRenderRef.renderItemAndEffectIntoGUI(fontRendererRef, mineCraft.getTextureManager(), stack, x + (30 + (recipeWidth * 18)), y + (17 + (recipeHeight * 18)));
                    String renderString = "";

                    if (stack.stackSize > 1) {
                        renderString += stack.stackSize;
                    }

                    itemRenderRef.renderItemOverlayIntoGUI(fontRendererRef, mineCraft.getTextureManager(), stack, x + (30 + (recipeWidth * 18)), y + (17 + (recipeHeight * 18)), renderString);
                }
            }
        }

        // TODO Draw crafting output, draw names of items & number of items
        // Lighting is good for items, not so good for GUIs
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        // Just in case
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
