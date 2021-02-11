package com.github.NeRdTheNed;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public final class ReciPicConfig extends GuiConfig {

    public ReciPicConfig(GuiScreen g) {
        super(g, new ConfigElement<ConfigCategory>(ReciPic.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), ReciPic.MOD_ID, false, false, ReciPic.MOD_ID);
        entryList.listEntries.add(null);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        if (button.id == 1) {
            final String tempString = "TODO lmao";
            final int tempStringWidth = Math.max(mc.fontRenderer.getStringWidth(tempString) + 20, 100);
            button.displayString = tempString;
            button.width = tempStringWidth;
            button.xPosition = (width / 2) - (tempStringWidth / 2);
            button.enabled = false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        final String makeRecipeImagesLocalised = I18n.format("ReciPic.config.makeRecipeImages");
        final int makeRecipeImagesLocalisedWidth = Math.max(mc.fontRenderer.getStringWidth(makeRecipeImagesLocalised) + 20, 100);
        buttonList.add(new GuiButtonExt(1, (width / 2) - (makeRecipeImagesLocalisedWidth / 2), height - 60, makeRecipeImagesLocalisedWidth, 20, makeRecipeImagesLocalised));
    }
}