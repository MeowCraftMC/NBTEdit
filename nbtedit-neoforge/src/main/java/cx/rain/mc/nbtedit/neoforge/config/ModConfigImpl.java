package cx.rain.mc.nbtedit.neoforge.config;

import cx.rain.mc.nbtedit.api.config.IModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfigImpl implements IModConfig {
    public static ModConfigSpec CONFIG;

    public static ModConfigSpec.BooleanValue DEBUG;

    static {
        var builder = new ModConfigSpec.Builder();

        builder.comment("General settings.")
                .push("general");

        DEBUG = builder
                .comment("If true, show more debug info.")
                .define("debug", false);

        builder.pop();

        CONFIG = builder.build();
    }

    @Override
    public boolean isDebug() {
        return DEBUG.get();
    }
}
