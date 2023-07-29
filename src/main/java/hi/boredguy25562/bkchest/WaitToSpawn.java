package hi.boredguy25562.bkchest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WaitToSpawn implements Runnable{
    @Override
    public void run() {
        ArrayList<String> arrayList= BKchest.instance.waitToSpawnList;
        for(String string:BKchest.instance.waitToSpawnList){
            String[] strings=StaticFunction.unTurn(string);
            Location location=new Location(Bukkit.getWorld(BKchest.instance.world),Integer.valueOf(strings[0]), Integer.valueOf(strings[1]), Integer.valueOf(strings[2]));
            for(Player p:Bukkit.getOnlinePlayers()){
                if(p.getLocation().distanceSquared(location)<=400){
                    return;
                }
            }
            location.getBlock().setType(Material.CRIMSON_NYLIUM);
            for(Player p:Bukkit.getOnlinePlayers()){
                if(p.isOp()){
                    p.sendMessage(ChatColor.GREEN+"[后室]"+"在坐标:"+StaticFunction.turn(location)+" 生成了一个补给箱。");
                }
            }
            arrayList.remove(string);
        }
        BKchest.instance.waitToSpawnList=arrayList;
    }
}
