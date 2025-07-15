package de.neomin.neonlua.events;

import de.neomin.neonlua.NeonLua;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

@RequiredArgsConstructor
public class MoveEvent implements Listener {

    private final NeonLua plugin;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            LuaValue luaEvent = createLuaEvent(event);
            callLuaHandlers(luaEvent);
        } catch (Exception e) {
            plugin.getLogger().severe("Erro ao processar chat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private LuaValue createLuaEvent(PlayerMoveEvent event) {
        LuaValue luaEvent = LuaValue.tableOf();

        luaEvent.set("getPlayer", getPlayerFunction(event));
        luaEvent.set("getFrom", getFromFunction(event));
        luaEvent.set("getTo", getToFunction(event));

        luaEvent.set("isCancelled", isCancelledFunction(event));
        luaEvent.set("setCancelled", setCancelledFunction(event));
        luaEvent.set("setFrom", setFrom(event));
        luaEvent.set("setTo", setTo(event));

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
                LuaValue handler = chunk.get("onPlayerMove");
                if (!handler.isnil() && handler.isfunction()) {
                    handler.call(luaEvent);
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Erro ao executar handler Lua: " + e.getMessage());
            }
        });
    }

    private ZeroArgFunction getPlayerFunction(PlayerMoveEvent event) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return CoerceJavaToLua.coerce(event.getPlayer());
            }
        };
    }

    private ZeroArgFunction getFromFunction(PlayerMoveEvent event) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return CoerceJavaToLua.coerce(event.getFrom());
            }
        };
    }

    private ZeroArgFunction getToFunction(PlayerMoveEvent event) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return CoerceJavaToLua.coerce(event.getTo());
            }
        };
    }

    private OneArgFunction setFrom(PlayerMoveEvent event) {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                event.setFrom((Location) arg.touserdata(Location.class));
                return LuaValue.NIL;
            }
        };
    }

    private OneArgFunction setTo(PlayerMoveEvent event) {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                event.setTo((Location) arg.touserdata(Location.class));
                return LuaValue.NIL;
            }
        };
    }

    private OneArgFunction setCancelledFunction(PlayerMoveEvent event) {
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

    private ZeroArgFunction isCancelledFunction(PlayerMoveEvent event) {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(event.isCancelled());
            }
        };
    }
}