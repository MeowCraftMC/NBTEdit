package cx.rain.mc.nbtedit.fabric.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cx.rain.mc.nbtedit.api.command.ModPermissions;
import cx.rain.mc.nbtedit.api.config.IModConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class ModConfigImpl implements IModConfig {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected File configDir;
    protected File configFile;
    protected ConfigBean config = new ConfigBean();

    public ModConfigImpl(File gameDir) {
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
            }
        }

        saveConfig();
    }

    private void saveConfig() {
        try {
            var json = GSON.toJson(config);
            Files.writeString(configFile.toPath(), json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isDebug() {
        return config.debug;
    }

    public Map<String, Integer> getPermissionsLevel() {
        return config.permissionsLevels;
    }

    public int getPermissionsLevel(ModPermissions permission) {
        return config.permissionsLevels.getOrDefault(permission.getName(), permission.getDefaultLevel());
    }
}
