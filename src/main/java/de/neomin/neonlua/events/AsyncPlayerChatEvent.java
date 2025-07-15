package de.neomin.neonlua.events;

import de.neomin.neonlua.NeonLua;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

@RequiredArgsConstructor
public class AsyncPlayerChatEvent implements Listener {

    private final NeonLua plugin;

    @EventHandler
    public void onAsyncPlayerChat(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        try {
            LuaValue luaEvent = createLuaEvent(event);
            callLuaHandlers(luaEvent);
        } catch (Exception e) {
            plugin.getLogger().severe("Erro ao processar chat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private LuaValue createLuaEvent(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        LuaValue luaEvent = LuaValue.tableOf();

        luaEvent.set("getPlayer", getPlayerFunction(event));
        luaEvent.set("getMessage", getMessageFunction(event));
        luaEvent.set("getFormat", getFormatFunction(event));
        luaEvent.set("setCancelled", setCancelledFunction(event));
        luaEvent.set("isCancelled", isCancelledFunction(event));
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
                LuaValue handler = chunk.get("onAsyncPlayerChat");
                if (!handler.isnil() && handler.isfunction()) {
                    handler.call(luaEvent);
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Erro ao executar handler Lua: " + e.getMessage());
            }
        });
    }

    private ZeroArgFunction getPlayerFunction(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return CoerceJavaToLua.coerce(event.getPlayer());
            }
        };
    }

    private ZeroArgFunction getMessageFunction(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return CoerceJavaToLua.coerce(event.getMessage());
            }
        };
    }

    private ZeroArgFunction getFormatFunction(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return CoerceJavaToLua.coerce(event.getFormat());
            }
        };
    }

    private OneArgFunction setCancelledFunction(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                if (arg.istable()) {
                    LuaValue cancelled = arg.get("cancelled");
                    if (!cancelled.isnil()) {
                        event.setCancelled(cancelled.toboolean());
                    }
                }
                else {
                    event.setCancelled(arg.toboolean());
                }
                return LuaValue.NIL;
            }
        };
    }

    private ZeroArgFunction isCancelledFunction(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(event.isCancelled());
            }
        };
    }

    private ZeroArgFunction createGetServerFunction() {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return CoerceJavaToLua.coerce(plugin.getServer());
            }
        };
    }
}