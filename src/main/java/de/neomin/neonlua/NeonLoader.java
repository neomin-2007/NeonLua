package de.neomin.neonlua;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.util.List;

@RequiredArgsConstructor
public class NeonLoader {

    private final NeonLua plugin;

    public void init() {
        FileConfiguration cfg = plugin.getConfig();
        List<String> scriptsList = cfg.getStringList("active-scripts");

        if (scriptsList.isEmpty()) {
            plugin.getLogger().warning("Nenhum script ativo configurado em config.yml");
            return;
        }

        File scriptsFolder = new File(plugin.getDataFolder(), "scripts");
        if (!scriptsFolder.exists()) {
            scriptsFolder.mkdirs();
        }

        Globals globals = JsePlatform.standardGlobals();
        globals.set("Server", plugin.getNeonFunctions().createServerFunction());
        globals.set("Tags", plugin.getNeonFunctions().createTagsFunction());
        globals.set("Material", CoerceJavaToLua.coerce(Material.class));
        globals.set("EntityType", CoerceJavaToLua.coerce(EntityType.class));
        globals.set("itemBuilder", plugin.getNeonFunctions().createItemBuilder());
        globals.set("inventoryManager", plugin.getNeonFunctions().createInventoryManager());


        scriptsList.forEach(scriptPath -> {
            File scriptFile = new File(scriptsFolder, scriptPath);

            if (!scriptPath.endsWith(".lua")) {
                scriptFile = new File(scriptsFolder, scriptPath + ".lua");
            }

            if (!scriptFile.exists()) {
                plugin.getLogger().warning("Script não encontrado: " + scriptPath);
                return;
            }

            try {
                LuaValue chunk = globals.loadfile(scriptFile.getAbsolutePath());
                LuaValue result = chunk.call();

                if (result.isnil()) {
                    plugin.getLogger().warning("Script não retornou valores: " + scriptPath);
                    return;
                }

                LuaValue mainTable = result.get("main");
                if (!mainTable.isnil()) {
                    plugin.getLoaded_scripts().put(mainTable.get("name").toString(), result);
                    plugin.getLogger().info("Script carregado: " + scriptPath);
                }

            } catch (Exception e) {
                plugin.getLogger().severe("Erro no script " + scriptPath + ": " + e.getMessage());
            }
        });
    }
}