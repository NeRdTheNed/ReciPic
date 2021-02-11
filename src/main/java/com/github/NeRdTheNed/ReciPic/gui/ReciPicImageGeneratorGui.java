package com.github.NeRdTheNed.ReciPic.gui;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

final class ReciPicImageGeneratorGui extends GuiScreen {

    private final static int backButtonID = 0;
    private final static int generateImagesButtonID = 1;

    private boolean areImagesGenerating = false;

    final String backButtonLocalised;
    final String cancelButtonLocalised;
    final String generateImagesButtonLocalised;

    private int imageGenerationProgress = 0;

    private final GuiConfig parentScreen;

    private final String title = "ReciPic Image Generator";

    public ReciPicImageGeneratorGui (GuiConfig parentScreen) {
        this.parentScreen = parentScreen;
        backButtonLocalised = I18n.format("gui.done");
        generateImagesButtonLocalised = I18n.format("ReciPic.config.makeRecipeImages");
        cancelButtonLocalised = I18n.format("gui.cancel");
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        if (button.id == backButtonID) {
            mc.displayGuiScreen(parentScreen);
        } else if (button.id == generateImagesButtonID) {
            areImagesGenerating = !areImagesGenerating;
            initGui();
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
        // Add back button
        final int backButtonWidth = Math.max(mc.fontRenderer.getStringWidth(backButtonLocalised) + 20, 100);
        final GuiButtonExt backButton = new GuiButtonExt(backButtonID, (width / 2) - (backButtonWidth / 2), height - 29, backButtonWidth, 20, backButtonLocalised);
        backButton.enabled = !areImagesGenerating;
        buttonList.add(backButton);
        // Add generate images button
        final int generateImagesButtonWidth = Math.max(mc.fontRenderer.getStringWidth(generateImagesButtonLocalised) + 20, 100);
        final String generateImagesButtonTextSelection;

        if (areImagesGenerating) {
            generateImagesButtonTextSelection = cancelButtonLocalised;
        } else {
            generateImagesButtonTextSelection = generateImagesButtonLocalised;
        }

        final GuiButtonExt generateImagesButton = new GuiButtonExt(generateImagesButtonID, (width / 2) - (generateImagesButtonWidth / 2), height - 29 - 40, generateImagesButtonWidth, 20, generateImagesButtonTextSelection);
        buttonList.add(generateImagesButton);
    }

    @Override
    public void onGuiClosed() {
        parentScreen.needsRefresh = true;
        parentScreen.initGui();
    }

}
