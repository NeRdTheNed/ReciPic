package com.github.NeRdTheNed.ReciPic;

import java.util.Set;

import com.github.NeRdTheNed.ReciPic.gui.ReciPicConfigGui;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = ReciPic.MOD_ID, guiFactory = "com.github.NeRdTheNed.ReciPic.ReciPic")
public final class ReciPic implements IModGuiFactory {

    public static Configuration config;

    public static boolean displaySingleOreDictEntries;

    public static final String MOD_ID = "ReciPic";

    public static final Item wildcardItem = new WildcardItem();

    private static void syncConfig() {
        displaySingleOreDictEntries = config.get(Configuration.CATEGORY_GENERAL, "ReciPic.displaySingleOreDictEntries", false).getBoolean();
        config.save();
    }

    @SubscribeEvent
    public void confChange(OnConfigChangedEvent event) {
        if (event.modID.equals(MOD_ID)) {
            syncConfig();
        }
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement a) {
        return null;
    }

    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        GameRegistry.registerItem(wildcardItem, "WildcardItem");
    }

    @Override
    public void initialize(Minecraft a) {
        /* This space left intentionally blank */
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ReciPicConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
}