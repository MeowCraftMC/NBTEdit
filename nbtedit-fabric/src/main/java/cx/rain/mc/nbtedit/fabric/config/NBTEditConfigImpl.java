package cx.rain.mc.nbtedit.fabric.config;

import com.google.gson.Gson;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NBTEditConfigImpl implements INBTEditConfig {
    public static final Gson GSON = new Gson();

    protected File configDir;
    protected File configFile;
    protected ConfigBean config = new ConfigBean();

    public NBTEditConfigImpl(File gameDir) {
        configDir = new File(gameDir, "config");
        configFile = new File(configDir, "nbtedit.json");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        loadConfig();
    }

    private void loadConfig() {
        if (configFile.exists()) {
            try {
                config = GSON.fromJson(Files.readString(configFile.toPath()), ConfigBean.class);
            } catch (IOException ex) {
                ex.printStackTrace();
                saveNewConfig();
            }
        } else {
            saveNewConfig();
        }
    }

    private void saveNewConfig() {
        try {
            var json = GSON.toJson(config);
            Files.writeString(configFile.toPath(), json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean canEditOthers() {
        return config.canEditOthers;
    }

    @Override
    public boolean isDebug() {
        return config.debug;
    }

    public int getPermissionLevel() {
        return config.permissionLevel;
    }
}
