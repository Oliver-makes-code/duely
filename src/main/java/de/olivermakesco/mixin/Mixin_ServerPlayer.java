package de.olivermakesco.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.olivermakesco.PlayerConfigAttachment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public class Mixin_ServerPlayer {
    @WrapOperation(
            method = "canHarmPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;isPvpAllowed()Z"
            )
    )
    private boolean isPvpMutual(ServerPlayer self, Operation<Boolean> original, Player other) {
        if (PlayerConfigAttachment.get(self).shouldAcceptPvp && PlayerConfigAttachment.get(other).shouldAcceptPvp)
            return original.call(self);

        return false;
    }
}
