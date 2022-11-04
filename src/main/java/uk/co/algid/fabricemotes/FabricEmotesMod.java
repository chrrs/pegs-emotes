package uk.co.algid.fabricemotes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.algid.fabricemotes.emotes.EmoteRegistry;

public class FabricEmotesMod implements ClientModInitializer {
	private final Logger LOGGER = LogManager.getLogger("fabricemotes.ChatEmotesMod");

	@Override
	public void onInitializeClient() {
		LOGGER.info("onInitializeClient(): Loading emotes...");

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
			new SimpleSynchronousResourceReloadListener() {
				@Override
				public Identifier getFabricId() {
					return new Identifier("chatemotes", "emotes");
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
