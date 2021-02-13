package com.github.NeRdTheNed.ReciPic.gui;

import com.github.NeRdTheNed.ReciPic.ReciPic;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public final class ReciPicConfigGui extends GuiConfig {

    private final static int makeRecipeImagesButtonID = 0;

    public ReciPicConfigGui(GuiScreen g) {
        super(g, new ConfigElement<ConfigCategory>(ReciPic.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), ReciPic.MOD_ID, false, false, ReciPic.MOD_ID + " Config");
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        if (button.id == makeRecipeImagesButtonID) {
            entryList.saveConfigElements();
            ReciPic.syncConfig();
            mc.displayGuiScreen(new ReciPicImageGeneratorGui(this));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        final String makeRecipeImagesLocalised = I18n.format("ReciPic.config.enterMakeRecipeImages");
        final int makeRecipeImagesLocalisedWidth = Math.max(mc.fontRenderer.getStringWidth(makeRecipeImagesLocalised) + 20, 100);
        buttonList.add(new GuiButtonExt(makeRecipeImagesButtonID, (width / 2) - (makeRecipeImagesLocalisedWidth / 2), height - 60, makeRecipeImagesLocalisedWidth, 20, makeRecipeImagesLocalised));
    }
}
