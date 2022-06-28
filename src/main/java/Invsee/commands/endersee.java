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
import com.nukkitx.fakeinventories.inventory.ChestFakeInventory;
import com.nukkitx.fakeinventories.inventory.FakeSlotChangeEvent;
import java.util.Map;

public class endersee extends Command {
    public static Player target2;
    public static String prefix = Main.getPlugin().getInvseeConfig().prefix();

    public endersee(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
        this.setPermission("endersee.cmd");
        this.commandParameters.put("endersee", new CommandParameter[]{new CommandParameter("endersee", CommandParamType.TARGET, false)});
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("endersee.cmd") | sender.isOp()) {
                if (args.length == 1) {
                    String one = args[0];
                    Player target = Server.getInstance().getPlayer(one);
                    target2 = target;
                    if (target != null) {
                        if (!target.getName().equalsIgnoreCase(sender.getName())) {
                            ChestFakeInventory inv = new ChestFakeInventory();
                            inv.setName("§e" + target.getName() + "'s §5enderchest!");
                            inv.setContents(target.getEnderChestInventory().getContents());
                            ((Player) sender).addWindow(inv);
                            inv.addListener(this::onSlotChange);
                        }else {
                            sender.sendMessage(prefix + "§cYou can`t edit your own Enderchest!");
                        }
                    } else {
                        sender.sendMessage(prefix + Main.getPlugin().getInvseeConfig().isNotOnline());
                    }
                } else {
                    sender.sendMessage(prefix + Main.getPlugin().getInvseeConfig().ecusagemessage());
                }
            } else {
                sender.sendMessage(prefix + Main.getPlugin().getInvseeConfig().hasNotPermission());
            }
        } else {
            sender.sendMessage(prefix + Main.getPlugin().getInvseeConfig().isNotaPlayer());
        }

        return true;
    }

    private void onSlotChange(final FakeSlotChangeEvent event) {
        if (event.getInventory() instanceof ChestFakeInventory && event.getInventory().getName().equalsIgnoreCase("§e" + target2.getName() + "'s §5enderchest!")) {
            Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                public void onRun(int currentTick) {
                    Map<Integer, Item> contens = event.getInventory().getContents();
                    endersee.target2.getEnderChestInventory().setContents(contens);
                }
            }, 1);
        }

    }
}