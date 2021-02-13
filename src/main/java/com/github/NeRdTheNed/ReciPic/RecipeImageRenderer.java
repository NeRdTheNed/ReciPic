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
    private final static ResourceLocation craftingTableResourceLocation = new ResourceLocation("textures/gui/container/crafting_table.png");

    public static void drawRecipe(int x, int y, ItemStack[] recipeStacks) {
        // Just in case
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // Draw crafting table background
        mineCraft.getTextureManager().bindTexture(craftingTableResourceLocation);
        guiRef.drawTexturedModalRect(x, y, 0, 0, craftingImageWidth, craftingImageHeight);
        // Draw a grey box over the "player inventory slots" portion of the image
        Gui.drawRect(x + 7, y + 83, x + 169, y + 159, 0xFFC6C6C6);
        // Draw items on top of crafting grid
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        for (int recipeSlotHeight = 0; recipeSlotHeight < 3; recipeSlotHeight++) {
            for (int recipeSlotWidth = 0; recipeSlotWidth < 3; recipeSlotWidth++) {
                final ItemStack stack = recipeStacks[recipeSlotWidth + (recipeSlotHeight * 3)];

                if (stack != null) {
                    // Calculate item location relative to the slot in the crafting table
                    final int itemX = x + (30 + (recipeSlotWidth * 18));
                    final int itemY = y + (17 + (recipeSlotHeight * 18));
                    itemRenderRef.renderItemAndEffectIntoGUI(fontRendererRef, mineCraft.getTextureManager(), stack, itemX, itemY);
                    itemRenderRef.renderItemOverlayIntoGUI(fontRendererRef, mineCraft.getTextureManager(), stack, itemX, itemY);
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
