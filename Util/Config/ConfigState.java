package me.illusion.outcastmoderation.Util.Config;

import me.illusion.outcastmoderation.Moderation.Punishments.Cooldown;
import me.illusion.outcastmoderation.Moderation.Punishments.Punish;
import me.illusion.outcastmoderation.Moderation.Utilities;
import org.bukkit.ChatColor;

public class ConfigState {
    private CreateConfig config;

    public ConfigState(CreateConfig config) {
        this.config = config;
    }

    public void loadConfig() {
        this.config.loadMapLong("punishment.ban.players", Punish.getBannedPlayerList());
        this.config.loadMap("punishment.ban.reasons", Punish.getBanReasonList());
        this.config.loadMap("punishment.warn.players", Punish.getWarnedPlayerList());
        this.config.loadMapLong("punishment.mute.players", Punish.getMutedPlayerList());
        this.config.loadMapLong("chat.delay", Punish.getChatDelayList());
        this.config.loadMapLong("punishment.cooldowns", Cooldown.getCooldownList());
        this.config.loadMap("chat.nicknames", Utilities.getNickedPlayerList());
        this.config.loadMapLong("anti-xray.cooldown", Punish.getMineBannedList());
    }

    public void saveConfig() {
        if (!Punish.getBannedPlayerList().isEmpty())
            this.config.saveMap("punishment.ban.players", Punish.getBannedPlayerList());

        if (!Punish.getBanReasonList().isEmpty())
            this.config.saveMap("punishment.ban.reasons", Punish.getBanReasonList());

        if (!Punish.getWarnedPlayerList().isEmpty())
            this.config.saveMap("punishment.warn.players", Punish.getWarnedPlayerList());

        if (!Punish.getMutedPlayerList().isEmpty())
            this.config.saveMap("punishment.mute.players", Punish.getMutedPlayerList());

        if (!Punish.getChatDelayList().isEmpty())
            this.config.saveMap("chat.delay", Punish.getChatDelayList());

        if (!Cooldown.getCooldownList().isEmpty())
            this.config.saveMap("punishment.cooldowns", Cooldown.getCooldownList());

        if (!Utilities.getNickedPlayerList().isEmpty())
            this.config.saveMap("chat.nicknames", Utilities.getNickedPlayerList());

        if (!Punish.getMineBannedList().isEmpty())
            this.config.saveMap("anti-xray.cooldown", Punish.getMineBannedList());
    }

    public void configDefaults() {
        this.config.setDefault("moderation.prefix", "&5&bModeration");
        this.config.setDefault("moderation.permissions.reload", "oc.moderation.reload");
    }

    public void loadStaffConfig() {
        this.config.setDefault("moderation.permissions.staff-chat", "oc.moderation.staff.chat");
        this.config.setDefault("moderation.staff-chat.prefix", "&cStaff");

        this.config.setDefault("moderation.permissions.lockchat-bypass", "oc.moderation.lockchat.bypass");
        this.config.setDefault("moderation.permissions.chatdelay-bypass", "oc.moderation.chatdelay.bypass");
    }

    public void loadAntiXrayConfig() {
        this.config.setDefault("moderation.permissions.antixray-bypass", "oc.moderation.antixray.bypass");
        this.config.setDefault("moderation.antixray.cooldown-time", "60");

        this.config.setDefault("ANCIENT_DEBRIS.threshold-items", "2");
        this.config.setDefault("ANCIENT_DEBRIS.threshold-time", "20");
        this.config.setDefault("EMERALD_ORE.threshold-items", "3");
        this.config.setDefault("EMERALD_ORE.threshold-time", "25");
        this.config.setDefault("DIAMOND_ORE.threshold-items", "10");
        this.config.setDefault("DIAMOND_ORE.threshold-time", "30");
        this.config.setDefault("GOLD_ORE.threshold-items", "15");
        this.config.setDefault("GOLD_ORE.threshold-time", "25");
        this.config.setDefault("IRON_ORE.threshold-items", "20");
        this.config.setDefault("IRON_ORE.threshold-time", "25");
    }

