package cx.rain.mc.nbtedit;

import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.config.IModConfig;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class NBTEdit {
    public static final String MODID = "nbtedit";
    public static final String NAME = "In-game NBTEdit Reborn";
    public static final String VERSION;

    static {
        var properties = new Properties();
        var version = "";
        try {
            properties.load(NBTEdit.class.getResourceAsStream("/build.properties"));
            version = properties.getProperty("mod_version");
        } catch (IOException ex) {
            version = "Unknown";
        }
        VERSION = version;
    }

    private static NBTEdit INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(NAME);

    public NBTEdit() {
        INSTANCE = this;

        logger.info("Loading NBTEdit ver: {}", VERSION);
    }

    public static NBTEdit getInstance() {
        return INSTANCE;
    }

    public Logger getLogger() {
        return logger;
    }
}
