package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class NBTEditEditingHelper {
    public static void editBlockEntity(ServerPlayer player, BlockPos pos) {
        player.getServer().execute(() -> {
            if (!NBTEditNetworkingHelper.checkPermission(player)) {
                return;
            }

            if (!NBTEditNetworkingHelper.checkPosLoaded(player, pos)) {
                return;
            }

            NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " requested BlockEntity at " +
                    pos.getX() + " " + pos.getY() + " " + pos.getZ() + ".");

            var blockEntity = player.getLevel().getBlockEntity(pos);
            if (blockEntity == null) {
                player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY)
                        .withStyle(ChatFormatting.RED));
                return;
            }

            player.sendMessage(new TranslatableComponent(Constants.MESSAGE_EDITING_BLOCK_ENTITY,
                    pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
            NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, pos, blockEntity);
        });
    }

    public static void editEntity(ServerPlayer player, UUID entityUuid) {
        player.getServer().execute(() -> {
            if (!NBTEditNetworkingHelper.checkPermission(player)) {
                return;
            }

            var entity = player.getLevel().getEntity(entityUuid);

            assert entity != null;  // XXX: qyl27: will it work?

            if (entity instanceof Player
                    && entity != player
                    && !NBTEdit.getInstance().getConfig().canEditOthers()) {
                NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                        " tried to use /nbtedit on a player. But config is not allow that.");
                player.createCommandSourceStack().sendFailure(
                        new TranslatableComponent(Constants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER)
                                .withStyle(ChatFormatting.RED));
                return;
            }

            player.sendMessage(new TranslatableComponent(Constants.MESSAGE_EDITING_ENTITY, entityUuid)
                    .withStyle(ChatFormatting.GREEN), Util.NIL_UUID);

            if (player == entity) {
                NBTEdit.getInstance().getNetworking().serverOpenClientGui(player);
                NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " is editing itself.");
            } else {
                NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, entity);
                NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                        " is editing entity " + entity.getUUID() + ".");
            }
        });
    }

    public static void editItemStack(ServerPlayer player, ItemStack stack) {
        player.getServer().execute(() -> {
            if (!NBTEditNetworkingHelper.checkPermission(player)) {
                return;
            }

            player.sendMessage(new TranslatableComponent(Constants.MESSAGE_EDITING_ITEM_STACK,
                    stack.getDisplayName().getString()).withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
            NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
                    " is editing ItemStack named " + stack.getDisplayName().getString() + ".");
            NBTEdit.getInstance().getNetworking().serverOpenClientGui(player, stack);
        });
    }
}
