package com.github.NeRdTheNed.ReciPic.Render;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glGetTexImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class RecipeRenderer {

    protected final static Minecraft mineCraft = Minecraft.getMinecraft();
    protected final static Gui guiRef = new Gui();
    protected final static RenderItem itemRenderRef = new RenderItem();
    protected final static FontRenderer fontRendererRef = mineCraft.fontRenderer;

    protected final static int itemSize = 18;

    private final static File minecraftRecipesDir = new File(Minecraft.getMinecraft().mcDataDir, "recipes");

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

    /** TODO There is likely a much better way to do this, but I don't know OpenGL very well. References for future use:
     * - http://forum.lwjgl.org/index.php?topic=6677.0
     * - http://forum.lwjgl.org/index.php?topic=5659.0
     * - http://forum.lwjgl.org/index.php?topic=3346.0
     * - http://forum.lwjgl.org/index.php?topic=2903.0
     * - http://wiki.lwjgl.org/wiki/Render_to_Texture_with_Frame_Buffer_Objects_(FBO).html
     * - https://github.com/mattdesl/lwjgl-basics/wiki/FrameBufferObjects
     * - https://stackoverflow.com/questions/2571402/how-to-use-glortho-in-opengl
     * - https://stackoverflow.com/questions/51484274/opengl-drawing-texture-wrong
     * - https://gist.github.com/anonymous/35f9a540a82ad369e4e3
     * - A bunch of classes that reference the Framebuffer class
     * Additionally, need to implement transparency in output images (why is there a border around the images?), need to check if file exists before writing to it.
     * Also, figure out which bits of this can be removed without everything breaking and make like all of this code better.
     */
    public void drawAndSaveCraftingRecipe(ItemStack output, ItemStack[] inputStacks) {
        // TODO all of this method is inefficient and hacky, make better
        final int bitsPerPixel = 4;
        (minecraftRecipesDir).mkdir();
        final File outputFile = new File(minecraftRecipesDir + "/" + output.getUnlocalizedName() + ".png");
        final Framebuffer framebuffer = getBuffer();
        final int width = framebuffer.framebufferWidth;
        final int height = framebuffer.framebufferHeight;
        framebuffer.bindFramebuffer(false);
        // See classes referencing the Framebuffer class
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glViewport(0, 0, width, height);
        // Draw recipe image
        drawCraftingRecipe(0, 0, output, inputStacks);
        final ByteBuffer recipeImageBuffer = ByteBuffer.allocateDirect(width * height * bitsPerPixel).order(ByteOrder.nativeOrder());
        framebuffer.bindFramebufferTexture();
        glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, recipeImageBuffer);
        final BufferedImage renderedRecipeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // TODO this is inefficient
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // http://forum.lwjgl.org/index.php?topic=6677.0, https://gist.github.com/anonymous/35f9a540a82ad369e4e3
                final int i = (x + (width * y)) * bitsPerPixel;
                final int r = recipeImageBuffer.get(i) & 0xFF;
                final int g = recipeImageBuffer.get(i + 1) & 0xFF;
                final int b = recipeImageBuffer.get(i + 2) & 0xFF;
                final int a = recipeImageBuffer.get(i + 3) & 0xFF;
                renderedRecipeImage.setRGB(x, height - (y + 1), (a << 24) | (r << 16) | (g << 8) | b);
                // TODO use http://forum.lwjgl.org/index.php?topic=3346.0 instead?
            }
        }

        // TODO implement choosing output image format via Forge configuration
        try {
            ImageIO.write(renderedRecipeImage, "PNG", outputFile);
        } catch (final IOException e) {
            System.out.println("Error writing recipe image: ");
            e.printStackTrace();
        }

        // TODO see if there's a better way to do this
        framebuffer.unbindFramebuffer();
        framebuffer.unbindFramebufferTexture();
        // See classes referencing the Framebuffer class
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(false);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        // TODO this is inefficient
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        GL11.glOrtho(0.0D, scaledResolution.getScaledWidth_double(), scaledResolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glViewport(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    }

    public abstract void drawCraftingRecipe(int x, int y, ItemStack output, ItemStack[] inputStacks);

    protected abstract Framebuffer getBuffer();

}
