package cx.rain.mc.nbtedit;

import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;

public abstract class NBTEditPlatform {
    private static NBTEditPlatform INSTANCE;

    static void setInstance(NBTEditPlatform platform) {
        INSTANCE = platform;
    }

    public static NBTEditPlatform getInstance() {
        return INSTANCE;
    }

    public abstract INBTEditNetworking getNetworking();

    public abstract INBTEditConfig getConfig();

    public abstract INBTEditCommandPermission getPermission();
}
