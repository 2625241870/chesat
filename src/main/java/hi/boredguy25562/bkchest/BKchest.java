package hi.boredguy25562.bkchest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public final class BKchest extends JavaPlugin implements Listener {
    int Taskid;
    int PlayerCount=1;
    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this,this);
        Taskid=Bukkit.getScheduler().runTaskTimer(this,new BKrun(),100,1000).getTaskId();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public ItemStack spawnItem(){
        ItemStack item = null;
        int randomNumber = (int)(Math.random()*10);
            if(randomNumber==0) {
                item = new ItemStack(Material.POTION);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW + "杏仁水"); // 设置显示名称
                meta.setLore(Collections.singletonList(ChatColor.GRAY + "一种自然生成的重要物品")); // 杏仁水简短的描述
                item.setItemMeta(meta);
            }else if(randomNumber==1){
                item = new ItemStack(Material.POTION);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW + "杏仁水"); // 设置显示名称
                meta.setLore(Collections.singletonList(ChatColor.GRAY + "一种自然生成的重要物品")); // 杏仁水简短的描述
                item.setItemMeta(meta);
            }else if (randomNumber==2){
                item = new ItemStack(Material.IRON_INGOT);
            }else if (randomNumber==3){
                item = new ItemStack(Material.BREAD);
            }else if (randomNumber==4){
                item = new ItemStack(Material.STICK);
            }else if (randomNumber==5){
                item = new ItemStack(Material.STICK);
            }else if (randomNumber==6){
                item = new ItemStack(Material.COBBLESTONE);
            }else if (randomNumber==7){
                item = new ItemStack(Material.LEATHER);
            }
        return item;
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void playerbrokeChest(BlockBreakEvent event){
        if(event.getBlock().getType().equals(Material.CRIMSON_NYLIUM)){
            while (true){
                ItemStack item=spawnItem();
                if(item!=null){
                    event.getBlock().getWorld().dropItem(event.getBlock().getLocation(),item);
                }else{
                    break;
                }
            }
            event.getBlock().setType(Material.AIR);
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        if(Math.abs(PlayerCount-Bukkit.getOnlinePlayers().size())>=3){
            PlayerCount = Bukkit.getOnlinePlayers().size();
            Bukkit.getScheduler().cancelTask(Taskid);
            Taskid=Bukkit.getScheduler().runTaskTimer(this,new BKrun(),10,1000/Bukkit.getOnlinePlayers().size()).getTaskId();
        }
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        if(Math.abs(PlayerCount-Bukkit.getOnlinePlayers().size())>=3) {
            PlayerCount = Bukkit.getOnlinePlayers().size();
            Bukkit.getScheduler().cancelTask(Taskid);
            Taskid = Bukkit.getScheduler().runTaskTimer(this, new BKrun(), 10, 1000 / Bukkit.getOnlinePlayers().size()).getTaskId();
        }
        }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.hasPermission("bk")) {
            if (command.getName().equalsIgnoreCase("Bkchest")) {
                int randomX= (int)((Math.random() * 2001) - 1000);
                int randomZ= (int) ((Math.random() * 2001) - 1000);
                Location location=new Location(Bukkit.getWorld("world"),randomX,0,randomZ);
                boolean isLastBlockAIR = false;
                for(int y=0;y<255;y++) {
                    if (!location.getBlock().getType().equals(Material.AIR)) {
                        location.setY(y);
                        isLastBlockAIR=true;
                    }else if(isLastBlockAIR) {
                        if (((int)(Math.random()+0.5))==1) {
                            location.getBlock().setType(Material.CRIMSON_NYLIUM);
                            BlockState block= location.getBlock().getState();
                            block.update();
                            for(int i=1;i<10;i++){
                                Location loc=location.clone();
                                loc.setX(location.getX()+i);
                                if(!loc.getBlock().getType().equals(Material.AIR)&&!loc.getBlock().getType().isSolid()){
                                 location.setX(loc.getX()-1);
                                 break;
                                }else{
                                    loc.setX(location.getX()-i);
                                }
                                loc.setY(location.getY()+i);
                                if(!loc.getBlock().getType().equals(Material.AIR)&&!loc.getBlock().isSolid()){
                                    location.setY(location.getY()-1);
                                    break;
                                }
                                if(i==9){
                                    for(Player p:Bukkit.getOnlinePlayers()){
                                        if(p.isOp()){
                                            p.sendMessage("[后室]在坐标:"+location.getX()+","+location.getY()+","+location.getZ()+"生成补给箱失败...未找到合适的位置");
                                        }
                                    }
                                    return true;
                                }
                            }
                            for(Player p:Bukkit.getOnlinePlayers()){
                                if(p.isOp()){
                                    p.sendMessage("[后室]在坐标:"+location.getX()+","+location.getY()+","+location.getZ()+"生成了一个补给箱,此消息仅管理可见...");
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }
}
