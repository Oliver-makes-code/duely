package de.olivermakesco;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Player;

public class PlayerConfigAttachment {
    public static final Codec<PlayerConfigAttachment> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Codec.BOOL.fieldOf("shouldAcceptPvp").forGetter(PlayerConfigAttachment::shouldAcceptPvp)
            ).apply(instance, PlayerConfigAttachment::new)
    );

    public boolean shouldAcceptPvp = false;

    public PlayerConfigAttachment() {}

    public PlayerConfigAttachment(boolean shouldAcceptPvp) {
        this.shouldAcceptPvp = shouldAcceptPvp;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static PlayerConfigAttachment get(Player player) {
        return player.getAttachedOrCreate(Duely.PLAYER_CONFIG_TYPE);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void set(Player player, PlayerConfigAttachment value) {
        player.setAttached(Duely.PLAYER_CONFIG_TYPE, value);
    }

    public boolean shouldAcceptPvp() {
        return shouldAcceptPvp;
    }
}
