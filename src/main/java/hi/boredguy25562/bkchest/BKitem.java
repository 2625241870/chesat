package hi.boredguy25562.bkchest;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BKitem {
    ItemStack itemStack;
    double weight;
    boolean isIA=false;
    public BKitem(Material item) {
        itemStack = new ItemStack(item);
    }
}
