package cx.rain.mc.nbtedit.fabric;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.config.IModConfig;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.fabric.command.FabricPermissionApiImpl;
import cx.rain.mc.nbtedit.fabric.command.VanillaPermissionImpl;
import cx.rain.mc.nbtedit.fabric.config.ModConfigImpl;
import cx.rain.mc.nbtedit.fabric.networking.ModNetworkingImpl;
import net.fabricmc.loader.api.FabricLoader;

public class NBTEditPlatformImpl {
    private static final ModNetworkingImpl NETWORKING = new ModNetworkingImpl();

    private static final ModConfigImpl CONFIG;
    private static final IModPermission PERMISSION;

    static {
        CONFIG = new ModConfigImpl(FabricLoader.getInstance().getGameDir().toFile());

        {
            IModPermission impl = null;
            try {
                if (FabricLoader.getInstance().isModLoaded("fabric-permissions-api-v0")) {
                    impl = new FabricPermissionApiImpl();
                    NBTEdit.getInstance().getLogger().info("Fabric Permissions API detected, using it.");
                }
            } catch (Throwable ignored) {
            }
            if (impl == null) {
                impl = new VanillaPermissionImpl(CONFIG.getPermissionsLevel());
            }
            PERMISSION = impl;
        }
    }

    public static IModNetworking getNetworking() {
        return NETWORKING;
    }

    public static IModConfig getConfig() {
        return CONFIG;
    }

    public static IModPermission getPermission() {
        return PERMISSION;
    }
}
