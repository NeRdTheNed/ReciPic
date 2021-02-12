package com.github.NeRdTheNed.ReciPic.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class ReciPicImageGeneratorPreviewGui extends GuiScreen {

    private final static int backButtonID = 0;

    // Crafting table is 176 × 166
    private final static int craftingImageHeight = 166;
    private final static int craftingImageWidth = 176;

    final String backButtonLocalised;

    private final ReciPicImageGeneratorGui parentScreen;

    private final static String title = "ReciPic Image Generator Preview";

    public ReciPicImageGeneratorPreviewGui (ReciPicImageGeneratorGui parentScreen) {
        this.parentScreen = parentScreen;
        backButtonLocalised = I18n.format("gui.done");
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        if (button.id == backButtonID) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, title, width / 2, 8, 16777215);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/crafting_table.png"));
        final int adjustedX = (width / 2) - (craftingImageWidth / 2);
        final int adjustedY = (height / 2) - (craftingImageHeight / 2);
        drawTexturedModalRect(adjustedX, adjustedY, 0, 0, craftingImageWidth, craftingImageHeight);
        drawCenteredString(fontRendererObj, "Not implemented yet!", (width / 2), (height / 3), 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        buttonList.clear();
        // Add back button
        final int backButtonWidth = Math.max(mc.fontRenderer.getStringWidth(backButtonLocalised) + 20, 100);
        final GuiButtonExt backButton = new GuiButtonExt(backButtonID, (width / 2) - (backButtonWidth / 2), height - 29, backButtonWidth, 20, backButtonLocalised);
        buttonList.add(backButton);
    }

    @Override
    public void onGuiClosed() {
        parentScreen.initGui();
    }

}
