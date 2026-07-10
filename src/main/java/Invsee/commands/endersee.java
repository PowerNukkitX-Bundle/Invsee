package Invsee.commands;

import Invsee.Main;
import org.powernukkitx.Player;
import org.powernukkitx.Server;
import org.powernukkitx.command.Command;
import org.powernukkitx.command.CommandSender;
import org.powernukkitx.command.data.CommandParameter;
import org.powernukkitx.command.tree.ParamList;
import org.powernukkitx.command.tree.node.PlayersNode;
import org.powernukkitx.command.utils.CommandLogger;
import org.powernukkitx.inventory.fake.FakeInventory;
import org.powernukkitx.inventory.fake.FakeInventoryType;
import org.powernukkitx.item.Item;
import org.powernukkitx.plugin.InternalPlugin;
import org.powernukkitx.scheduler.Task;
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
