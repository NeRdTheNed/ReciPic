package com.github.NeRdTheNed.ReciPic.gui;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

final class ReciPicImageGeneratorGui extends GuiScreen {

    private final static int backButtonID = 0;
    private final static int generateImagesButtonID = 1;
    private final static int previewRecipeImagesButtonID = 2;

    private final static String title = "ReciPic Image Generator";

    private boolean areImagesGenerating = false;

    final String backButtonLocalised;
    final String cancelButtonLocalised;
    final String generateImagesButtonLocalised;
    final String previewRecipeImagesButtonLocalised;

    private int imageGenerationProgress = 0;

    private final GuiConfig parentScreen;

    public ReciPicImageGeneratorGui (GuiConfig parentScreen) {
        this.parentScreen = parentScreen;
        backButtonLocalised = I18n.format("gui.done");
        generateImagesButtonLocalised = I18n.format("ReciPic.config.makeRecipeImages");
        cancelButtonLocalised = I18n.format("gui.cancel");
        previewRecipeImagesButtonLocalised = I18n.format("ReciPic.config.previewRecipeImages");
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
        drawCenteredString(fontRendererObj, title, width / 2, 8, 16777215);

        if (areImagesGenerating) {
            drawCenteredString(fontRendererObj, "Not implemented yet!", (width / 2), (height / 3), 16777215);
            drawCenteredString(fontRendererObj, ("Mod development progress: " + " " + imageGenerationProgress + "%"), (width / 2), (height / 3) + (fontRendererObj.FONT_HEIGHT * 2), 16777215);
            imageGenerationProgress++;

            if (imageGenerationProgress > 99) {
                imageGenerationProgress = -100;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
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
        if (!areImagesGenerating) {
            final GuiButtonExt previewRecipeImagesButton = new GuiButtonExt(previewRecipeImagesButtonID, previewRecipeImagesButtonXPosition, height - 29 - 40, previewRecipeImagesButtonWidth, 20, previewRecipeImagesButtonLocalised);
            buttonList.add(previewRecipeImagesButton);
        }

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
