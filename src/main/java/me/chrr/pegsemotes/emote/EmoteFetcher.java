package me.chrr.pegsemotes.emote;

import me.chrr.pegsemotes.EmoteMod;
import me.chrr.pegsemotes.util.GifDecoder;
import me.chrr.pegsemotes.util.ImageUtil;
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

    public Future<Emote> fetchEmote(RemoteEmote remoteEmote) {
        return executorService.submit(() -> {
            ApiEmotes.ApiEmote apiEmote = remoteEmote.apiEmote;

            try {
                File cachedImage = getCacheDir().resolve(apiEmote.sha1 + "." + apiEmote.format).toFile();
                if (cachedImage.isFile()) {
                    try {
                        return readEmote(apiEmote.format, new FileInputStream(cachedImage));
                    } catch (Exception e) {
                        LOGGER.error("could not read " + apiEmote.name + " from local cache", e);
                    }
                }

                URL url = new URL(remoteEmote.base, apiEmote.url);
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("User-Agent", EmoteMod.USER_AGENT);

                byte[] bytes = connection.getInputStream().readAllBytes();
                if (!apiEmote.sha1.equals(DigestUtils.sha1Hex(bytes))) {
                    throw new IllegalStateException("SHA1 of fetched image does not match");
                }

                if (!cachedImage.isFile()) {
                    //noinspection ResultOfMethodCallIgnored
                    cachedImage.getParentFile().mkdirs();
                    //noinspection ResultOfMethodCallIgnored
                    cachedImage.createNewFile();

                    try (FileOutputStream outputStream = new FileOutputStream(cachedImage)) {
                        outputStream.write(bytes);
                    }
                }

                return readEmote(apiEmote.format, new ByteArrayInputStream(bytes));
            } catch (Exception e) {
                LOGGER.error("failed to fetch " + apiEmote.name, e);
                throw e;
            }
        });
    }

    private Emote readEmote(String format, InputStream inputStream) throws IOException {
        if (format.equals("png")) {
            NativeImage image = NativeImage.read(inputStream);
            if (image.getWidth() > 256 || image.getHeight() > 256) {
                throw new IOException("emote image should be max. 256x256");
            }

            return new Emote.Static(image);
        } else if (format.equals("gif")) {
            GifDecoder decoder = new GifDecoder();

            if (decoder.read(inputStream) != 0) {
                throw new IOException("failed to decode GIF file");
            }

            if (decoder.getImage().getWidth() > 256 || decoder.getImage().getHeight() > 256) {
                throw new IOException("emote image should be max. 256x256");
            }

            Emote.Animated.Frame[] frames = new Emote.Animated.Frame[decoder.getFrameCount()];
            for (int i = 0; i < decoder.getFrameCount(); i++) {
                NativeImage image = ImageUtil.fromBufferedImage(decoder.getFrame(i));
                frames[i] = new Emote.Animated.Frame(image, decoder.getDelay(i));
            }

            return new Emote.Animated(frames);
        } else {
            throw new UnsupportedOperationException(format + " format is not supported");
        }
    }

    private Path getCacheDir() {
        return FabricLoader.getInstance().getGameDir().resolve("pegs-emotes").resolve("emote-cache");
    }

    public record RemoteEmote(URL base, ApiEmotes.ApiEmote apiEmote) {
    }
}
