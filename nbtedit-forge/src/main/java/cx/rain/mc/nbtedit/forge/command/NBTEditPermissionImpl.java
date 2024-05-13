package cx.rain.mc.nbtedit.forge.command;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID)
public class NBTEditPermissionImpl implements INBTEditCommandPermission {
    public static final String PERMISSION_USE = "nbtedit.use";

    public NBTEditPermissionImpl() {
        PermissionAPI.registerNode(PERMISSION_USE, DefaultPermissionLevel.OP, "Permission node to use NBTEdit.");
    }

    @Override
    public boolean hasPermission(final CommandSourceStack source) {
        if (source.getEntity() instanceof Player) {
            Player player = (Player) source.getEntity();
            return PermissionAPI.hasPermission(player, PERMISSION_USE);
        } else {
            return true;
        }
    }

    @Override
    public boolean hasPermission(final ServerPlayer player) {
        return PermissionAPI.hasPermission(player, PERMISSION_USE);
    }
}
