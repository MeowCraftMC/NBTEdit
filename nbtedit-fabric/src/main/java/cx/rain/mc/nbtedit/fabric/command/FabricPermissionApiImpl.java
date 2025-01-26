package cx.rain.mc.nbtedit.fabric.command;

import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.command.ModPermissions;
import cx.rain.mc.nbtedit.fabric.config.ModConfigImpl;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class FabricPermissionApiImpl implements IModPermission {
    private final ModConfigImpl config;

    public FabricPermissionApiImpl(ModConfigImpl config) {
        this.config = config;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSourceStack sourceStack, @NotNull ModPermissions permission) {
        return Permissions.check(sourceStack, "nbtedit.%s".formatted(permission.getName()), config.getPermissionsLevel(permission));
    }

    @Override
    public boolean hasPermission(@NotNull ServerPlayer player, @NotNull ModPermissions permission) {
        return Permissions.check(player, "nbtedit.%s".formatted(permission.getName()), config.getPermissionsLevel(permission));
    }
}
