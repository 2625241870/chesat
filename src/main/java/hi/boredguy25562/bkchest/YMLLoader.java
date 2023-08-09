package hi.boredguy25562.bkchest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YMLLoader {
    public static void loadYml() throws IOException {
        BKchest.instance.list = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("plugins/BKchest/items.yml"), StandardCharsets.UTF_8));
        Map<String, Object> map = new Yaml().load(bf);
        try {
            BKchest.instance.baodi = (int) map.get("baodi");
            BKchest.instance.zuida = (int) map.get("zuida");
            BKchest.instance.gailu = (double) map.get("gailu");
            BKchest.instance.world = (String) map.get("world");
            BKchest.instance.MaxRadius = (int) map.get("maxradius");
            BKchest.instance.maxY = (int) map.get("maxY");
            BKchest.instance.YStartAt = (int) map.get("YStartAt");
            BKchest.instance.searchRadiusMax = (int) map.get("search");
        }catch (Exception e){
            Bukkit.getLogger().info("加载items.yml除item外的配置项错误");
        }
        int weight = 0;
        for (HashMap<String, Object> item : (ArrayList<HashMap<String, Object>>) (map.get("Items"))) {
            weight = weight + (Integer) item.get("weight");
        }
        double mui = ((Double) map.get("gailu")) / weight;
        for (HashMap<String, Object> item : (ArrayList<HashMap<String, Object>>) (map.get("Items"))) {
            BKitem bkitem = new BKitem(Material.valueOf(String.valueOf(item.get("item")).toUpperCase()));
            try {
                if (item.get("namespace_id") != null) {
                    if (BKchest.instance.ia = true) {
                        bkitem.isIA = true;
                        bkitem.IAname = (String) item.get("namespace_id");
                    } else {
                        Bukkit.getLogger().info("未加载ItemsAdder插件，加载" + item.get("namespace_id") + "时失败.");
                        continue;
                    }
                }
            } catch (Exception e) {
                Bukkit.getLogger().info("加载itemsAdder物品时失败了...");
            }

            ItemMeta itemMeta = bkitem.itemStack.getItemMeta();

            try {
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', item.get("name").toString()));
            } catch (Exception ignored) {
            }

            ArrayList<String> list = new ArrayList<>();

            try {
                for (String string : (ArrayList<String>) item.get("description")) {
                    string = ChatColor.translateAlternateColorCodes('&', string);
                    list.add(string);
                }//加载description
                itemMeta.setLore(list);
            } catch (Exception ignored) {
            }

            bkitem.itemStack.setItemMeta(itemMeta);
            bkitem.weight = ((int) item.get("weight")) * mui;
            BKchest.instance.list.add(bkitem);
            StaticFunction.sortBkitems(BKchest.instance.list);
        }
        bf.close();
    }
}
