package me.chrr.pegsemotes.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final int VERSION = 3;
    public static final Config DEFAULT = new Config();

    private static Config INSTANCE;

    public int version;
    public List<String> repositories = List.of("https://emotes.chrr.me/v2/");
    public boolean emoteShadow = true;

    private void upgrade() throws IOException {
        if (this.version == VERSION) {
            return;
        }

        if (this.version <= 1) {
            this.repositories = DEFAULT.repositories;
        }

        if (this.version <= 2) {
            this.emoteShadow = DEFAULT.emoteShadow;
        }

        this.version = VERSION;
        this.save();
    }

    public void save() throws IOException {
        String json = GSON.toJson(this);
        Files.writeString(getPath(), json);
    }

    public static Path getPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("pegs-emotes.json");
    }

    public static void loadInstance() throws IOException {
        Path path = getPath();
        if (path.toFile().isFile()) {
            String source = Files.readString(path);
            INSTANCE = GSON.fromJson(source, Config.class);
            INSTANCE.upgrade();
        } else {
            INSTANCE = DEFAULT;
            INSTANCE.save();
        }
    }

    public static Config getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("Config not loaded yet");
        }

        return INSTANCE;
    }
}
