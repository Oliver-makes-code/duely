package de.olivermakesco;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.olivermakesco.mixin.Accessor_CommandSourceStack;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(LiteralArgumentBuilder
                .<CommandSourceStack>literal("acceptPvp")
                .then(RequiredArgumentBuilder
                        .<CommandSourceStack, Boolean>argument("value", BoolArgumentType.bool())
                        .executes(ModCommands::setPvp)
                )
                .executes(ModCommands::getPvp)
        );
    }

    private static int getPvp(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();

        var conf = PlayerConfigAttachment.get(player);

        context.getSource().sendSuccess(() -> Component.literal(String.format("Value acceptPvp set to %s", conf.shouldAcceptPvp)), true);

        return 0;
    }

    private static int setPvp(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var source = ((Accessor_CommandSourceStack) context.getSource()).getSource();

        var player = context.getSource().getPlayerOrException();

        if (source != player) {
            context.getSource().sendFailure(Component.literal("Cannot change the pvp value of anyone but yourself!"));

            if (source instanceof MinecraftServer)
                context.getSource().getServer().getPlayerList().broadcastSystemMessage(Component.translatableWithFallback("", "Someone with console access tried to change the pvp status of %s. Shame.", player.getName()), false);

            if (source instanceof ServerPlayer serverPlayer)
                context.getSource().getServer().getPlayerList().broadcastSystemMessage(Component.translatableWithFallback("", "%s tried to change the pvp status of %s. Shame.", serverPlayer.getName(), player.getName()), false);

            return 1;
        }

        var value = BoolArgumentType.getBool(context, "value");

        var conf = PlayerConfigAttachment.get(player);

        conf.shouldAcceptPvp = value;

        PlayerConfigAttachment.set(player, conf);

        context.getSource().sendSuccess(() -> Component.literal(String.format("Value acceptPvp set to %s", value)), true);
        return 0;
    }
}
