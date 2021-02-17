package com.github.NeRdTheNed.ReciPic.gui;

import static com.github.NeRdTheNed.ReciPic.Render.CraftingRecipeImageRenderer.craftingImageHeight;
import static com.github.NeRdTheNed.ReciPic.Render.CraftingRecipeImageRenderer.craftingImageWidth;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.github.NeRdTheNed.ReciPic.RecipeWranglerManager;
import com.github.NeRdTheNed.ReciPic.Render.CraftingRecipeImageRenderer;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
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

    private final static CraftingRecipeImageRenderer craftingRecipeImageRenderer = new CraftingRecipeImageRenderer();

    private final static int backButtonID = 0;
    private final static int leftButtonID = 1;
    private final static int rightButtonID = 2;

    private final static String title = "ReciPic Image Generator Preview";

    final String backButtonLocalised;

    private final ReciPicImageGeneratorGui parentScreen;

    private final HashMap<ItemStack, ItemStack[]> testCraftingRecipes = new HashMap<ItemStack, ItemStack[]>();
    private int recipeIndex = 0;

    public ReciPicImageGeneratorPreviewGui (ReciPicImageGeneratorGui parentScreen) {
        this.parentScreen = parentScreen;
        backButtonLocalised = I18n.format("gui.done");
        // Temporary testing
        makeTestRecipes();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        switch (button.id) {
        case backButtonID:
            mc.displayGuiScreen(parentScreen);
            break;

        case leftButtonID:
            if (recipeIndex > 0) {
                recipeIndex--;
            }

            initGui();
            break;

        case rightButtonID:
            if (recipeIndex < (testCraftingRecipes.size() - 1)) {
                recipeIndex++;
            }

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
        // TODO This is highly dubious
        craftingRecipeImageRenderer.drawCraftingRecipe(adjustedX, adjustedY, (ItemStack) testCraftingRecipes.keySet().toArray()[recipeIndex], (ItemStack[]) testCraftingRecipes.values().toArray()[recipeIndex]);
        drawCenteredString(fontRendererObj, "Not fully implemented yet!", (width / 2), (craftingImageHeight + (fontRendererObj.FONT_HEIGHT / 2)), 16777215);
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
        buttonLeft.visible = recipeIndex > 0;
        buttonRight.visible = recipeIndex < (testCraftingRecipes.size() - 1);
        buttonList.add(buttonLeft);
        buttonList.add(buttonRight);
        // Temporary testing
        craftingRecipeImageRenderer.drawAndSaveCraftingRecipe(craftingImageWidth, craftingImageHeight, (ItemStack) testCraftingRecipes.keySet().toArray()[recipeIndex], (ItemStack[]) testCraftingRecipes.values().toArray()[recipeIndex]);
    }

    /** TODO Create set of recipes to use for testing */
    private void makeTestRecipes() {
        /*final ShapedRecipes testRecipe = new ShapedRecipes(3, 3, new ItemStack[] {new ItemStack(Blocks.planks), new ItemStack(Blocks.planks), null, null, new ItemStack(Items.stick, 2), new ItemStack(Blocks.planks, 2), null, new ItemStack(Items.stick, 3), null}, new ItemStack(Items.wooden_pickaxe, 2));
        testCraftingRecipes.put(testRecipe.getRecipeOutput(), RecipeWranglerManager.wrangleRecipe(testRecipe));
        final ShapedRecipes testRecipe2 = new ShapedRecipes(3, 3, new ItemStack[] {new ItemStack(Blocks.iron_block), new ItemStack(Blocks.iron_block), null, null, new ItemStack(Items.stick, 2), new ItemStack(Blocks.iron_block, 2), null, new ItemStack(Items.stick, 3), null}, new ItemStack(Items.iron_pickaxe, 2));
        testCraftingRecipes.put(testRecipe2.getRecipeOutput(), RecipeWranglerManager.wrangleRecipe(testRecipe2));
        final ShapedRecipes testRecipe3 = new ShapedRecipes(2, 2, new ItemStack[] {new ItemStack(Items.diamond), new ItemStack(Blocks.dirt), new ItemStack(Blocks.dirt), new ItemStack(Items.diamond)}, new ItemStack(Blocks.diamond_ore));
        testCraftingRecipes.put(testRecipe3.getRecipeOutput(), RecipeWranglerManager.wrangleRecipe(testRecipe3));
        final ShapedRecipes testRecipe4 = new ShapedRecipes(2, 3, new ItemStack[] {new ItemStack(Blocks.iron_block), new ItemStack(Blocks.iron_block), new ItemStack(Blocks.iron_block), new ItemStack(Blocks.iron_block), new ItemStack(Blocks.iron_block), new ItemStack(Blocks.iron_block)}, new ItemStack(Items.iron_door, 9));
        testCraftingRecipes.put(testRecipe4.getRecipeOutput(), RecipeWranglerManager.wrangleRecipe(testRecipe4));
        final ShapedRecipes testRecipe5 = new ShapedRecipes(3, 1, new ItemStack[] {new ItemStack(Blocks.log), new ItemStack(Blocks.log2), new ItemStack(Blocks.log, 1, 2)}, new ItemStack(Blocks.wooden_slab, 24));
        testCraftingRecipes.put(testRecipe5.getRecipeOutput(), RecipeWranglerManager.wrangleRecipe(testRecipe5));
        final ArrayList<ItemStack> testRecipe6Input = new ArrayList<ItemStack>();
        testRecipe6Input.add(new ItemStack(Items.dye, 1, 11));
        testRecipe6Input.add(new ItemStack(Items.apple));
        testRecipe6Input.add(new ItemStack(Items.dye, 1, 11));
        final ShapelessRecipes testRecipe6 = new ShapelessRecipes(new ItemStack(Items.golden_apple), testRecipe6Input);
        testCraftingRecipes.put(testRecipe6.getRecipeOutput(), RecipeWranglerManager.wrangleRecipe(testRecipe6));*/
        testCraftingRecipes.putAll(RecipeWranglerManager.getWrangledRecipes());
    }

    @Override
    public void onGuiClosed() {
        parentScreen.initGui();
    }

}
