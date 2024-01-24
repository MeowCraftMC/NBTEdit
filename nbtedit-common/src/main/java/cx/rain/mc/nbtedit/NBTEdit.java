package cx.rain.mc.nbtedit;

import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.client.NBTEditClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private NBTEditClient client;

    private final Logger logger = LogManager.getLogger(NAME);

    public NBTEdit() {
        INSTANCE = this;

        logger.info("Loading NBTEdit ver: " + VERSION);
    }

    public static NBTEdit getInstance() {
        return INSTANCE;
    }

    public Logger getLogger() {
        return logger;
    }

    public NBTEditClient getClient() {
        if (client == null) {
            client = new NBTEditClient();
        }

        return client;
    }

    public INBTEditNetworking getNetworking() {
        return NBTEditPlatform.getNetworking();
    }

    public INBTEditConfig getConfig() {
        return NBTEditPlatform.getConfig();
    }

    public INBTEditCommandPermission getPermission() {
        return NBTEditPlatform.getPermission();
    }
}
