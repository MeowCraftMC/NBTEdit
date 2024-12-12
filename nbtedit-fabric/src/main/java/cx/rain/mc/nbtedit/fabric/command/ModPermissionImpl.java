package cx.rain.mc.nbtedit.fabric.command;

import cx.rain.mc.nbtedit.api.command.ModPermissions;
import cx.rain.mc.nbtedit.api.command.IModPermission;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ModPermissionImpl implements IModPermission {
    private final Map<ModPermissions, Integer> permissionLevels = new HashMap<>();

    public ModPermissionImpl(Map<String, Integer> permissionToLevel) {
        for (var p : ModPermissions.values()) {
            if (permissionToLevel.containsKey(p.getName())) {
                permissionLevels.put(p, permissionToLevel.get(p.getName()));
            } else {
                permissionLevels.put(p, p.getDefaultLevel());
            }
        }
    }

    @Override
    public boolean hasPermission(@NotNull CommandSourceStack sourceStack, @NotNull ModPermissions permission) {
        return sourceStack.hasPermission(permissionLevels.get(permission));
    }

    @Override
    public boolean hasPermission(@NotNull ServerPlayer player, @NotNull ModPermissions permission) {
        return player.hasPermissions(permissionLevels.get(permission));
    }
}
