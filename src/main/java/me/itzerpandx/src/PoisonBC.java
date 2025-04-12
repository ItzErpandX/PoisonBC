package me.itzerpandx.src;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PoisonBC extends JavaPlugin {

    private FileConfiguration config;
    private MiniMessage miniMessage;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = this.getConfig();
        miniMessage = MiniMessage.miniMessage();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("poisonbcreload")) {
            this.reloadConfig();
            config = this.getConfig();
            sender.sendMessage(miniMessage.deserialize("<green>PoisonBC configuration reloaded."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(miniMessage.deserialize("<red>Usage: /" + label + " <message>"));
            return false;
        }

        String rawMessage = String.join(" ", args);
        Component message = miniMessage.deserialize(rawMessage);
        Component header = miniMessage.deserialize(config.getString("title-header", "<dark_purple><bold>Broadcast"));
        Component bcheader = miniMessage.deserialize(config.getString("broadcast-header", "<dark_purple><bold>Broadcast"));

        int fadeIn = config.getInt("fade-in", 10);
        int stay = config.getInt("stay", 70);
        int fadeOut = config.getInt("fade-out", 20);

        String soundName = config.getString("sound", "entity.player.levelup");
        float volume = (float) config.getDouble("volume", 1.0);
        float pitch = (float) config.getDouble("pitch", 1.0);

        if (command.getName().equalsIgnoreCase("announcement")) {
            Component announcementMessage = bcheader.append(Component.space()).append(message);
            Bukkit.getServer().broadcast(announcementMessage);

            sendTitleAndSoundToPlayers(header, message, fadeIn, stay, fadeOut, soundName, volume, pitch);
            return true;
        } else if (command.getName().equalsIgnoreCase("bc")) {
            Component announcementMessage = bcheader.append(Component.space()).append(message);
            Bukkit.getServer().broadcast(announcementMessage);
            playSoundToPlayers(soundName, volume, pitch);
            return true;
        }

        return false;
    }

    private void sendTitleAndSoundToPlayers(Component header, Component message, int fadeIn, int stay, int fadeOut, String soundName, float volume, float pitch) {
        Sound sound;
        try {
            sound = Sound.sound(
                    Key.key(soundName),
                    Sound.Source.MASTER,
                    volume,
                    pitch
            );
        } catch (IllegalArgumentException e) {
            getLogger().severe("Invalid sound name in config: " + soundName);
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            Audience audience = (Audience) player;
            audience.showTitle(net.kyori.adventure.title.Title.title(
                    header,
                    message,
                    net.kyori.adventure.title.Title.Times.times(
                            java.time.Duration.ofMillis(fadeIn * 50L),
                            java.time.Duration.ofMillis(stay * 50L),
                            java.time.Duration.ofMillis(fadeOut * 50L)
                    )
            ));
            audience.playSound(sound);
        }
    }

    private void playSoundToPlayers(String soundName, float volume, float pitch) {
        Sound sound;
        try {
            sound = Sound.sound(
                    Key.key(soundName),
                    Sound.Source.MASTER,
                    volume,
                    pitch
            );
        } catch (IllegalArgumentException e) {
            getLogger().severe("Invalid sound name in config: " + soundName);
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            Audience audience = (Audience) player;
            audience.playSound(sound);
        }
    }
}