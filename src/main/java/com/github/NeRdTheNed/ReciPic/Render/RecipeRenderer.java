package com.github.NeRdTheNed.ReciPic.Render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class RecipeRenderer {

    protected final static Minecraft mineCraft = Minecraft.getMinecraft();
    protected final static Gui guiRef = new Gui();
    protected final static RenderItem itemRenderRef = new RenderItem();
    protected final static FontRenderer fontRendererRef = mineCraft.fontRenderer;

    protected final static int itemSize = 18;

    protected static void drawBackgroundImage(int x, int y, int width, int height, ResourceLocation image) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mineCraft.getTextureManager().bindTexture(image);
        guiRef.drawTexturedModalRect(x, y, 0, 0, width, height);
    }

    private static void drawItemsAndText(int x, int y, int width, ItemStack[] stacks) {
        for (final ItemStack stack : stacks) {
            if (stack != null) {
                drawItemStackAtLocationWithGLBoilerplate(x, y + (itemSize / 4), stack);
                y += drawStringWrapped(x + itemSize, y, width - itemSize, stack.getDisplayName()) + fontRendererRef.FONT_HEIGHT;
            }
        }
    }

    protected static void drawItemStackAtLocation(int x, int y, ItemStack stack) {
        if (stack != null) {
            itemRenderRef.renderItemAndEffectIntoGUI(fontRendererRef, mineCraft.getTextureManager(), stack, x, y);
            itemRenderRef.renderItemOverlayIntoGUI(fontRendererRef, mineCraft.getTextureManager(), stack, x, y);
        }
    }

    protected static void drawItemStackAtLocationWithGLBoilerplate(int x, int y, ItemStack stack) {
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        drawItemStackAtLocation(x, y, stack);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    protected static int drawStringWrapped(int x, int y, int width, String string) {
        @SuppressWarnings("unchecked")
        final List<String> wrappedStrings = fontRendererRef.listFormattedStringToWidth(string, width);
        int amountWrapped = 0;

        for (final String wrappedLine : wrappedStrings) {
            amountWrapped += fontRendererRef.FONT_HEIGHT;
            fontRendererRef.drawString(wrappedLine, x, y + amountWrapped, 0x404040);
        }

        return amountWrapped;
    }

    /** TODO This is pure garbage, fix later. */
    protected static void sortAndDrawItemsAndText(int x, int y, int width, ItemStack[] stacks) {
        final HashMap<String, ItemStack> items = new HashMap<String, ItemStack>();

        for (final ItemStack stack : stacks) {
            if ((stack != null) && !items.containsKey(stack.getDisplayName())) {
                items.put(stack.getDisplayName(), stack);
            } /* else {

            	ItemStack thisProbablyWillNotBeAnIssue = items.get(stack.getDisplayName());
            	if (!ItemStack.areItemStacksEqual(stack, thisProbablyWillNotBeAnIssue)) {
            		// ???
            	}
            } */
        }

        final ArrayList<String> itemNames = new ArrayList<String>(items.keySet());
        Collections.sort(itemNames, String.CASE_INSENSITIVE_ORDER);
        final ItemStack[] sortedStacks = new ItemStack[itemNames.size()];

        for (int i = 0; i < itemNames.size(); i++) {
            sortedStacks[i] = items.get(itemNames.get(i));
        }

        drawItemsAndText(x, y, width, sortedStacks);
    }

}
