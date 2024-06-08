package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.ModConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class NBTEditNetworkingHelper {
    public static boolean checkPermission(ServerPlayer player) {
        var result = NBTEdit.getInstance().getPermission().hasPermission(player);

        if (!result) {
            player.sendSystemMessage(Component.translatable(ModConstants.MESSAGE_NO_PERMISSION)
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
        return NBTEdit.getInstance().getConfig().isDebug();
    }

    public static boolean canEditOthers() {
        return NBTEdit.getInstance().getConfig().canEditOthers();
    }
}
