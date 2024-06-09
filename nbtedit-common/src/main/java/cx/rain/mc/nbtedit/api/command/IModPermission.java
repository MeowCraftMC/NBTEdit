package cx.rain.mc.nbtedit.api.command;

import net.minecraft.server.level.ServerPlayer;

public interface IModPermission {
    boolean hasPermission(ServerPlayer player, ModPermissions permission);

    default boolean canOpenEditor(ServerPlayer player) {
        return hasPermission(player, ModPermissions.USE) || hasPermission(player, ModPermissions.READ_ONLY);
    }

    default boolean isReadOnly(ServerPlayer player) {
        return hasPermission(player, ModPermissions.READ_ONLY);
    }

    default boolean canSave(ServerPlayer player) {
        return hasPermission(player, ModPermissions.USE);
    }

    default boolean canEditOnPlayer(ServerPlayer player) {
        return hasPermission(player, ModPermissions.EDIT_ON_PLAYER);
    }
}
