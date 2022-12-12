package me.chrrrs.pegsemotes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final Logger LOGGER = LogManager.getLogger("pegs-emotes.Config");

    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setLenient()
            .setPrettyPrinting()
            .create();

    public List<String> repositories = new ArrayList<>() {{
        add("https://chrrs.github.io/PegsEmotes/");
    }};

    public static Config loadConfigOrCreateDefault() {
        try {
            Config config;
            File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "pegs-emotes.json");

            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    config = GSON.fromJson(reader, Config.class);
                }
            } else {
                config = new Config();

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(GSON.toJson(config));
                }
            }

            return config;
        } catch (IOException e) {
            LOGGER.error("failed to load config", e);
            return new Config();
        }
    }
}