    public void configChatMessages() {
        this.config.setDefault("chat.command.warning", "&7You have been warned for '&c#REASON&7' by &c#STAFF&7!");
        this.config.setDefault("chat.command.mute", "You have been muted for #REASON for #TIME by #MODERATOR.");
        this.config.setDefault("chat.command.ban", "You have been banned for #REASON for #TIME by #MODERATOR.");
        this.config.setDefault("chat.command.kick", "\n&7You have been kicked by &c#STAFF&7\nReason&f: &c#REASON\n");

        this.config.setDefault("chat.command.freeze.enable", "&7You have been &cfrozen&7 by &c#STAFF&7...");
        this.config.setDefault("chat.command.freeze.disable", "&7You have been &aunfrozen&7 by &a#STAFF&7.");

        /* */

        this.config.setDefault("chat.error.ban.incorrect-command-use", "&cIncorrect command usage. Please use /ban <name> <time> <reason>");
        this.config.setDefault("chat.error.ban-message", "&cUnable to ban the specified player '#IGN'.");
        this.config.setDefault("chat.success.ban-message", "&a#IGN has been successfully banned.");

        this.config.setDefault("chat.error.freeze.incorrect-command-use", "&cIncorrect command usage. Please use /freeze <name>");
        this.config.setDefault("chat.error.freeze.player-not-found", "&cThere is no player named #IGN.");
        this.config.setDefault("chat.error.freeze.same-player", "&cYou can't freeze yourself...");
        this.config.setDefault("chat.error.freeze-message", "&cUnable to freeze the specified player '#IGN'.");
        this.config.setDefault("chat.success.freeze-message", "&aYou have toggled #IGN's frozen state.");

        this.config.setDefault("chat.error.invsee.incorrect-command-use", "&cIncorrect command usage. Please use /invsee <name>");
        this.config.setDefault("chat.error.invsee.player-not-found", "&cThere is no player named #IGN.");
        this.config.setDefault("chat.success.invsee-message", "&7Now viewing the inventory of &c#IGN&7...");

        this.config.setDefault("chat.error.kick.incorrect-command-use", "&cIncorrect command usage. Please use /kick <name> <reason>");

        this.config.setDefault("chat.error.mute.incorrect-command-use", "&cIncorrect command usage. Please use /mute <name> <time> <reason>");
        this.config.setDefault("chat.error.mute-message", "&cUnable to mute the specified player '#IGN'.");
        this.config.setDefault("chat.success.mute-message", "&a#IGN has been successfully muted.");

        this.config.setDefault("chat.error.nick.incorrect-command-use", "&cIncorrect command usage. Please use /nick <nickname>");
        this.config.setDefault("chat.error.nick-message", "&cUnable to set your nickname.");
        this.config.setDefault("chat.error.nick-remove", "&cThere was a problem removing your nickname.");
        this.config.setDefault("chat.success.nick-message", "&7You have now been nicknamed #NICK&7.");
        this.config.setDefault("chat.success.nick-remove", "&7You no longer have a nickname.");

        this.config.setDefault("chat.error.reload.incorrect-command-use", "&cIncorrect command usage. Please use /om [reload]");
        this.config.setDefault("chat.error.reload.insufficient-permission", "&cYou don't have permission to do this.");
        this.config.setDefault("chat.success.reload-message", "&aConfigs reloaded.");

        this.config.setDefault("chat.error.staff.incorrect-command-use", "&cIncorrect command usage. Please use /staff <message>");

        this.config.setDefault("chat.error.tp.incorrect-command-use", "&cIncorrect command usage. Please use /tp <name>");
        this.config.setDefault("chat.error.tp.player-not-found", "&cThere is no player named #IGN.");
        this.config.setDefault("chat.error.tp.same-player", "&cYou can't teleport to yourself...");
        this.config.setDefault("chat.error.tp-message", "&cUnable to teleport to the specified player '#IGN'.");
        this.config.setDefault("chat.success.tp-message", "&7Teleported to &c&l#IGN&7...");

        this.config.setDefault("chat.error.vanish-toggle.self", "&cUnable to vanish / unvanish yourself.");
        this.config.setDefault("chat.error.vanish-toggle.player", "&cUnable to vanish / unvanish '#IGN'.");
        this.config.setDefault("chat.success.vanish-toggle.self", "&7You have toggled your vanish status: #VANISH");
        this.config.setDefault("chat.success.vanish-toggle.player", "&7You have toggled #IGN's vanish status: #VANISH");

        this.config.setDefault("chat.error.warn.incorrect-command-use", "&cIncorrect command usage. Please use /warn <name> <reason>");
        this.config.setDefault("chat.error.warn-message", "&cUnable to warn the specified player '#IGN'.");
        this.config.setDefault("chat.success.warn-message", "&a#IGN has been successfully warned.");

        this.config.setDefault("chat.error.delaychat", "&7You can only send messages once every &c#TIME&7.");
        this.config.setDefault("chat.success.delaychat", "&7A &c#TIME&7 delay has been added to the chat.");

        /* */

        this.config.setDefault("punish.ban.remind-user", "&c\nYou are currently banned!&7\n\nReason&f:&e #REASON&7\n\nDuration&f:&e #TIME\n ");
        this.config.setDefault("punish.ban-permanent.remind-user", "&c\nYou have been permanently banned!&7\n\nReason&f:&e #REASON\n ");

        this.config.setDefault("punish.mute.remind-user", "&cYou are still muted for #TIME");

        /* */

        this.config.setDefault("staff.alert.antixray", "&7#IGN possible xray (&a#MATERIAL&7). Placed on cooldown for &a#TIME&7.");

        this.config.setDefault("staff.alert.ban-permanent", "&7#IGN has been banned permanently by &a#STAFF&7 for '#REASON'.");
        this.config.setDefault("staff.alert.ban", "&7#IGN has been banned for #TIME&a by #STAFF&7 for '#REASON'.");

        this.config.setDefault("staff.alert.mute", "&7#IGN has been muted for #TIME&a by #STAFF&7 for '#REASON'.");

        this.config.setDefault("staff.alert.vanish.disable", "&7#IGN removed their vanish.");
        this.config.setDefault("staff.alert.vanish.enable", "&7#IGN has entered vanish.");

        this.config.setDefault("staff.alert.warn", "&7#IGN has been warned &aby #STAFF&7 for '#REASON'.");

        this.config.setDefault("staff.alert.freeze.enable", "&7#IGN has been frozen &aby #STAFF&7.");
        this.config.setDefault("staff.alert.freeze.disable", "&7#IGN has been unfrozen &aby #STAFF&7.");
    }
}
