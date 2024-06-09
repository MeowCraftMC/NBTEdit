package cx.rain.mc.nbtedit.forge.config;

import cx.rain.mc.nbtedit.api.config.IModConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfigImpl implements IModConfig {
    public static ForgeConfigSpec CONFIG;

    public static ForgeConfigSpec.BooleanValue DEBUG;

    static {
        var builder = new ForgeConfigSpec.Builder();

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
