package de.neomin.neonlua.utilities;

import de.neomin.neonlua.NeonLua;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

@RequiredArgsConstructor
public class NeonFunctions {

    private final NeonLua plugin;

    public ZeroArgFunction createServerFunction() {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return CoerceJavaToLua.coerce(plugin.getServer());
            }
        };
    }

    public ThreeArgFunction createItemBuilder() {
        return new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue type, LuaValue amount, LuaValue data) {
                try {
                    Material material;

                    if (type.isstring()) {
                        material = Material.matchMaterial(type.tojstring());
                    }
                    else if (type.isuserdata(Material.class)) {
                        material = (Material) type.touserdata();
                    }
                    else if (type.isnumber()) {
                        material = Material.getMaterial(type.toint());
                    }
                    else {
                        throw new IllegalArgumentException("Tipo de item inválido. Use Material.NAME, 'NAME' ou ID");
                    }

                    if (material == null) {
                        throw new IllegalArgumentException("Material não encontrado: " + type);
                    }

                    int itemAmount = amount.optint(1);
                    short itemData = (short) data.optint(0);

                    return CoerceJavaToLua.coerce(new NeonItemStack(new ItemStack(material, itemAmount, itemData)));

                } catch (Exception e) {
                    plugin.getLogger().warning("Erro ao criar item: " + e.getMessage());
                    e.printStackTrace();
                    return LuaValue.NIL;
                }
            }
        };
    }

    public OneArgFunction createInventoryManager() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue player) {
                Player bukkitPlayer = (Player) player.touserdata(Player.class);
                return new NeonInventory().init(bukkitPlayer);
            }
        };
    }

    public ZeroArgFunction createTagsFunction() {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                LuaValue luaTags = LuaValue.tableOf();

                luaTags.set("hasTag", new TwoArgFunction() {
                    @Override
                    public LuaValue call(LuaValue key, LuaValue mapKey) {
                        return LuaValue.valueOf(
                                plugin.getNeonTags().hasTag(
                                        key.tojstring(),
                                        mapKey.tojstring()
                                )
                        );
                    }
                });

                luaTags.set("getTag", new TwoArgFunction() {
                    @Override
                    public LuaValue call(LuaValue key, LuaValue mapKey) {
                        return plugin.getNeonTags().getTag(
                                key.tojstring(),
                                mapKey.tojstring());
                    }
                });

                luaTags.set("putTag", new ThreeArgFunction() {
                    @Override
                    public LuaValue call(LuaValue key, LuaValue mapKey, LuaValue value) {
                        plugin.getNeonTags().putTag(
                                key.tojstring(),
                                mapKey.tojstring(),
                                value
                        );
                        return LuaValue.NIL;
                    }
                });

                luaTags.set("removeTag", new ThreeArgFunction() {
                    @Override
                    public LuaValue call(LuaValue key, LuaValue mapKey, LuaValue value) {
                        plugin.getNeonTags().removeTag(
                                key.tojstring(),
                                mapKey.tojstring()
                        );
                        return LuaValue.NIL;
                    }
                });

                return luaTags;
            }
        };
    }
}
