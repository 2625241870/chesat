package hi.boredguy25562.bkchest;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Random;

public class BKrun implements Runnable {
    @Override
    public void run() {
        try {
            Location location = randomLocation();
            if (location.isChunkLoaded()) {
                location.getChunk().load();
            }
            boolean isLastBlockAIR = false;
            double y = BKchest.instance.YStartAt;
            for (int c = 0; c < BKchest.instance.maxY - y; c++) {
                location.setY(y + c);
                if (!location.getBlock().getType().isAir()) {
                    isLastBlockAIR = true;
                } else if (isLastBlockAIR) {//上一个方块不是空气的话
                    if (new Random().nextBoolean()) {
                        if (execute(location)) return;
                    } else {
                        isLastBlockAIR = false;//上一个方块是空气。
                    }
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("在run函数出错，非正常退出.");
            return;
        }
        run();
    }

    public boolean findWall(Location location) {
        if (downIsnAir(location)) {
            Bukkit.getLogger().info("已经将一个补给箱加入队列");
            BKchest.instance.waitToSpawnList.add(StaticFunction.turn(location));//加入队列，等待WaitToSpawn类生成
            BKchest.instance.chestMap.put(StaticFunction.turn(location), System.currentTimeMillis());//记录下时间，以便之后删除
            return true;
        }
        while (downIsnAir(location)) location.setY(location.getY() - 1);
        return false;
    }

    public boolean downIsnAir(Location l) {
        Location location = l.clone();
        location.setY(location.getY() - 1);
        return !location.getBlock().getType().isAir();
    }

    public boolean nextXIsWall(Location l) {
        Location location = l.clone();
        location.setX(location.getX() + 1);
        return !location.getBlock().getType().isAir();
    }

    public boolean nextZIsWall(Location l) {
        Location location = l.clone();
        location.setZ(location.getZ() + 1);
        return !location.getBlock().getType().isAir();
    }

    public Location randomLocation() {
        Random random = new Random();
        return new Location(Bukkit.getWorld(BKchest.instance.world), random.nextInt((BKchest.instance.MaxRadius * 2) + 1) - BKchest.instance.MaxRadius, BKchest.instance.YStartAt, random.nextInt((BKchest.instance.MaxRadius * 2) + 1) - BKchest.instance.MaxRadius);
    }

    public boolean execute(Location location) {
        int i = 1;
        double x = location.getX();
        double z = location.getZ();
        for (; i < BKchest.instance.searchRadiusMax; i++) {
            if (nextXIsWall(location)) {
                if (findWall(location)) {
                    return true;
                } else {
                    break;
                }
            }
            location.setX(x + i);
        }
        for (i = 1; i < BKchest.instance.searchRadiusMax; i++) {
            if (nextZIsWall(location)) {
                if (findWall(location)) {
                    return true;
                } else {
                    break;
                }
            }
            location.setZ(z + i);
        }
        return false;
    }
}
