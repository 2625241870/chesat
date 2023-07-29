package hi.boredguy25562.bkchest;

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
    int MaxRadius=1000;
    int YStartAt=0;
    int searchRadiusMax=15;
    ArrayList<String> waitToSpawnList= new ArrayList<>();
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
        Bukkit.getScheduler().runTaskTimer(this,new WaitToSpawn(),1000,100);
        Taskid = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new BKrun(), 1000, 1000).getTaskId();
        try {
            YMLLoader.loadYml();
        } catch (Exception ignored) {
        }
    }

    public ItemStack spawnItem(int ran) {
        double sum = 0;
        for (BKitem bKitem : list) {//循环遍历元素并判断得出概率。
            if (new Random().nextInt(ran) < bKitem.weight + sum)return bKitem.itemStack;//随机生成
            sum = sum + bKitem.weight;
        }
        return null;//啥都没抽到返回null
    }

    @EventHandler(priority = EventPriority.HIGH)//高优先级
    public void playerBrokeChestEvent(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.CRIMSON_NYLIUM)) {
            int b = 0;
            while (b < zuida) {
                int ran=101;
                if(b<baodi)ran=(int)gailu;
                ItemStack item = spawnItem(ran);
                if (item != null)event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);else break;
                b++;
            }
            event.getBlock().setType(Material.AIR);
        }
    }

    public void refreshPlayer() {//当玩家变化数量超过3时变化一次生成速率。
        if (Math.abs(PlayerCount - Bukkit.getOnlinePlayers().size()) >= 3) {
            PlayerCount = Bukkit.getOnlinePlayers().size();
            Bukkit.getScheduler().cancelTask(Taskid);
            Taskid = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new BKrun(), 10, 1000 / Bukkit.getOnlinePlayers().size()).getTaskId();
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
