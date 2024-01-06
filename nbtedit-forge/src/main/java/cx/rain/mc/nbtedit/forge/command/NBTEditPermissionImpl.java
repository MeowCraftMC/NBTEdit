package cx.rain.mc.nbtedit.forge.command;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID)
public class NBTEditPermissionImpl implements INBTEditCommandPermission {
    // qyl27: The name of this permission node is "nbtedit.use".
    public static final PermissionNode<Boolean> PERMISSION_USE = bool("use", 2);

    private static PermissionNode<Boolean> bool(String name, int defaultLevel) {
        return new PermissionNode<>(NBTEdit.MODID, name, PermissionTypes.BOOLEAN,
                (player, uuid, context) -> player != null && player.hasPermissions(defaultLevel));
    }

    @SubscribeEvent
    public static void registerPermission(PermissionGatherEvent.Nodes event) {
        event.addNodes(PERMISSION_USE);
    }

    @Override
    public boolean hasPermission(final CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            return PermissionAPI.getPermission(player, PERMISSION_USE);
        } else {
            return true;
        }
    }

    @Override
    public boolean hasPermission(final ServerPlayer player) {
        return PermissionAPI.getPermission(player, PERMISSION_USE);
    }
}
