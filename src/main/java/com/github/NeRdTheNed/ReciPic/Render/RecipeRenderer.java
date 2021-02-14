package com.github.NeRdTheNed.ReciPic.Render;

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

    protected static void drawBackgroundImage(int x, int y, int width, int height, ResourceLocation image) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mineCraft.getTextureManager().bindTexture(image);
        guiRef.drawTexturedModalRect(x, y, 0, 0, width, height);
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
        drawItemStackAtLocation(x, y, stack);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    }

}
