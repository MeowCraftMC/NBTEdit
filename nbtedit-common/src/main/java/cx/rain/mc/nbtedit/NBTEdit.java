package cx.rain.mc.nbtedit;

import net.minecraft.SharedConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Properties;

public class NBTEdit {
    public static final String MODID = "nbtedit";
    public static final String NAME = "In-game NBTEdit Reborn";
    public static final String VERSION;
    public static final OffsetDateTime BUILD_TIME;

    static {
        var properties = new Properties();
        var version = "";
        OffsetDateTime buildTime;
        try {
            properties.load(NBTEdit.class.getResourceAsStream("/build_info.properties"));
            version = properties.getProperty("build_version");
            buildTime = OffsetDateTime.parse(properties.getProperty("build_time"));
        } catch (IOException ex) {
            version = "Unknown";
            buildTime = null;
        }
        VERSION = version;
        BUILD_TIME = buildTime;
    }

    private static NBTEdit INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(NAME);

    public NBTEdit() {
        INSTANCE = this;

        logger.info("Loading NBTEdit ver: {} on mc {}, build at {}",
                VERSION,
                SharedConstants.getCurrentVersion().getName(),
                BUILD_TIME != null ? BUILD_TIME : "B.C. 3200");
    }

    public static NBTEdit getInstance() {
        return INSTANCE;
    }

    public Logger getLogger() {
        return logger;
    }
}
