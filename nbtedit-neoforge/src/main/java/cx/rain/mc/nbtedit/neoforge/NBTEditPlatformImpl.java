package cx.rain.mc.nbtedit.neoforge;

import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.neoforge.command.NBTEditPermissionImpl;
import cx.rain.mc.nbtedit.neoforge.config.NBTEditConfigImpl;
import cx.rain.mc.nbtedit.neoforge.networking.NBTEditNetworkingImpl;

public class NBTEditPlatformImpl extends NBTEditPlatform {
    private static final NBTEditNetworkingImpl NETWORKING = new NBTEditNetworkingImpl();
    private static final NBTEditConfigImpl CONFIG = new NBTEditConfigImpl();
    private static final NBTEditPermissionImpl PERMISSION = new NBTEditPermissionImpl();

    @Override
    public INBTEditNetworking getNetworking() {
        return NETWORKING;
    }

    @Override
    public INBTEditConfig getConfig() {
        return CONFIG;
    }

    @Override
    public INBTEditCommandPermission getPermission() {
        return PERMISSION;
    }
}
