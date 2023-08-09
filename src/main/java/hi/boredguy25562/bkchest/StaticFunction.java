package hi.boredguy25562.bkchest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class StaticFunction {
    public static String turn(Location l) {
        return (int) l.getX() + "," + (int) l.getY() + "," + (int) l.getZ();
    }

    public static String[] unTurn(String xyz) {
        return xyz.split(",");
    }

    public static void sortBkitems(ArrayList<BKitem> bkitems) {
        bkitems.sort(Comparator.comparingDouble(item -> item.weight));
    }

}
