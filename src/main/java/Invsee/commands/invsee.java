package Invsee.commands;

import Invsee.Main;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.tree.node.PlayersNode;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.inventory.fake.FakeInventory;
import cn.nukkit.inventory.fake.FakeInventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.InternalPlugin;
import cn.nukkit.scheduler.Task;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType;

import java.util.List;
import java.util.Map;

public class invsee extends Command {
    public static String prefix = Main.getPlugin().getInvseeConfig().prefix();
    public static Player target2;

    public invsee(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
        this.setPermission("invsee.cmd");
        this.setPermissionMessage(prefix + Main.getPlugin().getInvseeConfig().hasNotPermission());
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("player", CommandParamType.SELECTION, new PlayersNode())
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(prefix + Main.getPlugin().getInvseeConfig().isNotaPlayer());
            return 0;
        }

        List<Player> targets = result.getValue().getResult(0);
        Player target = targets == null || targets.size() != 1 ? null : targets.get(0);

        if (target == null) {
            player.sendMessage(prefix + Main.getPlugin().getInvseeConfig().isNotOnline());
            return 0;
        }

        if (target.getName().equalsIgnoreCase(player.getName())) {
            player.sendMessage(prefix + "§cYou can`t edit your own Inventory!");
            return 0;
        }

        openInventory(player, target);
        return 1;
    }

    private void openInventory(Player player, Player target) {
        target2 = target;
        FakeInventory inv = new FakeInventory(FakeInventoryType.DOUBLE_CHEST);
        inv.setTitle("§e" + target.getName() + "'s §3inventory!");
        inv.setContents(target.getInventory().getContents());
        setBarriers(inv);
        inv.setDefaultItemHandler((inventory, slot, srcItem, dstItem, event) -> {
            Player viewer = inventory.getViewers().iterator().next();
            if (viewer == null) return;
            if (!(viewer.hasPermission("invsee.edit") || viewer.isOp())) event.setCancelled(true);

            Map<Integer, Item> targetContents = target.getInventory().getContents();
            Map<Integer, Item> viewerContents = inventory.getContents();
            viewerContents.entrySet().removeIf(entry -> entry.getKey() >= 40 && entry.getKey() <= 53);

            targetContents.put(slot, srcItem);
            viewerContents.put(slot, srcItem);
            if (!targetContents.equals(viewerContents)) {
                inventory.setContents(target.getInventory().getContents());
                setBarriers(inventory);
                event.setCancelled(true);
            }

            targetContents.put(slot, dstItem);
            viewerContents.put(slot, dstItem);
            if (!targetContents.equals(viewerContents)) {
                inventory.setContents(target.getInventory().getContents());
                setBarriers(inventory);
                event.setCancelled(true);
            } else {
                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                    public void onRun(int currentTick) {
                        Map<Integer, Item> contents = inv.getContents();
                        target.getInventory().setContents(contents);
                    }
                }, 1);
            }
        });
        player.getServer().getScheduler().scheduleDelayedTask(InternalPlugin.INSTANCE, () -> player.addWindow(inv), 5);
    }

    public void setBarriers(FakeInventory inventory) {
        inventory.setItem(40, Item.get("minecraft:barrier"));
        inventory.setItem(41, Item.get("minecraft:barrier"));
        inventory.setItem(42, Item.get("minecraft:barrier"));
        inventory.setItem(43, Item.get("minecraft:barrier"));
        inventory.setItem(44, Item.get("minecraft:barrier"));
        inventory.setItem(45, Item.get("minecraft:barrier"));
        inventory.setItem(46, Item.get("minecraft:barrier"));
        inventory.setItem(47, Item.get("minecraft:barrier"));
        inventory.setItem(48, Item.get("minecraft:barrier"));
        inventory.setItem(49, Item.get("minecraft:barrier"));
        inventory.setItem(50, Item.get("minecraft:barrier"));
        inventory.setItem(51, Item.get("minecraft:barrier"));
        inventory.setItem(52, Item.get("minecraft:barrier"));
        inventory.setItem(53, Item.get("minecraft:barrier"));
    }
}
