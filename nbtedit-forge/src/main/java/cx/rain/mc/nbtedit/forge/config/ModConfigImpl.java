package cx.rain.mc.nbtedit.forge.config;

import cx.rain.mc.nbtedit.api.command.ModPermissions;
import cx.rain.mc.nbtedit.api.config.IModConfig;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public class ModConfigImpl implements IModConfig {
    public static ForgeConfigSpec CONFIG;

    public static ForgeConfigSpec.BooleanValue DEBUG;

    public static Map<ModPermissions, ForgeConfigSpec.ConfigValue<Integer>> PERMISSION_LEVELS = new HashMap<>();

    static {
        var builder = new ForgeConfigSpec.Builder();

        builder.comment("General settings.")
                .push("general");

        DEBUG = builder
                .comment("Enable debug logs. Necessary if you are reporting bugs.")
                .define("debug", false);

        builder.comment("Permission node levels. Like vanilla, should in 0 ~ 5 range.")
                .push("permission");

        for (var p : ModPermissions.values()) {
            var spec = builder.define(p.getName(), p.getDefaultLevel());
            PERMISSION_LEVELS.put(p, spec);
        }

        builder.pop();
        builder.pop();

        CONFIG = builder.build();
    }

    @Override
    public boolean isDebug() {
        return DEBUG.get();
    }
}
