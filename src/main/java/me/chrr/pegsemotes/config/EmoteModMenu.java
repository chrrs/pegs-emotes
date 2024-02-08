package me.chrr.pegsemotes.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.awt.*;
import java.io.IOException;

public class EmoteModMenu implements ModMenuApi {
    private static final boolean CLOTH_CONFIG_INSTALLED = FabricLoader.getInstance().isModLoaded("cloth-config2");

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (CLOTH_CONFIG_INSTALLED) {
            return ClothConfigScreen::create;
        } else {
            return parent -> new Screen(Text.empty()) {
                @Override
                protected void init() {
                    Util.getOperatingSystem().open(Config.getPath().toFile());
                    assert client != null;
                    client.setScreen(parent);
                }
            };
        }
    }
}
