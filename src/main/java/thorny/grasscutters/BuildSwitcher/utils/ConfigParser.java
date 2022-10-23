package thorny.grasscutters.BuildSwitcher.utils;

import com.google.gson.GsonBuilder;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.utils.JsonUtils;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public final class ConfigParser {

    private Config config;
    private final String configPath = Grasscutter.getConfig().folderStructure.plugins + "BuildSwitcher";
    private final File configFile = new File(this.configPath + "/config.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ConfigParser() {
        this.loadConfig(null);
    }

    public Config getConfig() {
        return this.config;
    }

    public void loadConfig(Config updated) {
        try (FileReader file = new FileReader(this.configFile)) {
            this.config = gson.fromJson(file, Config.class);
            Grasscutter.getLogger().info("[BuildSwitcher] Config Loaded!");
        } catch (Exception e) {
            this.config = new Config();
            Grasscutter.getLogger().info("[BuildSwitcher] Basic config creating...");
        }

        if (!saveConfig(updated)) {
            Grasscutter.getLogger().error("[BuildSwitcher] Unable to save config file.");
        }
    }

    public boolean saveConfig(Config updated) {
        File dir = new File(this.configPath);

        if (!dir.exists() || !dir.isDirectory()) {
            if (!new java.io.File(String.valueOf(dir)).mkdirs())
                return false;
        }

        try (FileWriter file = new FileWriter(this.configFile)) {
            if(updated == null){file.write(JsonUtils.encode(this.config));}
            else{file.write(JsonUtils.encode(updated));}
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
