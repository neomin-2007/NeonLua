package de.neomin.neonlua;

import de.neomin.neonlua.events.*;
import de.neomin.neonlua.utilities.NeonFunctions;
import de.neomin.neonlua.utilities.NeonTags;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.HashMap;

@Getter
public class NeonLua
extends JavaPlugin {

    private Globals luaGlobals;
    private NeonLoader neonLoader;
    private NeonTags neonTags;
    private NeonFunctions neonFunctions;

    private final HashMap<String, LuaValue> loaded_scripts = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        luaGlobals = JsePlatform.standardGlobals();

        neonFunctions = new NeonFunctions(this);
        neonLoader = new NeonLoader(this);
        neonLoader.init();

        neonTags = new NeonTags();

        registerEvents();
    }

    private void registerEvents() {
        final PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new JoinEvent(this), this);
        pm.registerEvents(new QuitEvent(this), this);
        pm.registerEvents(new MoveEvent(this), this);
        pm.registerEvents(new DeathEvent(this), this);
        pm.registerEvents(new AsyncPlayerChatEvent(this), this);
        pm.registerEvents(new CommandPreProcessEvent(this), this);

    }
}
