package Invsee.commands;

import Invsee.Main;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;
import com.nukkitx.fakeinventories.inventory.DoubleChestFakeInventory;
import com.nukkitx.fakeinventories.inventory.FakeSlotChangeEvent;
import java.util.Map;

public class invsee extends Command {
    public static String prefix = Main.getPlugin().getInvseeConfig().prefix();
    public static Player target2;

    public invsee(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
        this.setPermission("invsee.cmd");
        this.commandParameters.put("invsee", new CommandParameter[]{new CommandParameter("invsee", CommandParamType.TARGET, false)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String var10001;
        if (sender instanceof Player) {
            if (sender.hasPermission("invsee.cmd") | sender.isOp()) {
                if (args.length == 1) {
                    String one = args[0];
                    Player target = Server.getInstance().getPlayer(one);
                    target2 = target;
                    if (target != null) {
                        if (!target.getName().equalsIgnoreCase(sender.getName())) {
                            DoubleChestFakeInventory inv = new DoubleChestFakeInventory();
                            inv.setName("§e" + target.getName() + "'s §3inventory!");
                            inv.setContents(target.getInventory().getContents());
                            inv.setItem(40, Item.get(-161));
                            inv.setItem(41, Item.get(-161));
                            inv.setItem(42, Item.get(-161));
                            inv.setItem(43, Item.get(-161));
                            inv.setItem(44, Item.get(-161));
                            inv.setItem(45, Item.get(-161));
                            inv.setItem(46, Item.get(-161));
                            inv.setItem(47, Item.get(-161));
                            inv.setItem(48, Item.get(-161));
                            inv.setItem(49, Item.get(-161));
                            inv.setItem(50, Item.get(-161));
                            inv.setItem(51, Item.get(-161));
                            inv.setItem(52, Item.get(-161));
                            inv.setItem(53, Item.get(-161));
                            ((Player) sender).addWindow(inv);
                            inv.addListener(this::onSlotChange);
                        }else {
                            var10001 = prefix;
                            sender.sendMessage(var10001 + "§cYou can`t edit your own Inventory!");
                        }
                    } else {
                        var10001 = prefix;
                        sender.sendMessage(var10001 + Main.getPlugin().getInvseeConfig().isNotOnline());
                    }
                } else {
                    var10001 = prefix;
                    sender.sendMessage(var10001 + Main.getPlugin().getInvseeConfig().usagemessage());
                }
            } else {
                var10001 = prefix;
                sender.sendMessage(var10001 + Main.getPlugin().getInvseeConfig().hasNotPermission());
            }
        } else {
            var10001 = prefix;
            sender.sendMessage(var10001 + Main.getPlugin().getInvseeConfig().isNotaPlayer());
        }

        return true;
    }

    private void onSlotChange(final FakeSlotChangeEvent event) {
        if (event.getInventory() instanceof DoubleChestFakeInventory && event.getInventory().getName().equalsIgnoreCase("§e" + target2.getName() + "'s §3Inventory!")) {
            if (event.getAction().getSlot() >= 40){
                event.setCancelled(true);
            }else {
                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                    public void onRun(int currentTick) {
                        Map<Integer, Item> contens = event.getInventory().getContents();
                        invsee.target2.getInventory().setContents(contens);
                    }
                }, 1);
            }
        }

    }
}
