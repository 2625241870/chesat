package hi.boredguy25562.bkchest;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Random;

public class BKrun implements Runnable {
    @Override
    public void run() {
        Random random = new Random();
        Location location = new Location(Bukkit.getWorld(BKchest.instance.world), random.nextInt((BKchest.instance.MaxRadius * 2) + 1) - BKchest.instance.MaxRadius, BKchest.instance.YStartAt, random.nextInt((BKchest.instance.MaxRadius * 2) + 1) - BKchest.instance.MaxRadius);
        boolean isLastBlockAIR = false;
        double y = BKchest.instance.YStartAt;
        for (int c = 0; c < 100; c++) {
            if (!location.getBlock().getType().isAir()) {
                location.setY(y + c);
                isLastBlockAIR = true;
            } else if (isLastBlockAIR) {//上一个方块不是空气的话
                if (random.nextBoolean()) {
                    int i = 1;
                    while (i < BKchest.instance.searchRadiusMax) {
                        double x = location.getX();
                        location.setX(x + i);
                        if (!location.getBlock().getType().isAir() && !location.getBlock().getType().isSolid()) {
                            location.setX(x + i - 1);//检测到墙
                            break;
                        } else location.setX(x);
                        double z = location.getZ();
                        location.setZ(z + i);
                        if (!location.getBlock().getType().isAir() && !location.getBlock().getType().isSolid()) {
                            location.setZ(z + i - 1);
                            break;
                        } else location.setZ(z);
                        i++;
                    }
                    if (i >= BKchest.instance.searchRadiusMax) return;
                    Bukkit.getLogger().info("已经将一个补给箱放入队列中.");
                    BKchest.instance.waitToSpawnList.add(StaticFunction.turn(location));
                    BKchest.instance.chestMap.put(StaticFunction.turn(location), System.currentTimeMillis());
                    break;
                } else {
                    isLastBlockAIR = false;
                }
            }
        }
    }
}
