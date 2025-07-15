package de.neomin.neonlua.utilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class NeonInventory {

    public LuaValue init(Player player) {
        LuaValue manager = LuaValue.tableOf();

        manager.set("addItem", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue item) {
                try {
                    ItemStack itemStack = (ItemStack) item.checkuserdata(ItemStack.class);
                    if (itemStack == null || itemStack.getType() == Material.AIR) {
                        return LuaValue.FALSE;
                    }

                    player.getInventory().addItem(itemStack.clone());
                    return LuaValue.TRUE;
                } catch (Exception e) {
                    return LuaValue.FALSE;
                }
            }
        });

        return manager;
    }
}
