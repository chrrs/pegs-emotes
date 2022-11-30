package me.chrrrs.pegsemotes;

import me.chrrrs.pegsemotes.emotes.EmoteRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmotesMod implements ClientModInitializer {
	public static final int EMOTE_HEIGHT = 8;

	private final Logger LOGGER = LogManager.getLogger("pegsemotes.ChatEmotesMod");

	@Override
	public void onInitializeClient() {
		LOGGER.info("onInitializeClient(): Loading emotes...");

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
			new SimpleSynchronousResourceReloadListener() {
				@Override
				public Identifier getFabricId() {
					return new Identifier("pegs-emotes", "emotes");
				}

				@Override
				public void reload(ResourceManager manager) {
					EmoteRegistry.getInstance().init();
				}
			}
		);

		LOGGER.info("onInitializeClient(): Emotes loaded.");
	}
}
