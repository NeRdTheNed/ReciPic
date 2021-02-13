package com.github.NeRdTheNed.ReciPic.gui;

import static com.github.NeRdTheNed.ReciPic.RecipeImageRenderer.craftingImageHeight;
import static com.github.NeRdTheNed.ReciPic.RecipeImageRenderer.craftingImageWidth;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.github.NeRdTheNed.ReciPic.RecipeImageRenderer;
import com.github.NeRdTheNed.ReciPic.RecipeWranglerManager;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;

public class ReciPicImageGeneratorPreviewGui extends GuiScreen {

    private static final class ArrowButton extends GuiButton {

        public final static int arrowButtonTextureWidth = 14;
        public final static int arrowButtonTextureHeight = 22;

        private final static int rightArrowU = 10;
        private final static int leftArrowU = rightArrowU + arrowButtonTextureWidth + 10;
        private final static int normalArrowV = 5;
        private final static int highlightedArrowV = normalArrowV + arrowButtonTextureHeight + 10;

        private final static ResourceLocation arrowTexturesLocation = new ResourceLocation("textures/gui/resource_packs.png");

        private final boolean isRightButton;

        public ArrowButton(int id, int x, int y, boolean isRightButton) {
            super(id, x, y, arrowButtonTextureWidth, arrowButtonTextureHeight, "");
            this.isRightButton = isRightButton;
        }

        @Override
        public void drawButton(Minecraft minecraft, int x, int y) {
            if (visible) {
                minecraft.getTextureManager().bindTexture(arrowTexturesLocation);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                final int textureV;
                final int textureU;
                final boolean isSelected = (x >= xPosition) && (y >= yPosition) && (x < (xPosition + width)) && (y < (yPosition + height));

                if (isSelected) {
                    textureV = highlightedArrowV;
                } else {
                    textureV = normalArrowV;
                }

                if (isRightButton) {
                    textureU = rightArrowU;
                } else {
                    textureU = leftArrowU;
                }

                drawTexturedModalRect(xPosition, yPosition, textureU, textureV, width, height);
            }
        }
    }

    private final static int backButtonID = 0;
    private final static int leftButtonID = 1;
    private final static int rightButtonID = 2;

    private final static String title = "ReciPic Image Generator Preview";

    private static ArrayList<ItemStack[]> getTestRecipes() {
        final ArrayList<ItemStack[]> recipesToReturn = new ArrayList<ItemStack[]>();
        final ShapedRecipes testRecipe = new ShapedRecipes(3, 3, new ItemStack[] {new ItemStack(Blocks.planks), new ItemStack(Blocks.planks), null, null, new ItemStack(Items.stick, 2), new ItemStack(Blocks.planks, 2), null, new ItemStack(Items.stick, 3), null}, null);
        recipesToReturn.add(RecipeWranglerManager.wrangleRecipe(testRecipe));
        RecipeWranglerManager.wrangleAllRecipes();
        return recipesToReturn;
    }

    private boolean isLeftButtonActive = true;
    private boolean isRightButtonActive = false;

    final String backButtonLocalised;

    private final ReciPicImageGeneratorGui parentScreen;

    private final ArrayList<ItemStack[]> testRecipes;

    public ReciPicImageGeneratorPreviewGui (ReciPicImageGeneratorGui parentScreen) {
        this.parentScreen = parentScreen;
        backButtonLocalised = I18n.format("gui.done");
        // Temporary testing
        testRecipes = getTestRecipes();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        switch (button.id) {
        case backButtonID:
            mc.displayGuiScreen(parentScreen);
            break;

        case leftButtonID:
            // Temporary testing TODO Actually implement recipe selection
            isLeftButtonActive = false;
            isRightButtonActive = true;
            initGui();
            break;

        case rightButtonID:
            // Temporary testing TODO Actually implement recipe selection
            isLeftButtonActive = true;
            isRightButtonActive = false;
            initGui();
            break;

        default:
            // TODO Error handling?
            break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, title, width / 2, 8, 16777215);
        final int adjustedX = (width / 2) - (craftingImageWidth / 2);
        final int adjustedY = (height / 2) - (craftingImageHeight / 2);
        // For now, only draw a single "recipe".
        RecipeImageRenderer.drawRecipe(adjustedX, adjustedY, testRecipes.get(0));
        drawCenteredString(fontRendererObj, "Not fully implemented yet!", (width / 2), adjustedY + (fontRendererObj.FONT_HEIGHT / 2), 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        buttonList.clear();
        // Calculate button positions
        final int backButtonWidth = Math.max(mc.fontRenderer.getStringWidth(backButtonLocalised) + 20, 100);
        final int leftArrowX = (width / 2) - (craftingImageWidth / 2) - (ArrowButton.arrowButtonTextureWidth * 2);
        final int rightArrowX = (width / 2) + (craftingImageWidth / 2) + ArrowButton.arrowButtonTextureWidth;
        final int arrowY = (height / 2);
        // Add back button
        final GuiButtonExt backButton = new GuiButtonExt(backButtonID, (width / 2) - (backButtonWidth / 2), height - 29, backButtonWidth, 20, backButtonLocalised);
        buttonList.add(backButton);
        // Add left & right buttons
        final ArrowButton buttonLeft = new ArrowButton(leftButtonID, leftArrowX, arrowY, false);
        final ArrowButton buttonRight = new ArrowButton(rightButtonID, rightArrowX, arrowY, true);
        buttonLeft.visible = isLeftButtonActive;
        buttonRight.visible = isRightButtonActive;
        buttonList.add(buttonLeft);
        buttonList.add(buttonRight);
    }

    @Override
    public void onGuiClosed() {
        parentScreen.initGui();
    }

}
