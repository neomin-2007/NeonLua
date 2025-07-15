package de.neomin.neonlua.events;

import de.neomin.neonlua.NeonLua;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

@RequiredArgsConstructor
public class DeathEvent implements Listener {

    private final NeonLua plugin;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        try {
            LuaValue luaEvent = createLuaEvent(event);
            callLuaHandlers(luaEvent);
        } catch (Exception e) {
            plugin.getLogger().severe("Erro ao processar chat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private LuaValue createLuaEvent(PlayerDeathEvent event) {
        LuaValue luaEvent = LuaValue.tableOf();

        luaEvent.set("getPlayer", getPlayerFunction(event));
        luaEvent.set("getDeathMessage", getDeathMessageFunction(event));
        luaEvent.set("setDeathMessage", setDeathMessage(event));
        luaEvent.set("getServer", plugin.getNeonFunctions().createServerFunction());
        luaEvent.set("getTags", plugin.getNeonFunctions().createTagsFunction());

        luaEvent.set("getCurrentMillis", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(System.currentTimeMillis());
            }
        });

        return luaEvent;
    }

    private void callLuaHandlers(LuaValue luaEvent) {
        plugin.getLoaded_scripts().values().forEach(chunk -> {
            try {
                LuaValue handler = chunk.get("onPlayerDeath");
                if (!handler.isnil() && handler.isfunction()) {
                    handler.call(luaEvent);
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Erro ao executar handler Lua: " + e.getMessage());
            }
        });
    }

    private OneArgFunction setDeathMessage(PlayerDeathEvent event) {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                event.setDeathMessage(arg.tojstring());
                return LuaValue.NIL;
            }
        };
    }

    private ZeroArgFunction getPlayerFunction(PlayerDeathEvent event) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return CoerceJavaToLua.coerce(event.getEntity());
            }
        };
    }

    private ZeroArgFunction getDeathMessageFunction(PlayerDeathEvent event) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return CoerceJavaToLua.coerce(event.getDeathMessage());
            }
        };
    }
}