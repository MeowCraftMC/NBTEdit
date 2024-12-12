package cx.rain.mc.nbtedit.fabric.command;

import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.command.ModPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.level.ServerPlayer;

public class FPAModPermissionImpl implements IModPermission {

    @Override
    public boolean hasPermission(ServerPlayer player, ModPermissions permission) {
        return Permissions.check(player, "nbtedit.%s".formatted(permission.getName()));
    }
}
