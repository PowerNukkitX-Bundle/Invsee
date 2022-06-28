package Invsee.utils;

import Invsee.Main;
import cn.nukkit.utils.Config;
import java.io.File;

public class InvseeConfig {
    private Main plugin;
    private Config config;
    private File file;

    public InvseeConfig(Main plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "config.yml");
        this.config = new Config(this.file, 2);
    }

    public void createDefault() {
        this.addDefault("invsee.prefix", "§5InvSee §f» ");
        this.addDefault("invsee.message.usageMessage", "§4Please use /inv <player>!");
        this.addDefault("invsee.message.hasNotPermission", "§4You can't use this command!");
        this.addDefault("invsee.message.isNotaPlayer", "§4You are not a player!");
        this.addDefault("invsee.message.playerIsNotOnline", "§4This Player is not online!");
        this.addDefault("invsee.message.endersee.usagemessage", "§4Please use /ecsee <Player>");
        this.addDefault("invsee.message.description", "See other players Inventory!");
        this.addDefault("invsee.message.endersee.description", "See other players Enderchest!");
    }

    public String prefix() {
        return this.config.getString("invsee.prefix");
    }

    public String usagemessage() {
        return this.config.getString("invsee.message.usageMessage");
    }

    public String hasNotPermission() {
        return this.config.getString("invsee.message.hasNotPermission");
    }

    public String isNotaPlayer() {
        return this.config.getString("invsee.message.isNotaPlayer");
    }

    public String isNotOnline() {
        return this.config.getString("invsee.message.playerIsNotOnline");
    }

    public String ecusagemessage() {
        return this.config.getString("invsee.message.endersee.usagemessage");
    }
    public String invdescription(){
        return this.config.getString("invsee.message.description");
    }
    public String ecdescription(){
        return this.config.getString("invsee.message.endersee.description");
    }

    public void addDefault(String path, Object object) {
        if (!this.config.exists(path)) {
            this.config.set(path, object);
            this.config.save(this.file);
        }

    }
}
