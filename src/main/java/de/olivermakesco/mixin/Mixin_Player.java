package de.olivermakesco.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.olivermakesco.PlayerConfigAttachment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class Mixin_Player {
    @WrapOperation(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    private boolean isSweepPvpMutual(Player self, Entity entity, Operation<Boolean> original) {
        if (!(entity instanceof Player other))
            return original.call(self, entity);

        if (PlayerConfigAttachment.get(self).shouldAcceptPvp && PlayerConfigAttachment.get(other).shouldAcceptPvp)
            return original.call(self, entity);

        return true;
    }
}
