package cx.rain.mc.nbtedit.fabric;

import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.fabric.command.NBTEditPermissionImpl;
import cx.rain.mc.nbtedit.fabric.config.NBTEditConfigImpl;
import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import net.fabricmc.loader.api.FabricLoader;

public class NBTEditPlatformImpl {
    private static final NBTEditNetworkingImpl NETWORKING = new NBTEditNetworkingImpl();
    private static final NBTEditConfigImpl CONFIG = new NBTEditConfigImpl(FabricLoader.getInstance().getGameDir().toFile());
    private static final NBTEditPermissionImpl PERMISSION = new NBTEditPermissionImpl(CONFIG.getPermissionLevel());

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
