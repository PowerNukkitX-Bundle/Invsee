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

public class endersee extends Command {
    public static String prefix = Main.getPlugin().getInvseeConfig().prefix();

    public endersee(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
        this.setPermission("endersee.cmd");
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
            player.sendMessage(prefix + "§cYou can`t edit your own Enderchest!");
            return 0;
        }

        openEnderChest(player, target);
        return 1;
    }

    private void openEnderChest(Player player, Player target) {
        FakeInventory inv = new FakeInventory(FakeInventoryType.CHEST);
        inv.setTitle("§e" + target.getName() + "'s §5enderchest!");
        inv.setContents(target.getEnderChestInventory().getContents());
        inv.setDefaultItemHandler((inventory, slot, srcItem, dstItem, event) -> {
            Player viewer = inventory.getViewers().iterator().next();
            if (viewer == null) return;
            if (!(viewer.hasPermission("endersee.edit") || player.isOp())) event.setCancelled(true);

            Map<Integer, Item> targetContents = target.getEnderChestInventory().getContents();
            Map<Integer, Item> viewerContents = inventory.getContents();

            targetContents.put(slot, srcItem);
            viewerContents.put(slot, srcItem);
            if (!targetContents.equals(viewerContents)) {
                inventory.setContents(target.getEnderChestInventory().getContents());
                event.setCancelled(true);
            }

            targetContents.put(slot, dstItem);
            viewerContents.put(slot, dstItem);
            if (!targetContents.equals(viewerContents)) {
                inventory.setContents(target.getEnderChestInventory().getContents());
                event.setCancelled(true);
            } else {
                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                    public void onRun(int currentTick) {
                        Map<Integer, Item> contents = inv.getContents();
                        target.getEnderChestInventory().setContents(contents);
                    }
                }, 1);
            }
        });
        player.getServer().getScheduler().scheduleDelayedTask(InternalPlugin.INSTANCE, () -> player.addWindow(inv), 5);
    }
}
