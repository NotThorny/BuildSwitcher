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
import emu.grasscutter.command.Command.TargetRequirement;

import java.util.ArrayList;
import java.util.List;

// Command usage
@Command(label = "set", aliases = "builds", usage = "save|load|list <name>", targetRequirement = TargetRequirement.PLAYER)
public class BuildSwitcherCommand implements CommandHandler {
    private static final Config config = BuildSwitcher.getInstance().config.getConfig();

    public void execute(Player sender, Player targetPlayer, List<String> args) {

        /*
         * Allows saving and loading of builds
         * Builds only include artifacts currently
         * As weapons checking would be needed
         */

        String state = args.get(0);
        String buildName = "";
        if (args.size() > 1){
            buildName = args.get(1);
        }
        var avatar = targetPlayer.getTeamManager().getCurrentAvatarEntity().getAvatar();

        switch (state) {
            case "save" -> {
                if (config.getBuilds() != null) {
                    for (Builds build : config.getBuilds()) {
                        if (build.getName().equals(buildName)) {
                            // Save each item equipped
                            build.setBracer(setArtifactData(avatar.getEquipBySlot(EquipType.EQUIP_BRACER)));
                            build.setDress(setArtifactData(avatar.getEquipBySlot(EquipType.EQUIP_DRESS)));
                            build.setNecklace(setArtifactData(avatar.getEquipBySlot(EquipType.EQUIP_NECKLACE)));
                            build.setRing(setArtifactData(avatar.getEquipBySlot(EquipType.EQUIP_RING)));
                            build.setShoes(setArtifactData(avatar.getEquipBySlot(EquipType.EQUIP_SHOES)));

                            if (updateConfig()) {
                                CommandHandler.sendMessage(targetPlayer, buildName + " saved!");
                                return;
                            }
                        }
                    };
                }

                Builds newBuild = new Builds();
                newBuild.setName(buildName);
                newBuild.setBracer(setArtifactData(avatar.getEquipBySlot(EquipType.EQUIP_BRACER)));
                newBuild.setDress(setArtifactData(avatar.getEquipBySlot(EquipType.EQUIP_DRESS)));
                newBuild.setNecklace(setArtifactData(avatar.getEquipBySlot(EquipType.EQUIP_NECKLACE)));
                newBuild.setRing(setArtifactData(avatar.getEquipBySlot(EquipType.EQUIP_RING)));
                newBuild.setShoes(setArtifactData(avatar.getEquipBySlot(EquipType.EQUIP_SHOES)));

                config.getBuilds().add(newBuild);

                // Update config file
                if (updateConfig()) {
                    CommandHandler.sendMessage(targetPlayer, "Saved new build " + buildName);
                } else {
                    CommandHandler.sendMessage(targetPlayer, "Failed to save build!! :(");
                }

            }
            case "load" -> {
                // Get the build from config
                for (Builds build : config.getBuilds()) {
                    if (build.getName().equals(buildName)) {

                        // Add all items to array
                        ArrayList<ArtifactData> items = new ArrayList<>();
                        items.add(build.getBracer());
                        items.add(build.getDress());
                        items.add(build.getNecklace());
                        items.add(build.getRing());
                        items.add(build.getShoes());

                        // Equip existing item or create new one
                        for (ArtifactData data : items) {

                            if (data != null && data.getItemId() > 0) {
                                var equip = new GameItem(data.getItemId());
                                equip.setMainPropId(data.getMainPropId());
                                equip.setLevel(data.getLevel());
                                var props = equip.getAppendPropIdList();
                                props.clear();
                                props.addAll(data.getAppendPropIdList());
                                if (targetPlayer.getInventory().getItemByGuid(equip.getGuid()) == null) {
                                    targetPlayer.getInventory().addItem(equip, ActionReason.SubfieldDrop);
                                }
                                avatar.equipItem(equip, true);
                            } else {
                                
                            }
                        }

                        // Save an clean up list
                        targetPlayer.getInventory().save();
                        avatar.save();
                        items.clear();

                        CommandHandler.sendMessage(targetPlayer, "Equipped build: " + buildName);
                        return;
                    }
                };
                CommandHandler.sendMessage(targetPlayer, "Build \"" + buildName + "\" not found!");
            }
            case "list" -> {
                List<String> names = new ArrayList<>();
                config.getBuilds().forEach(build -> {
                    names.add(build.getName());
                });
                CommandHandler.sendMessage(targetPlayer, "All builds saved: " + names);
            }
            default -> {
                sendUsageMessage(targetPlayer);
            }
        }
    }

    public ArtifactData setArtifactData(GameItem artifact) {
        if (artifact == null) {
            return new ArtifactData();
        }
        var data = new ArtifactData();
        data.setItemId(artifact.getItemId());
        data.setLevel(artifact.getLevel());
        data.setMainPropId(artifact.getMainPropId());
        data.setAppendPropIdList(artifact.getAppendPropIdList());
        return data;
    }

    // Update config file, from Grasscutter config writer
    public boolean updateConfig() {
        if ( // Save configuration & reload.
        BuildSwitcher.getInstance().reloadConfig(config)) {
            return true;
        } else {
            Grasscutter.getLogger().warn("Failed to update config!!");
            return false;
        }
    }
} // BuildSwitcherCommand
