package cx.rain.mc.nbtedit.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.api.command.ModPermissions;
import cx.rain.mc.nbtedit.networking.NetworkEditingHelper;
import com.mojang.brigadier.context.CommandContext;
import cx.rain.mc.nbtedit.networking.packet.s2c.RaytracePacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class NBTEditCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> NBTEDIT = literal("nbtedit")
            .requires(source -> NBTEditPlatform.getPermission().hasPermission(source, ModPermissions.USE))
            .executes(NBTEditCommand::onUse)
            .then(argument("entity", EntityArgument.entity())
                    .executes(NBTEditCommand::onEntity))
            .then(argument("block", BlockPosArgument.blockPos())
                    .executes(NBTEditCommand::onBlockEntity))
            .then(literal("me")
                    .executes(NBTEditCommand::onEntityMe))
            .then(literal("hand")
                    .executes(NBTEditCommand::onItemHand));

    private static int onUse(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        NBTEditPlatform.getNetworking().sendTo(player, new RaytracePacket());

        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " issued command /nbtedit.");
        return 1;
    }

    private static int onEntity(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var entity = EntityArgument.getEntity(context, "entity");

        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                " issued command /nbtedit with an entity.");
        NetworkEditingHelper.editEntity(player, entity.getUUID());
        return 1;
    }

    private static int onBlockEntity(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        var pos = BlockPosArgument.getBlockPos(context, "block");

        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                " issued command /nbtedit with an block at XYZ: " +
                pos.getX() + " " + pos.getY() + " " + pos.getZ() + ".");
        NetworkEditingHelper.editBlockEntity(player, pos);
        return 1;
    }

    private static int onEntityMe(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();

        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                " issued command /nbtedit to edit itself.");
        NetworkEditingHelper.editEntity(player, player.getUUID());
        return 1;
    }

    private static int onItemHand(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayerOrException();
        NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                " issued command /nbtedit to edit hand.");

        var stack = player.getMainHandItem();
        NetworkEditingHelper.editItemStack(player, stack);
        return 1;
    }
}
