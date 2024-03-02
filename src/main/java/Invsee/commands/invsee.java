package Invsee.commands;

import Invsee.Main;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.inventory.ItemStackRequestActionEvent;
import cn.nukkit.inventory.fake.FakeInventory;
import cn.nukkit.inventory.fake.FakeInventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;

import java.util.Map;

import static cn.nukkit.block.BlockID.BARRIER;

public class invsee extends Command {
    public static String prefix = Main.getPlugin().getInvseeConfig().prefix();
    public static Player target2;

    public invsee(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
        this.setPermission("invsee.cmd");
        this.commandParameters.put("invsee", new CommandParameter[]{CommandParameter.newType("invsee", CommandParamType.TARGET)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("invsee.cmd") | sender.isOp()) {
                if (args.length == 1) {
                    String one = args[0];
                    Player target = Server.getInstance().getPlayer(one);
                    target2 = target;
                    if (target != null) {
                        if (!target.getName().equalsIgnoreCase(sender.getName())) {
                            FakeInventory inv = new FakeInventory(FakeInventoryType.DOUBLE_CHEST);
                            inv.setTitle("§e" + target.getName() + "'s §3inventory!");
                            inv.setContents(target.getInventory().getContents());
                            inv.setItem(40, Item.get(BARRIER));
                            inv.setItem(41, Item.get(BARRIER));
                            inv.setItem(42, Item.get(BARRIER));
                            inv.setItem(43, Item.get(BARRIER));
                            inv.setItem(44, Item.get(BARRIER));
                            inv.setItem(45, Item.get(BARRIER));
                            inv.setItem(46, Item.get(BARRIER));
                            inv.setItem(47, Item.get(BARRIER));
                            inv.setItem(48, Item.get(BARRIER));
                            inv.setItem(49, Item.get(BARRIER));
                            inv.setItem(50, Item.get(BARRIER));
                            inv.setItem(51, Item.get(BARRIER));
                            inv.setItem(52, Item.get(BARRIER));
                            inv.setItem(53, Item.get(BARRIER));
                            ((Player) sender).addWindow(inv);
                            inv.setDefaultItemHandler(this::onSlotChange);
                        } else {
                            sender.sendMessage(prefix + "§cYou can`t edit your own Inventory!");
                        }
                    } else {
                        sender.sendMessage(prefix + Main.getPlugin().getInvseeConfig().isNotOnline());
                    }
                } else {
                    sender.sendMessage(prefix + Main.getPlugin().getInvseeConfig().usagemessage());
                }
            } else {
                sender.sendMessage(prefix + Main.getPlugin().getInvseeConfig().hasNotPermission());
            }
        } else {
            sender.sendMessage(prefix + Main.getPlugin().getInvseeConfig().isNotaPlayer());
        }

        return true;
    }

    private void onSlotChange(FakeInventory inv, int var1, Item var2, ItemStackRequestActionEvent var3) {
        if (var1 >= 40) {
            var3.setCancelled(true);
        } else {
            Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                public void onRun(int currentTick) {
                    Map<Integer, Item> contens = inv.getContents();
                    invsee.target2.getInventory().setContents(contens);
                }
            }, 1);
        }
    }
}
