package me.illusion.outcastmoderation.Util.Config;

import me.illusion.outcastmoderation.Moderation.Punishments.Cooldown;
import me.illusion.outcastmoderation.Moderation.Punishments.Punish;
import me.illusion.outcastmoderation.Moderation.Utilities;

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

        this.config.setDefault("moderation.commands.warn.message", "You have been warned for #REASON by #MODERATOR.");
        this.config.setDefault("moderation.commands.mute.message", "You have been muted for #REASON for #TIME by #MODERATOR.");
        this.config.setDefault("moderation.commands.ban.message", "You have been banned for #REASON for #TIME by #MODERATOR.");
        this.config.setDefault("moderation.commands.kick.message", "You have been kicked from the server for #REASON by #MODERATOR.");

        this.config.setDefault("moderation.commands.vanish.enable-message", "You are now in vanish.");
        this.config.setDefault("moderation.commands.vanish.disable-message", "You are no longer in vanish.");

        this.config.setDefault("moderation.commands.delaychat.message", "Chat has been delayed for #TIME.");

        this.config.setDefault("moderation.commands.lockchat.enable-message", "Chat has been locked.");
        this.config.setDefault("moderation.commands.lockchat.disable-message", "Chat has been unlocked.");

        this.config.setDefault("moderation.commands.nick.enable-message", "You have now been nicked as #NICK.");
        this.config.setDefault("moderation.commands.nick.disable-message", "You are no longer disguised with a nick.");

        this.config.setDefault("moderation.commands.freeze.enable-message", "You have frozen #IGN.");
        this.config.setDefault("moderation.commands.freeze.disable-message", "You have unfroze #IGN.");
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
}
