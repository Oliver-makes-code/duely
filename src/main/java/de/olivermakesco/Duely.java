package de.olivermakesco;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Duely implements ModInitializer {
    public static final String MODID = "duely";

    @SuppressWarnings("UnstableApiUsage")
    public static final AttachmentType<PlayerConfigAttachment> PLAYER_CONFIG_TYPE = AttachmentRegistry.
            create(
                    rl("player_config"),
                    builder -> builder
                            .persistent(PlayerConfigAttachment.CODEC)
                            .copyOnDeath()
                            .initializer(PlayerConfigAttachment::new)
            );

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(ModCommands::register);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
