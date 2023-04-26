package cx.rain.mc.nbtedit.api;

import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;

public interface INBTEditPlatform {
    INBTEditNetworking getNetworking();

    INBTEditConfig getConfig();

    INBTEditCommandPermission getPermission();
}
