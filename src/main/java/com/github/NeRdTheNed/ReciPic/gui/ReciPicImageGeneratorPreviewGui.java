package com.github.NeRdTheNed.ReciPic.gui;

import static com.github.NeRdTheNed.ReciPic.RecipeImageRenderer.craftingImageHeight;
import static com.github.NeRdTheNed.ReciPic.RecipeImageRenderer.craftingImageWidth;

import java.util.ArrayList;

import com.github.NeRdTheNed.ReciPic.RecipeImageRenderer;
import com.github.NeRdTheNed.ReciPic.RecipeWranglerManager;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class ReciPicImageGeneratorPreviewGui extends GuiScreen {

    private final static int backButtonID = 0;

    private final static String title = "ReciPic Image Generator Preview";

    final String backButtonLocalised;

    private final ReciPicImageGeneratorGui parentScreen;

    private final ArrayList<ItemStack[]> recipes;

    public ReciPicImageGeneratorPreviewGui (ReciPicImageGeneratorGui parentScreen) {
        this.parentScreen = parentScreen;
        backButtonLocalised = I18n.format("gui.done");
        // Temporary testing
        RecipeWranglerManager.loadAllRecipes();
        RecipeWranglerManager.resetWranglersToDefault();
        recipes = new ArrayList<ItemStack[]>();
        final ShapedRecipes testRecipe = new ShapedRecipes(3, 3, new ItemStack[] {new ItemStack(Blocks.planks), new ItemStack(Blocks.planks), null, null, new ItemStack(Items.stick, 2), new ItemStack(Blocks.planks, 2), null, new ItemStack(Items.stick, 3), null}, null);
        recipes.add(RecipeWranglerManager.wrangleRecipe(testRecipe));
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
        final int adjustedX = (width / 2) - (craftingImageWidth / 2);
        final int adjustedY = (height / 2) - (craftingImageHeight / 2);
        // For now, only draw a single "recipe".
        RecipeImageRenderer.drawRecipe(adjustedX, adjustedY, recipes.get(0));
        drawCenteredString(fontRendererObj, "Not fully implemented yet!", (width / 2), (height / 3), 16777215);
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
