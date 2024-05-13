package cx.rain.mc.nbtedit.fabric.config;

import com.google.gson.Gson;
import cx.rain.mc.nbtedit.api.config.INBTEditConfig;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

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
                StringBuilder builder = new StringBuilder();
                List<String> lines = Files.readAllLines(configFile.toPath());
                for (String line : lines) {
                    builder.append(line);
                }
                config = GSON.fromJson(builder.toString(), ConfigBean.class);
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
            String json = GSON.toJson(config);
            Files.write(configFile.toPath(), json.getBytes(StandardCharsets.UTF_8));
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
