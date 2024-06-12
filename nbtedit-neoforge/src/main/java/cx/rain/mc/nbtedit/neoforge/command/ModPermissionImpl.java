package cx.rain.mc.nbtedit.neoforge.command;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.command.ModPermissions;
import cx.rain.mc.nbtedit.neoforge.config.ModConfigImpl;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = NBTEdit.MODID)
public class ModPermissionImpl implements IModPermission {
    public static final Map<ModPermissions, PermissionNode<Boolean>> NODES = new HashMap<>();

    private static PermissionNode<Boolean> bool(String name, int defaultLevel) {
        return new PermissionNode<>(NBTEdit.MODID, name, PermissionTypes.BOOLEAN,
                (player, uuid, context) -> player != null && player.hasPermissions(defaultLevel));
    }

    @SubscribeEvent
    public static void registerPermission(PermissionGatherEvent.Nodes event) {
        for (var p : ModPermissions.values()) {
            var node = bool(p.getName(), ModConfigImpl.PERMISSION_LEVELS.get(p).get());
            NODES.put(p, node);
            event.addNodes(node);
        }
    }

    @Override
    public boolean hasPermission(ServerPlayer player, ModPermissions permission) {
        return PermissionAPI.getPermission(player, NODES.get(permission));
    }
}
