package de.neomin.neonlua.utilities;

import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class NeonNBT {

    private final ItemStack item;
    private final NBTTagCompound compound;
    private final net.minecraft.server.v1_8_R3.ItemStack nmsItem;

    public NeonNBT(ItemStack item) {
        this.item = item;
        this.nmsItem = CraftItemStack.asNMSCopy(item);
        this.compound = this.nmsItem.hasTag() ? this.nmsItem.getTag() : new NBTTagCompound();
    }

    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.nmsItem);
    }

    public NeonNBT setString(String key, String value) {
        this.compound.set(key, new NBTTagString(value));
        return this;
    }

    public boolean hasKey(String key) {
        return this.compound.hasKey(key);
    }

    public String getString(String key) {
        return this.compound.getString(key);
    }

    public NeonNBT setBoolean(String key, boolean value) {
        this.compound.setBoolean(key, value);
        return this;
    }

    public boolean getBoolean(String key) {
        return this.compound.getBoolean(key);
    }

    public NeonNBT setInt(String key, int value) {
        this.compound.setInt(key, value);
        return this;
    }

    public int getInt(String key) {
        return this.compound.getInt(key);
    }

    public NeonNBT setDouble(String key, double value) {
        this.compound.setDouble(key, value);
        return this;
    }

    public double getDouble(String key) {
        return this.compound.getDouble(key);
    }

    public NeonNBT addToStringList(String listKey, String value) {
        NBTTagList list = this.compound.getList(listKey, 8);
        list.add(new NBTTagString(value));
        this.compound.set(listKey, list);
        return this;
    }

    public boolean containsInStringList(String listKey, String value) {
        NBTTagList list = this.compound.getList(listKey, 8);
        for (int i = 0; i < list.size(); i++) {
            if (list.getString(i).equals(value)) {
                return true;
            }
        }
        return false;
    }

    public ItemStack apply(ItemStack item) {
        this.nmsItem.setTag(this.compound);
        return CraftItemStack.asBukkitCopy(this.nmsItem);
    }

    public NeonNBT remove(String key) {
        this.compound.remove(key);
        return this;
    }
}
