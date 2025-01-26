package cx.rain.mc.nbtedit.fabric.command;

import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.fabric.config.ModConfigImpl;
import net.minecraft.commands.CommandSourceStack;
import cx.rain.mc.nbtedit.api.command.ModPermissions;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class VanillaPermissionImpl implements IModPermission {
    private final ModConfigImpl config;

    public VanillaPermissionImpl(ModConfigImpl config) {
        this.config = config;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSourceStack sourceStack, @NotNull ModPermissions permission) {
        return sourceStack.hasPermission(config.getPermissionsLevel(permission));
    }

    @Override
    public boolean hasPermission(@NotNull ServerPlayer player, @NotNull ModPermissions permission) {
        return player.hasPermissions(config.getPermissionsLevel(permission));
    }
}
