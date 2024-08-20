package cx.rain.mc.nbtedit.neoforge;

import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.config.IModConfig;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.neoforge.command.ModPermissionImpl;
import cx.rain.mc.nbtedit.neoforge.config.ModConfigImpl;
import cx.rain.mc.nbtedit.neoforge.networking.ModNetworkingImpl;

public class NBTEditPlatformImpl {
    private static final ModNetworkingImpl NETWORKING = new ModNetworkingImpl();
    private static final ModConfigImpl CONFIG = new ModConfigImpl();
    private static final ModPermissionImpl PERMISSION = new ModPermissionImpl();

    public static IModNetworking getNetworking() {
        return NETWORKING;
    }

    public static IModConfig getConfig() {
        return CONFIG;
    }

    public static IModPermission getPermission() {
        return PERMISSION;
    }

    static void load() {
    }
}
