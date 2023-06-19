package me.chrr.pegsemotes;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.chrr.pegsemotes.config.ConfigManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class EmoteModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new Screen(Text.of("")) {
            @Override
            protected void init() {
                ConfigManager.openFileInEditor(EmoteMod.getConfigFile());
                assert this.client != null;
                this.client.setScreen(screen);
            }
        };
    }
}
