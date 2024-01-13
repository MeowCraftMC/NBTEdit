package cx.rain.mc.nbtedit.forge.config;

import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class NBTEditConfigImpl implements INBTEditConfig {
    public static ForgeConfigSpec CONFIG;

    public static ForgeConfigSpec.BooleanValue CAN_EDIT_OTHER_PLAYERS;
    public static ForgeConfigSpec.BooleanValue DEBUG;

    static {
        var builder = new ForgeConfigSpec.Builder();

        builder.comment("General settings.")
                .push("general");

        CAN_EDIT_OTHER_PLAYERS = builder
                .comment("If true, allows you edit the nbt tags of other players. USE AT YOUR OWN RISK!")
                .define("can_edit_other_players", false);

        DEBUG = builder
                .comment("If true, show more debug info.")
                .define("debug", false);

        builder.pop();

        CONFIG = builder.build();
    }

    @Override
    public boolean canEditOthers() {
        return CAN_EDIT_OTHER_PLAYERS.get();
    }

    @Override
    public boolean isDebug() {
        return DEBUG.get();
    }
}
