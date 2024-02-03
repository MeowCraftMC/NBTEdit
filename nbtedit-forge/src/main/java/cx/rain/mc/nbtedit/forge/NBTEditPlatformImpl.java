package cx.rain.mc.nbtedit.forge;

import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.forge.command.NBTEditPermissionImpl;
import cx.rain.mc.nbtedit.forge.config.NBTEditConfigImpl;
import cx.rain.mc.nbtedit.forge.networking.NBTEditNetworkingImpl;

public class NBTEditPlatformImpl {
    private static final NBTEditNetworkingImpl NETWORKING = new NBTEditNetworkingImpl();
    private static final NBTEditConfigImpl CONFIG = new NBTEditConfigImpl();
    private static final NBTEditPermissionImpl PERMISSION = new NBTEditPermissionImpl();

    public static void register() {
    }

    public static INBTEditNetworking getNetworking() {
        return NETWORKING;
    }

    public static INBTEditConfig getConfig() {
        return CONFIG;
    }

    public static INBTEditCommandPermission getPermission() {
        return PERMISSION;
    }
}
