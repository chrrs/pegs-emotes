package me.chrr.pegsemotes.emote;

import me.chrr.pegsemotes.EmoteMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.texture.NativeImage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EmoteFetcher {
    private static final Logger LOGGER = LogManager.getLogger("pegs-emotes.EmoteFetcher");

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public Future<Emote> fetchEmote(ApiEmotes.ApiEmote apiEmote) {
        return executorService.submit(() -> {
            try {
                File cachedImage = getCacheDir().resolve(apiEmote.sha1 + "." + apiEmote.format).toFile();
                if (cachedImage.isFile()) {
                    try {
                        return readEmote(apiEmote.format, new FileInputStream(cachedImage));
                    } catch (IOException e) {
                        LOGGER.error("could not read " + apiEmote.name + " from local cache", e);
                    }
                }

                URL url = new URL(apiEmote.url);
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("User-Agent", EmoteMod.USER_AGENT);

                byte[] bytes = connection.getInputStream().readAllBytes();
                if (!apiEmote.sha1.equals(DigestUtils.sha1Hex(bytes))) {
                    throw new IllegalStateException("SHA1 of fetched image does not match");
                }

                if ((!cachedImage.isFile() || cachedImage.delete())
                        && cachedImage.getParentFile().mkdirs()
                        && cachedImage.createNewFile()) {
                    try (FileOutputStream outputStream = new FileOutputStream(cachedImage)) {
                        outputStream.write(bytes);
                    }
                }

                return readEmote(apiEmote.format, new ByteArrayInputStream(bytes));
            } catch (IOException | IllegalStateException e) {
                LOGGER.error("failed to fetch " + apiEmote, e);
                throw e;
            }
        });
    }

    private Emote readEmote(String format, InputStream inputStream) throws IOException {
        if (format.equals("png")) {
            NativeImage image = NativeImage.read(inputStream);
            return new Emote.Static(image);
        } else {
            throw new UnsupportedOperationException(format + " format is not supported");
        }
    }

    private Path getCacheDir() {
        return FabricLoader.getInstance().getGameDir().resolve("pegs-emotes").resolve("emote-cache");
    }
}
