package cx.rain.mc.nbtedit;

import cx.rain.mc.nbtedit.api.INBTEditPlatform;
import cx.rain.mc.nbtedit.api.command.INBTEditCommandPermission;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.client.NBTEditClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NBTEdit {
    public static final String MODID = "nbtedit";
    public static final String NAME = "In-game NBTEdit Reborn";
    public static final String VERSION = "@version@";

    private static NBTEdit INSTANCE;

    private INBTEditPlatform platform;

    private NBTEditClient client = new NBTEditClient();

    private final Logger logger = LoggerFactory.getLogger(NAME);

    public NBTEdit() {
        INSTANCE = this;

        logger.info("Loading NBTEdit ver: " + VERSION);
    }

    public void init(INBTEditPlatform platform) {
        this.platform = platform;
    }

    public static NBTEdit getInstance() {
        return INSTANCE;
    }

    public Logger getLogger() {
        return logger;
    }

    public INBTEditNetworking getNetworking() {
        return platform.getNetworking();
    }

    public NBTEditClient getClient() {
        return client;
    }

    public INBTEditConfig getConfig() {
        return platform.getConfig();
    }

    public INBTEditCommandPermission getPermission() {
        return platform.getPermission();
    }
}
