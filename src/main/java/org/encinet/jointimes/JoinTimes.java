package org.encinet.jointimes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class JoinTimes extends JavaPlugin implements Listener {
    private FileConfiguration playerData;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getCommand("jointimes")).setExecutor(this);
        File configFile = new File(getDataFolder(), "playerData.yml");
        if (!configFile.exists()) {
            if (configFile.getParentFile().mkdirs()) {
                saveResource("playerData.yml", false);
            } else {
                getLogger().warning("无法创建配置文件目录！");
            }
        }
        playerData = YamlConfiguration.loadConfiguration(configFile);
        getLogger().info("插件已启动");
    }

    @Override
    public void onDisable() {
        try {
            playerData.save(new File(getDataFolder(), "playerData.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        String name = e.getPlayer().getName();
        if (!playerData.contains(name)) {
            playerData.set(name, 1);
        } else {
            playerData.set(name, playerData.getInt(name));
        }
        e.getPlayer().sendMessage("你已经进入服务器 " + playerData.getInt(name) + " 次");
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        int times = playerData.getInt(sender.getName());
        sender.sendMessage("你进入服务器的次数为：" + times + "\nPS:这个计时是从2023/7/18开始的");
        return true;
    }
}
