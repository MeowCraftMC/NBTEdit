package cx.rain.mc.nbtedit.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class NBTEditCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> NBTEDIT = literal("nbtedit")
            .requires(source -> NBTEdit.getInstance().getPermission().hasPermission(source))
            .executes(NBTEditCommand::onUse)
            .then(argument("entity", EntityArgument.entity())
                    .executes(NBTEditCommand::onEntity))
            .then(argument("block", BlockPosArgument.blockPos())
                    .executes(NBTEditCommand::onBlockEntity))
            .then(literal("me")
                    .executes(NBTEditCommand::onEntityMe))
            .then(literal("hand")
                    .executes(NBTEditCommand::onItemHand));

    private static int onUse(final CommandContext<CommandSourceStack> context) {
        if (!ensurePlayer(context)) {
            return 0;
        }

        var player = context.getSource().getPlayer();
        NBTEdit.getInstance().getNetworking().serverRayTraceRequest(player);

        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " issued command /nbtedit.");
        return 1;
    }

    private static int onEntity(final CommandContext<CommandSourceStack> context) {
        if (!ensurePlayer(context)) {
            return 0;
        }

        var player = context.getSource().getPlayer();
        var entity = context.getArgument("entity", Entity.class);

        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                " issued command /nbtedit with an entity.");
        NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, entity);
        return 1;
    }

    private static int onBlockEntity(final CommandContext<CommandSourceStack> context) {
        if (!ensurePlayer(context)) {
            return 0;
        }

        var player = context.getSource().getPlayer();
        var pos = context.getArgument("block", BlockPos.class);

        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                " issued command /nbtedit with an block at XYZ: " +
                pos.getX() + " " + pos.getY() + " " + pos.getZ() + ".");
        NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, pos);
        return 1;
    }

    private static int onEntityMe(final CommandContext<CommandSourceStack> context) {
        if (!ensurePlayer(context)) {
            return 0;
        }

        var player = context.getSource().getPlayer();

        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                " issued command /nbtedit to edit itself.");
        NBTEdit.getInstance().getNetworking().serverOpenClientGui(player);
        return 1;
    }

    private static int onItemHand(final CommandContext<CommandSourceStack> context) {
        if (!ensurePlayer(context)) {
            return 0;
        }

        var player = context.getSource().getPlayer();
        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                " issued command /nbtedit to edit hand.");

        var stack = player.getMainHandItem();
        NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, stack);
        return 1;
    }

    private static boolean ensurePlayer(final CommandContext<CommandSourceStack> context) {
        var source = context.getSource();
        if (source.getEntity() instanceof ServerPlayer) {
            return true;
        } else {
            source.sendFailure(Component.translatable(Constants.MESSAGE_NOT_PLAYER).withStyle(ChatFormatting.RED));
            return false;
        }
    }
}
