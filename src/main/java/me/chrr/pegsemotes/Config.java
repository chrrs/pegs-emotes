package me.chrr.pegsemotes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public int version = 1;
    public String[] repositories = new String[]{};

    public static Config load(Path configPath) throws IOException {
        Config config;

        if (configPath.toFile().isFile()) {
            String source = Files.readString(configPath);
            config = GSON.fromJson(source, Config.class);
        } else {
            config = new Config();
        }

        upgrade(config);
        config.save(configPath);

        return config;
    }

    private static void upgrade(Config config) {
        if (config.version == 1) {
            config.version = 2;
            config.repositories = new String[]{"https://chrrs.github.io/pegs-emotes/v2/"};
        }
    }

    public void save(Path configPath) throws IOException {
        String json = GSON.toJson(this);
        Files.writeString(configPath, json);
    }
}
