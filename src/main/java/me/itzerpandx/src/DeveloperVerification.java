package me.itzerpandx.src;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DeveloperVerification implements Listener {

    private final JavaPlugin plugin;

    public DeveloperVerification(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getName().equals("ItzErpandX")) {
            event.getPlayer().sendMessage(" ");
            event.getPlayer().sendMessage(ChatColor.GREEN + "This server uses PoisonBC!");
            event.getPlayer().sendMessage(" ");
        }
    }
}
