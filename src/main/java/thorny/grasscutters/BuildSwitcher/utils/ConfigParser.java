package thorny.grasscutters.BuildSwitcher.utils;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.utils.JsonUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public final class ConfigParser {

    private Config config;

    private final String configPath = Grasscutter.getConfig().folderStructure.plugins + "BuildSwitcher";
    private final File configFile = new File(this.configPath + "/config.json");

    public ConfigParser() {
        this.loadConfig();
    }

    public Config getConfig() {
        return this.config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void loadConfig() {
        try (FileReader file = new FileReader(this.configFile)) {
            this.config = JsonUtils.loadToClass(file, Config.class);
            Grasscutter.getLogger().info("[BuildSwitcher] Config Loaded!");
        } catch (Exception e) {
            this.config = new Config();
            this.config.setDefault();
            Grasscutter.getLogger().info("[BuildSwitcher] Basic config creating...");
        }

        if (!saveConfig()) {
            Grasscutter.getLogger().error("[BuildSwitcher] Unable to save config file.");
        }

        Grasscutter.getLogger().info("[BuildSwitcher] Plugin loaded!");
    }

    public boolean saveConfig() {
        File dir = new File(this.configPath);

        if (!dir.exists() || !dir.isDirectory()) {
            if (!new java.io.File(String.valueOf(dir)).mkdirs())
                return false;
        }

        try (FileWriter file = new FileWriter(this.configFile)) {
            file.write(JsonUtils.encode(this.config));
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
