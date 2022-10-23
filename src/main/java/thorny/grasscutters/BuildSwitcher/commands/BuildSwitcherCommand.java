package thorny.grasscutters.BuildSwitcher.commands;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.command.Command;
import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.game.inventory.EquipType;
import emu.grasscutter.game.player.Player;
import thorny.grasscutters.BuildSwitcher.BuildSwitcher;
import thorny.grasscutters.BuildSwitcher.utils.*;
import thorny.grasscutters.BuildSwitcher.utils.Config.builds;
import emu.grasscutter.command.Command.TargetRequirement;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

// Command usage
@Command(label = "build", aliases = "set", usage = "set|load [name]", targetRequirement = TargetRequirement.NONE)
public class BuildSwitcherCommand implements CommandHandler {
    private static final Config config = BuildSwitcher.getInstance().config.getConfig();

    public void execute(Player sender, Player targetPlayer, List<String> args) {

        /*
         * Allows saving and loading of builds
         * Builds only include artifacts currently
         * As weapons checking would be needed
         * Currently equipped artifacts will be swapped to the characters that have the set artis equipped
        */

        String state = "on";
        String buildName = args.get(1);
        var avatar = targetPlayer.getTeamManager().getCurrentAvatarEntity().getAvatar();

        state = args.get(0);

        switch (state){
            case "save" ->{
                builds avatarToChange = null;
                try {
                    avatarToChange = getBuild.getCurrent(buildName);
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    // Make this work when my peabrain figures it out without fully rewriting all config logic
                    // To add a new field in the config for a build with the specified name
                    // avatarToChange = new builds();
                    CommandHandler.sendMessage(targetPlayer, "No build of name " + buildName + " so the build was not saved!");
                }
                
                avatarToChange.items.bracer = avatar.getEquipBySlot(EquipType.EQUIP_BRACER).getGuid();
                avatarToChange.items.dress = avatar.getEquipBySlot(EquipType.EQUIP_DRESS).getGuid();
                avatarToChange.items.necklace = avatar.getEquipBySlot(EquipType.EQUIP_NECKLACE).getGuid();
                avatarToChange.items.ring = avatar.getEquipBySlot(EquipType.EQUIP_RING).getGuid();
                avatarToChange.items.shoes = avatar.getEquipBySlot(EquipType.EQUIP_SHOES).getGuid();
                
                updateConfig();
                CommandHandler.sendMessage(targetPlayer, "Build saved!");
            }
            case "load" -> {
                builds avatarToChange = null;
                
                try {
                    avatarToChange = getBuild.getCurrent(buildName);
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    CommandHandler.sendMessage(targetPlayer, "No build of name " + buildName);
                }

                var avatarGuid = avatar.getGuid();
                targetPlayer.getInventory().equipItem(avatarGuid, avatarToChange.items.bracer);
                targetPlayer.getInventory().equipItem(avatarGuid, avatarToChange.items.dress);
                targetPlayer.getInventory().equipItem(avatarGuid, avatarToChange.items.necklace);
                targetPlayer.getInventory().equipItem(avatarGuid, avatarToChange.items.ring);
                targetPlayer.getInventory().equipItem(avatarGuid, avatarToChange.items.shoes);
                targetPlayer.getInventory().save();
                
                CommandHandler.sendMessage(targetPlayer, "Equipped build: " + buildName);
            }
        }
    }

    public static class getBuild {
        public static builds getCurrent(String curName)
                throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
            builds curr = new builds();
            var me = config.getClass().getDeclaredField(curName);
            me.setAccessible(true);
            curr = (builds) me.get(config);
            return curr;
        }
    }

    public void updateConfig() {
        // Create a new configuration instance.
        Config updated = new Config();
        // Update all configuration fields.
        Field[] fields = config.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            try {
                field.set(updated, field.get(config));
            } catch (Exception exception) {
                Grasscutter.getLogger().error("Failed to update a configuration field.", exception);
            }
        });

        try { // Save configuration & reload.
            BuildSwitcher.getInstance().reloadConfig(updated);
        } catch (Exception exception) {
            Grasscutter.getLogger().warn("Failed to inject the updated ", exception);
        }
        
    }
} // BuildSwitcherCommand