package cx.rain.mc.nbtedit.neoforge.command;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.command.ModPermissions;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID)
public class ModPermissionImpl implements IModPermission {
    public static final Map<ModPermissions, PermissionNode<Boolean>> NODES = new HashMap<>();

    static {
        for (var p : ModPermissions.values()) {
            NODES.put(p, bool(p.getName(), p.getDefaultLevel()));
        }
    }

    private static PermissionNode<Boolean> bool(String name, int defaultLevel) {
        return new PermissionNode<>(NBTEdit.MODID, name, PermissionTypes.BOOLEAN,
                (player, uuid, context) -> player != null && player.hasPermissions(defaultLevel));
    }

    @SubscribeEvent
    public static void registerPermission(PermissionGatherEvent.Nodes event) {
        for (var n : NODES.values()) {
            event.addNodes(n);
        }
    }

    @Override
    public boolean hasPermission(ServerPlayer player, ModPermissions permission) {
        return PermissionAPI.getPermission(player, NODES.get(permission));
    }
}