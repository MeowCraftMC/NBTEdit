package cx.rain.mc.nbtedit;

import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class NBTEditPlatform {
    @ExpectPlatform
    public static INBTEditNetworking getNetworking() {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static INBTEditConfig getConfig() {
        throw new RuntimeException();
    }

    @ExpectPlatform
    public static INBTEditCommandPermission getPermission() {
        throw new RuntimeException();
    }
}
