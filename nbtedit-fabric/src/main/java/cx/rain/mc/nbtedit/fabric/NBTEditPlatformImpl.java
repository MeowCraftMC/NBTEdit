package cx.rain.mc.nbtedit.fabric;

import cx.rain.mc.nbtedit.NBTEditPlatform;
import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.fabric.command.NBTEditPermissionImpl;
import cx.rain.mc.nbtedit.fabric.config.NBTEditConfigImpl;
import cx.rain.mc.nbtedit.fabric.networking.NBTEditNetworkingImpl;
import net.fabricmc.loader.api.FabricLoader;

public class NBTEditPlatformImpl extends NBTEditPlatform {
    private static final NBTEditNetworkingImpl NETWORKING = new NBTEditNetworkingImpl();
    private static final NBTEditConfigImpl CONFIG = new NBTEditConfigImpl(FabricLoader.getInstance().getGameDir().toFile());
    private static final NBTEditPermissionImpl PERMISSION = new NBTEditPermissionImpl(CONFIG.getPermissionLevel());

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
