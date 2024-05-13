package cx.rain.mc.nbtedit.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

public class NBTEditNetworkingHelper {
    public static boolean checkPermission(ServerPlayer player) {
        boolean result = NBTEdit.getInstance().getPermission().hasPermission(player);

        if (!result) {
            player.sendMessage(new TranslatableComponent(Constants.MESSAGE_NO_PERMISSION)
                    .withStyle(ChatFormatting.RED), Util.NIL_UUID);
        }

        return result;
    }

    public static boolean checkPosLoaded(ServerPlayer player, BlockPos pos) {
        boolean result = player.getLevel().isLoaded(pos);

        if (!result) {
            player.sendMessage(new TranslatableComponent(Constants.MESSAGE_NOT_LOADED)
                    .withStyle(ChatFormatting.RED), Util.NIL_UUID);
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
