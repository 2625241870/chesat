package hi.boredguy25562.bkchest;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YMLLoader {
    public static void loadYml() throws IOException {
        BKchest.instance.list = new ArrayList<>();
        BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream("plugins/BKchest/items.yml"), StandardCharsets.UTF_8));
        Map<String, Object> map = new Yaml().load(bf);
        BKchest.instance.baodi = (int) map.get("baodi");
        BKchest.instance.zuida = (int) map.get("zuida");
        BKchest.instance.gailu = (double) map.get("gailu");
        BKchest.instance.world= (String)map.get("world");
        BKchest.instance.MaxRadius= (int)map.get("maxradius");
        BKchest.instance.YStartAt=(int)map.get("YStartAt");
        BKchest.instance.searchRadiusMax=(int)map.get("search");
        int weight = 0;
        for (HashMap<String, Object> item : (ArrayList<HashMap<String, Object>>) (map.get("Items"))) {
            weight = weight + (Integer) item.get("weight");
        }
        double mui = ((Double) map.get("gailu")) / weight;
        for (HashMap<String, Object> item : (ArrayList<HashMap<String, Object>>) (map.get("Items"))) {
            BKitem bkitem = new BKitem(Material.valueOf(String.valueOf(item.get("item")).toUpperCase()));
            try{
                
            }catch (Exception e){

            }
            ItemMeta itemMeta = bkitem.itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', item.get("name").toString()));
            ArrayList<String> list = new ArrayList<>();
            for (String string : (ArrayList<String>) item.get("description")) {
                string = ChatColor.translateAlternateColorCodes('&', string);
                list.add(string);
            }//加载description
            itemMeta.setLore(list);
            bkitem.itemStack.setItemMeta(itemMeta);
            bkitem.weight = ((int) item.get("weight")) * mui;
            BKchest.instance.list.add(bkitem);
            StaticFunction.sortBkitems(BKchest.instance.list);
        }
    bf.close();
    }
}
