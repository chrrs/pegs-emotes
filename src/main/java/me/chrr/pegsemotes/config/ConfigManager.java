package me.chrr.pegsemotes.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class ConfigManager {
    private static final Logger LOGGER = LogManager.getLogger("pegs-emotes.ConfigManager");

    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setLenient()
            .setPrettyPrinting()
            .create();

    public static Config read(File file) {
        if (!file.exists()) {
            Config config = new Config();
            write(file, config);
            return config;
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                return GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                LOGGER.error("failed to read config", e);
                return new Config();
            }
        }
    }

    public static void write(File file, Config config) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(GSON.toJson(config));
        } catch (IOException e) {
            LOGGER.error("failed to save config", e);
        }
    }

    public static void openFileInEditor(File file) {
        Util.getOperatingSystem().open(file);
    }
}
