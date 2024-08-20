package cx.rain.mc.nbtedit;

import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.config.IModConfig;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class NBTEditPlatform {
    @ExpectPlatform
    public static IModNetworking getNetworking() {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static IModConfig getConfig() {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static IModPermission getPermission() {
        throw new RuntimeException();
    }
}
