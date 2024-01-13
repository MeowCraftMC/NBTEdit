package cx.rain.mc.nbtedit.neoforge;

import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.neoforge.command.NBTEditPermissionImpl;
import cx.rain.mc.nbtedit.neoforge.config.NBTEditConfigImpl;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;

public class NBTEditPlatformImpl {
    private static final NBTEditNetworkingImpl NETWORKING = new NBTEditNetworkingImpl();
    private static final NBTEditConfigImpl CONFIG = new NBTEditConfigImpl();
    private static final NBTEditPermissionImpl PERMISSION = new NBTEditPermissionImpl();

    public static INBTEditNetworking getNetworking() {
        return NETWORKING;
    }

    public static INBTEditConfig getConfig() {
        return CONFIG;
    }

    public static INBTEditCommandPermission getPermission() {
        return PERMISSION;
    }

    static void load() {
    }
}
