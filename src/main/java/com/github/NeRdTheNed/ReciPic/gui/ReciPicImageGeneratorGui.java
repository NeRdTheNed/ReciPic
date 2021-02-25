package com.github.NeRdTheNed.ReciPic.gui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.github.NeRdTheNed.ReciPic.ReciPic;
import com.github.NeRdTheNed.ReciPic.RecipeWranglerManager;
import com.github.NeRdTheNed.ReciPic.Render.CraftingRecipeImageRenderer;
import com.github.NeRdTheNed.ReciPic.Render.RecipeRenderer;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

final class ReciPicImageGeneratorGui extends GuiScreen {

    private final static int backButtonID = 0;
    private final static int generateImagesButtonID = 1;
    private final static int previewRecipeImagesButtonID = 2;

    private final static CraftingRecipeImageRenderer craftingRecipeImageRenderer = new CraftingRecipeImageRenderer();

    private final static File minecraftRecipesDir = new File(Minecraft.getMinecraft().mcDataDir, "recipes");

    private boolean areImagesGenerating = false;

    private final String guiNameLocalised;
    private final String backButtonLocalised;
    private final String cancelButtonLocalised;
    private final String generateImagesButtonLocalised;
    private final String previewRecipeImagesButtonLocalised;
    private final String generatingImagesLocalised;
    private final String recipeGenerationProgressLocalised;


    private int imageGenerationProgress = 0;
    private final int imageBatchSize = 10;

    private final GuiConfig parentScreen;

    private final HashMap<ItemStack, ItemStack[]> craftingRecipes = new HashMap<ItemStack, ItemStack[]>();

    public ReciPicImageGeneratorGui (GuiConfig parentScreen) {
        this.parentScreen = parentScreen;
        guiNameLocalised = I18n.format("ReciPic.gui.imageGeneratorGuiName");
        backButtonLocalised = I18n.format("gui.done");
        generateImagesButtonLocalised = I18n.format("ReciPic.gui.makeRecipeImages");
        cancelButtonLocalised = I18n.format("gui.cancel");
        previewRecipeImagesButtonLocalised = I18n.format("ReciPic.gui.previewRecipeImages");
        generatingImagesLocalised = I18n.format("ReciPic.gui.makingRecipeImages");
        recipeGenerationProgressLocalised = I18n.format("ReciPic.gui.recipeGenerationProgress");
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        switch (button.id) {
        case backButtonID:
            mc.displayGuiScreen(parentScreen);
            break;

        case generateImagesButtonID:
            areImagesGenerating = !areImagesGenerating;
            craftingRecipes.clear();
            craftingRecipes.putAll(RecipeWranglerManager.getWrangledRecipes());
            RecipeRenderer.itemDamageLocalised = I18n.format("ReciPic.recipeGeneration.fallbackItemName.damage");
            RecipeRenderer.itemIDLocalised = I18n.format("ReciPic.recipeGeneration.fallbackItemName.id");

            if (minecraftRecipesDir.exists()) {
                try {
                    FileUtils.deleteDirectory(minecraftRecipesDir);
                } catch (final IOException e) {
                    ReciPic.ReciPicLog.error("Could not delete previously generated recipes folder!");
                    e.printStackTrace();
                }
            }

            imageGenerationProgress = 0;
            initGui();
            break;

        case previewRecipeImagesButtonID:
            mc.displayGuiScreen(new ReciPicImageGeneratorPreviewGui(this));
            break;

        default:
            // TODO Error handling?
            break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, guiNameLocalised, width / 2, 8, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (areImagesGenerating) {
            drawCenteredString(fontRendererObj, generatingImagesLocalised, (width / 2), (height / 3), 16777215);
            drawCenteredString(fontRendererObj, recipeGenerationProgressLocalised + " " + ((int)(((float) imageGenerationProgress / (float) craftingRecipes.size()) * 100)) + "%", (width / 2), (height / 3) + (fontRendererObj.FONT_HEIGHT * 2), 16777215);

            for (int i = 0; (imageGenerationProgress < craftingRecipes.size()) && (i < imageBatchSize); i++) {
                craftingRecipeImageRenderer.drawAndSaveCraftingRecipe((ItemStack) craftingRecipes.keySet().toArray()[imageGenerationProgress], (ItemStack[]) craftingRecipes.values().toArray()[imageGenerationProgress], 4);
                imageGenerationProgress++;
            }

            if (imageGenerationProgress == craftingRecipes.size()) {
                areImagesGenerating = false;
                initGui();
            }
        }
    }



    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        buttonList.clear();
        final String generateImagesButtonTextSelection;

        if (areImagesGenerating) {
            generateImagesButtonTextSelection = cancelButtonLocalised;
        } else {
            generateImagesButtonTextSelection = generateImagesButtonLocalised;
        }

        // Calculate widths of all buttons
        final int backButtonWidth = Math.max(mc.fontRenderer.getStringWidth(backButtonLocalised) + 20, 100);
        final int generateImagesButtonWidth = Math.max(mc.fontRenderer.getStringWidth(generateImagesButtonTextSelection) + 20, 100);
        final int previewRecipeImagesButtonWidth = Math.max(mc.fontRenderer.getStringWidth(previewRecipeImagesButtonLocalised) + 20, 100);
        // Use widths to calculate button positions
        final int generateImagesButtonXPosition;
        final int previewRecipeImagesButtonXPosition;

        if (areImagesGenerating) {
            previewRecipeImagesButtonXPosition = 0;
            generateImagesButtonXPosition = (width / 2) - (generateImagesButtonWidth / 2);
        } else {
            final int buttonWidthsSummed = generateImagesButtonWidth + previewRecipeImagesButtonWidth + 10;
            previewRecipeImagesButtonXPosition = ((width / 2) - (buttonWidthsSummed / 2)) + generateImagesButtonWidth + 5;
            generateImagesButtonXPosition = (width / 2) - (buttonWidthsSummed / 2);
        }

        // Create buttons
        // Create back button
        final GuiButtonExt backButton = new GuiButtonExt(backButtonID, (width / 2) - (backButtonWidth / 2), height - 29, backButtonWidth, 20, backButtonLocalised);
        backButton.enabled = !areImagesGenerating;
        buttonList.add(backButton);
        // Create preview recipe images button
        final GuiButtonExt previewRecipeImagesButton = new GuiButtonExt(previewRecipeImagesButtonID, previewRecipeImagesButtonXPosition, height - 29 - 40, previewRecipeImagesButtonWidth, 20, previewRecipeImagesButtonLocalised);
        previewRecipeImagesButton.visible = !areImagesGenerating;
        buttonList.add(previewRecipeImagesButton);
        // Create generate images button
        final GuiButtonExt generateImagesButton = new GuiButtonExt(generateImagesButtonID, generateImagesButtonXPosition, height - 29 - 40, generateImagesButtonWidth, 20, generateImagesButtonTextSelection);
        buttonList.add(generateImagesButton);
    }

    @Override
    public void onGuiClosed() {
        parentScreen.needsRefresh = true;
        parentScreen.initGui();
    }

}
