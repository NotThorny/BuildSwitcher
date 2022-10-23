package thorny.grasscutters.BuildSwitcher;

import emu.grasscutter.plugin.Plugin;
import thorny.grasscutters.BuildSwitcher.utils.*;

public final class BuildSwitcher extends Plugin {
    private static BuildSwitcher instance;
    public ConfigParser config;

    public static BuildSwitcher getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        // Set the plugin instance.
        instance = this;
        this.config = new ConfigParser();
    }

    @Override
    public void onEnable() {

        // Register commands.
        this.getHandle().registerCommand(new thorny.grasscutters.BuildSwitcher.commands.BuildSwitcherCommand());

        // Log a plugin status message.
        this.getLogger().info("The Build Switcher plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        // Log a plugin status message.
        this.getLogger().info("Build Switcher has been disabled.");
    }

    public void getConfig() {
        config.getConfig();
    }

    public void reloadConfig(Config updated) {
        config.loadConfig(updated);
    }
}