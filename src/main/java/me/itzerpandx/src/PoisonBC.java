package me.itzerpandx.src;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PoisonBC extends JavaPlugin {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = this.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("poisonbcreload")) {
            this.reloadConfig();
            config = this.getConfig();
            sender.sendMessage(ChatColor.GREEN + "PoisonBC configuration reloaded.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /" + label + " <message>");
            return false;
        }

        String message = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
        String header = ChatColor.translateAlternateColorCodes('&', config.getString("title-header", "&5&lBroadcast"));
        String bcheader = ChatColor.translateAlternateColorCodes('&', config.getString("broadcast-header", "&5&lBroadcast"));
        int fadeIn = config.getInt("fade-in", 10);
        int stay = config.getInt("stay", 70);
        int fadeOut = config.getInt("fade-out", 20);

        String soundName = config.getString("sound", "ENTITY_PLAYER_LEVELUP");
        float volume = (float) config.getDouble("volume", 1.0);
        float pitch = (float) config.getDouble("pitch", 1.0);

        if (command.getName().equalsIgnoreCase("announcement")) {
            String announcementMessage = bcheader + " " + message;
            Bukkit.broadcastMessage(announcementMessage);

            sendTitleAndSoundToPlayers(header, message, fadeIn, stay, fadeOut, soundName, volume, pitch);
            return true;
        } else if (command.getName().equalsIgnoreCase("bc")) {
            String announcementMessage = bcheader + " " + message;
            Bukkit.broadcastMessage(announcementMessage);
            playSoundToPlayers(soundName, volume, pitch);
            return true;
        }

        return false;
    }

    private void sendTitleAndSoundToPlayers(String header, String message, int fadeIn, int stay, int fadeOut, String soundName, float volume, float pitch) {
        Sound sound;
        try {
            sound = Sound.valueOf(soundName);
        } catch (IllegalArgumentException e) {
            getLogger().severe("Invalid sound name in config: " + soundName);
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(header, message, fadeIn, stay, fadeOut);
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    private void playSoundToPlayers(String soundName, float volume, float pitch) {
        Sound sound;
        try {
            sound = Sound.valueOf(soundName);
        } catch (IllegalArgumentException e) {
            getLogger().severe("Invalid sound name in config: " + soundName);
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }
}
