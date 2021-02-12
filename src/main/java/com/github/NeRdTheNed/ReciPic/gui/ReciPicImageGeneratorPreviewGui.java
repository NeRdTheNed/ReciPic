package com.github.NeRdTheNed.ReciPic.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.github.NeRdTheNed.ReciPic.RecipeWranglerManager;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;

public class ReciPicImageGeneratorPreviewGui extends GuiScreen {

    private final static int backButtonID = 0;

    // Crafting table is 176 × 166
    private final static int craftingImageHeight = 166;
    private final static int craftingImageWidth = 176;

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
        final ShapedRecipes testRecipe = new ShapedRecipes(3, 3, new ItemStack[] {new ItemStack(Blocks.planks), new ItemStack(Blocks.planks), null, null, new ItemStack(Items.stick), new ItemStack(Blocks.planks), null, new ItemStack(Items.stick), null}, null);
        recipes.add(RecipeWranglerManager.wrangleRecipe(testRecipe));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        if (button.id == backButtonID) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    /** TODO Move this to a separate class */
    private void drawRecipePreview(int x, int y, ItemStack[] stack) {
        // Just in case
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // Draw crafting table background
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/crafting_table.png"));
        drawTexturedModalRect(x, y, 0, 0, craftingImageWidth, craftingImageHeight);
        // Draw a grey box over the "player inventory slots" portion of the image
        drawRect(x + 7, y + 83, x + 169, y + 159, 0xFFC6C6C6);
        // Draw items on top of crafting grid
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        for (int recipeHeight = 0; recipeHeight < 3; recipeHeight++) {
            for (int recipeWidth = 0; recipeWidth < 3; recipeWidth++) {
                itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), recipes.get(0)[recipeWidth + (recipeHeight * 3)], x + (30 + (recipeWidth * 18)), y + (17 + (recipeHeight * 18)));
            }
        }

        // TODO Draw crafting output, draw names of items & number of items
        // Ligting is good for items, not so good for GUIs
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        // Just in case
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, title, width / 2, 8, 16777215);
        final int adjustedX = (width / 2) - (craftingImageWidth / 2);
        final int adjustedY = (height / 2) - (craftingImageHeight / 2);
        // For now, only draw a single "recipe".
        drawRecipePreview(adjustedX, adjustedY, recipes.get(0));
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
