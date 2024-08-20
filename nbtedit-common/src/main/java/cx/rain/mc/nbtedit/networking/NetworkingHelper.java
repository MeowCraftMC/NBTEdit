package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class NetworkingHelper {
    public static boolean checkReadPermission(ServerPlayer player) {
        var result = NBTEditPlatform.getPermission().canOpenEditor(player);

        if (!result) {
            player.sendSystemMessage(Component.translatable(ModConstants.MESSAGE_NO_PERMISSION)
                    .withStyle(ChatFormatting.RED));
        }

        return result;
    }

    public static boolean checkWritePermission(ServerPlayer player) {
        var result = NBTEditPlatform.getPermission().canSave(player);

        if (!result) {
            player.sendSystemMessage(Component.translatable(ModConstants.MESSAGE_NO_PERMISSION)
                    .withStyle(ChatFormatting.RED));
        }

        return result;
    }

    public static boolean checkEditOnPlayerPermission(ServerPlayer player) {
        var result = NBTEditPlatform.getPermission().canEditOnPlayer(player);

        if (!result) {
            player.sendSystemMessage(Component.translatable(ModConstants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER)
                    .withStyle(ChatFormatting.RED));
        }

        return result;
    }

    public static boolean checkPosLoaded(ServerPlayer player, BlockPos pos) {
        var result = player.serverLevel().isLoaded(pos);

        if (!result) {
            player.sendSystemMessage(Component.translatable(ModConstants.MESSAGE_NOT_LOADED)
                    .withStyle(ChatFormatting.RED));
        }

        return result;
    }

    public static boolean isDebug() {
        return NBTEditPlatform.getConfig().isDebug();
    }
}
