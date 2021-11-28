package Invsee;


import Invsee.commands.endersee;
import Invsee.commands.invsee;
import Invsee.utils.InvseeConfig;
import cn.nukkit.plugin.PluginBase;

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
            this.getServer().getLogger().info(prefix + "§eThe plugin has been activate!");
            this.getServer().getCommandMap().register("help", new invsee("invsee", "", this.getInvseeConfig().usagemessage(), new String[]{"inv"}));
            this.getServer().getCommandMap().register("help", new endersee("endersee", "", this.getInvseeConfig().usagemessage(), new String[]{"ecsee"}));
        }

        public void onDisable() {
            String prefix = getPlugin().getInvseeConfig().prefix();
            this.getServer().getLogger().info(prefix + "§eThe plugin has been deactivate!");
        }

        public InvseeConfig getInvseeConfig() {
            return this.invseeConfig;
        }

        public static Main getPlugin() {
            return plugin;
        }
}
