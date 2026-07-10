package Invsee;


import Invsee.commands.endersee;
import Invsee.commands.invsee;
import Invsee.utils.InvseeConfig;
import org.powernukkitx.Server;
import org.powernukkitx.plugin.PluginBase;

    public class Main extends PluginBase {
        private InvseeConfig invseeConfig;
        public static Main plugin;

        public Main() {
        }

        public void onEnable() {
            this.invseeConfig = new InvseeConfig(this);
            this.invseeConfig.createDefault();
            plugin = this;
            String prefix = getPlugin().getInvseeConfig().prefix();
            this.getServer().getLogger().info(prefix + "§eThe plugin has been activated!");
            this.getServer().getCommandMap().register("invsee", new invsee("invsee", this.invseeConfig.invdescription(), this.getInvseeConfig().usagemessage(), new String[]{"inv"}));
            this.getServer().getCommandMap().register("endersee", new endersee("endersee", this.invseeConfig.ecdescription(), this.getInvseeConfig().usagemessage(), new String[]{"ecsee", "ec"}));
        }

        public void onDisable() {
            String prefix = getPlugin().getInvseeConfig().prefix();
            this.getServer().getLogger().info(prefix + "§eThe plugin has been deactivated!");
        }

        public InvseeConfig getInvseeConfig() {
            return this.invseeConfig;
        }

        public static Main getPlugin() {
            return plugin;
        }
}
