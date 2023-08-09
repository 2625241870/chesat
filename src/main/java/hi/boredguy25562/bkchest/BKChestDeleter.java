package hi.boredguy25562.bkchest;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Objects;

public class BKChestDeleter implements Runnable {
    @Override
    public void run() {
        HashMap<String, Long> map = (HashMap<String, Long>) BKchest.instance.chestMap.clone();
        for (String key : BKchest.instance.chestMap.keySet()) {
            if (System.currentTimeMillis() - BKchest.instance.chestMap.get(key) >= 300000) {
                String[] strings = StaticFunction.unTurn(key);
                Objects.requireNonNull(Bukkit.getWorld(BKchest.instance.world)).getBlockAt(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]), Integer.valueOf(strings[2])).setType(Material.AIR);
                map.remove(key);
            }
        }
        BKchest.instance.chestMap =map;
    }
}
