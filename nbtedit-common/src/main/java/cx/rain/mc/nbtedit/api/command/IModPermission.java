package cx.rain.mc.nbtedit.api.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface IModPermission {
    boolean hasPermission(@NotNull CommandSourceStack sourceStack, @NotNull ModPermissions permission);

    boolean hasPermission(@NotNull ServerPlayer player, @NotNull ModPermissions permission);

    default boolean canOpenEditor(@NotNull ServerPlayer player) {
        return hasPermission(player, ModPermissions.USE) || hasPermission(player, ModPermissions.READ_ONLY);
    }

    default boolean isReadOnly(@NotNull ServerPlayer player) {
        return !hasPermission(player, ModPermissions.USE) && hasPermission(player, ModPermissions.READ_ONLY);
    }

    default boolean canSave(@NotNull ServerPlayer player) {
        return hasPermission(player, ModPermissions.USE);
    }

    default boolean canEditOnPlayer(@NotNull ServerPlayer player) {
        return hasPermission(player, ModPermissions.EDIT_ON_PLAYER);
    }
}
