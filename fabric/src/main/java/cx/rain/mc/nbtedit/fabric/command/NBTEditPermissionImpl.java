package cx.rain.mc.nbtedit.fabric.command;

import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class NBTEditPermissionImpl implements INBTEditCommandPermission {
    private int permissionLevel = 2;

    public NBTEditPermissionImpl(int level) {
        this.permissionLevel = level;
    }

    @Override
    public boolean hasPermission(CommandSourceStack source) {
        return source.hasPermission(permissionLevel);
    }

    @Override
    public boolean hasPermission(ServerPlayer player) {
        return player.hasPermissions(permissionLevel);
    }
}
