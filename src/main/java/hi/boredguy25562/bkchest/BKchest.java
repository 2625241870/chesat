package hi.boredguy25562.bkchest;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public final class BKchest extends JavaPlugin implements Listener {
    public static BKchest instance;
    double gailu = 1;
    int Taskid;
    int PlayerCount = 1;
    int baodi = 1;
    int zuida = 10;
    int MaxRadius = 1000;
    int YStartAt = 0;
    int searchRadiusMax = 15;
    int maxY;
    Boolean ia = false;
    ArrayList<String> waitToSpawnList = new ArrayList<>();
    String world = "world";
    ArrayList<BKitem> list = new ArrayList<>();
    HashMap<String, Long> chestMap = new HashMap<>();

    {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup
        saveResource("items.yml", false);
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().runTaskTimer(this, new BKChestDeleter(), 1000, 500);
        Bukkit.getScheduler().runTaskTimer(this, new WaitToSpawn(), 1000, 100);
        Taskid = Bukkit.getScheduler().runTaskTimer(this, new BKrun(), 1000, 1000).getTaskId();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            try {
                Class.forName("dev.lone.itemsadder.api.ItemsAdder");
                ia = true;
                Bukkit.getLogger().info("软前置IA已经加载.");
            } catch (Exception e) {
                Bukkit.getLogger().info("没有找到IA插件，此消息可被忽略.");
            }
            try {
                YMLLoader.loadYml();
                Bukkit.getLogger().info("加载items.yml成功!");
            } catch (IOException e) {
                Bukkit.getLogger().info("加载items.yml时出现错误...");
            }

        }, 200);

    }

    public ItemStack spawnItem(int ran) {
        double sum = 0;
        for (BKitem bKitem : list) {//循环遍历元素并判断得出概率。
            if (new Random().nextInt(ran) < bKitem.weight + sum) {
                try {
                    if (bKitem.isIA) {
                        return CustomStack.getInstance(bKitem.IAname).getItemStack();
                    }
                } catch (Exception e) {
                    Bukkit.getLogger().info("生成IA物品时失败了...");
                }
                return bKitem.itemStack;//随机生成
            }
            sum = sum + bKitem.weight;
        }
        return null;//啥都没抽到返回null
    }

    @EventHandler(priority = EventPriority.HIGHEST)//高优先级
    public void playerBrokeChestEvent(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.CRIMSON_NYLIUM)||event.getBlock().getType().equals(Material.NETHERRACK)) {
            int b = 0;
            while (b < zuida) {
                int ran = 101;
                if (b < baodi) ran = (int) gailu;
                ItemStack item = spawnItem(ran);
                if (item != null) event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
                else break;
                b++;
            }
            event.getBlock().setType(Material.AIR);
        }
    }

    public void refreshPlayer() {//当玩家变化数量超过3时变化一次生成速率。
        if (Math.abs(PlayerCount - Bukkit.getOnlinePlayers().size()) >= 3) {
            PlayerCount = Bukkit.getOnlinePlayers().size();
            Bukkit.getScheduler().cancelTask(Taskid);
            Taskid = Bukkit.getScheduler().runTaskTimer(this, new BKrun(), 10, 1000 / Bukkit.getOnlinePlayers().size()).getTaskId();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        refreshPlayer();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        refreshPlayer();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("bk")) {
            if (command.getName().equalsIgnoreCase("Bkchest")) {
                new BKrun().run();
            } else if (command.getName().equalsIgnoreCase("bkreload")) {
                try {
                    YMLLoader.loadYml();
                } catch (Exception ignored) {
                }
            }
        }
        return true;
    }
}
