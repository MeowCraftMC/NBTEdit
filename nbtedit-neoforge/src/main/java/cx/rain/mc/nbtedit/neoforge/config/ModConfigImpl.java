package cx.rain.mc.nbtedit.neoforge.config;

import cx.rain.mc.nbtedit.api.command.ModPermissions;
import cx.rain.mc.nbtedit.api.config.IModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;
import java.util.Map;

public class ModConfigImpl implements IModConfig {
    public static ModConfigSpec CONFIG;

    public static ModConfigSpec.BooleanValue DEBUG;

    public static Map<ModPermissions, ModConfigSpec.ConfigValue<Integer>> PERMISSION_LEVELS = new HashMap<>();

    static {
        var builder = new ModConfigSpec.Builder();

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
