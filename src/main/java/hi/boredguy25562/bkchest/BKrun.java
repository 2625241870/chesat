package hi.boredguy25562.bkchest;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BKrun implements Runnable{
    @Override
    public void run() {
        int randomX= (int)((Math.random() * 2001) - 1000);
        int randomZ= (int) ((Math.random() * 2001) - 1000);
        Location location=new Location(Bukkit.getWorld("world"),randomX,0,randomZ);
        boolean isLastBlockAIR = false;
        for(int y=0;y<255;y++) {
            if (!location.getBlock().getType().equals(Material.AIR)) {
                location.setY(y);
                isLastBlockAIR=true;
            }else if(isLastBlockAIR) {
                if ((int)(Math.random()*1.9)==1) {
                    isLastBlockAIR = false;
                    location.getBlock().setType(Material.CRIMSON_NYLIUM);
                    BlockState block= location.getBlock().getState();
                    block.update();
                    int i =1;
                    while (i<10){
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
                        i++;

                    }
                    if(!(i<9)){
                        for(Player p:Bukkit.getOnlinePlayers()){
                            if(p.isOp()){
                                p.sendMessage("[后室]在坐标:"+location.getX()+","+location.getY()+","+location.getZ()+"生成补给箱失败...未找到合适的位置");
                            }
                        }
                        return;
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
