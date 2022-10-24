package thorny.grasscutters.BuildSwitcher.commands;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.command.Command;
import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.game.inventory.EquipType;
import emu.grasscutter.game.inventory.GameItem;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.game.props.ActionReason;
import thorny.grasscutters.BuildSwitcher.BuildSwitcher;
import thorny.grasscutters.BuildSwitcher.utils.*;
import thorny.grasscutters.BuildSwitcher.utils.Config.builds;
import emu.grasscutter.command.Command.TargetRequirement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Command usage
@Command(label = "build", aliases = "set", usage = "save|load [a - z]", targetRequirement = TargetRequirement.NONE)
public class BuildSwitcherCommand implements CommandHandler {
    private static final Config config = BuildSwitcher.getInstance().config.getConfig();

    public void execute(Player sender, Player targetPlayer, List<String> args) {

        /*
         * Allows saving and loading of builds
         * Builds only include artifacts currently
         * As weapons checking would be needed
         * Currently equipped artifacts will be swapped to the characters that have the set artis equipped
        */

        String state = args.get(0);
        String buildName = args.get(1);
        var avatar = targetPlayer.getTeamManager().getCurrentAvatarEntity().getAvatar();

        switch (state){
            case "save" ->{
                builds avatarToChange = null;
                try {
                    avatarToChange = getBuild.getCurrent(buildName);
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    CommandHandler.sendMessage(targetPlayer, "No build of name " + buildName + " so the build was not saved!");
                }
                
                // Save each item equipped
                avatarToChange.items.bracer = avatar.getEquipBySlot(EquipType.EQUIP_BRACER);
                avatarToChange.items.dress = avatar.getEquipBySlot(EquipType.EQUIP_DRESS);
                avatarToChange.items.necklace = avatar.getEquipBySlot(EquipType.EQUIP_NECKLACE);
                avatarToChange.items.ring = avatar.getEquipBySlot(EquipType.EQUIP_RING);
                avatarToChange.items.shoes = avatar.getEquipBySlot(EquipType.EQUIP_SHOES);
                
                // Update config file
                updateConfig();
                CommandHandler.sendMessage(targetPlayer, "Build saved!");
            }
            case "load" -> {
                builds avatarToChange = null;

                // Get the build from config
                try {
                    avatarToChange = getBuild.getCurrent(buildName);
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    CommandHandler.sendMessage(targetPlayer, "No build of name " + buildName);
                }

                // Add all items to array
                ArrayList<GameItem> items = new ArrayList<>();
                items.add(avatarToChange.items.bracer);
                items.add(avatarToChange.items.dress);
                items.add(avatarToChange.items.necklace);
                items.add(avatarToChange.items.ring);
                items.add(avatarToChange.items.shoes);

                // Equip existing item or create new one
                for (GameItem equip : items) {
                    if(equip != null){
                        if(targetPlayer.getInventory().getItemByGuid(equip.getGuid()) == null){
                            targetPlayer.getInventory().addItem(equip, ActionReason.SubfieldDrop);
                        }
                        avatar.equipItem(equip, true);
                    }
                }

                // Save an clean up list
                targetPlayer.getInventory().save();
                avatar.save();
                items.clear();
                
                CommandHandler.sendMessage(targetPlayer, "Equipped build: " + buildName);
            }
        }
    }

    // Get builds from config file
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

    // Update config file, from Grasscutter config writer
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
            Grasscutter.getLogger().warn("Failed to update ", exception);
        }
        
    }
} // BuildSwitcherCommand
