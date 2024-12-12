package cx.rain.mc.nbtedit.fabric.command;

import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.command.ModPermissions;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ModPermissionImpl implements IModPermission {
    private static final Logger log = LoggerFactory.getLogger(ModPermissionImpl.class);
    private final Map<ModPermissions, Integer> permissionLevels = new HashMap<>();
    private IModPermission delegate = null;

    public ModPermissionImpl(Map<String, Integer> permissionToLevel) {
        try {
            Class.forName("me.lucko.fabric.api.permissions.v0.Permissions", false, getClass().getClassLoader());
            delegate = new FPAModPermissionImpl();
            log.info("[NBTEdit] Using Fabric Permissions API");
        } catch (ClassNotFoundException ignore) {
        }
        for (var p : ModPermissions.values()) {
            if (permissionToLevel.containsKey(p.getName())) {
                permissionLevels.put(p, permissionToLevel.get(p.getName()));
            } else {
                permissionLevels.put(p, p.getDefaultLevel());
            }
        }
    }

    @Override
    public boolean hasPermission(ServerPlayer player, ModPermissions permission) {
        boolean result = false;
        if (delegate != null) {
            result = delegate.hasPermission(player, permission);
        }
        // Check with delegate first
        return result || player.hasPermissions(permissionLevels.get(permission));
    }
}
