package de.neomin.neonlua.utilities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class NeonItemStack {

    private ItemStack item;
    private ItemMeta meta;
    private final NeonNBT neonNBT;

    public NeonItemStack(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
        this.neonNBT = new NeonNBT(item);
    }

    public NeonItemStack(Material material) {
        this(new ItemStack(material));
    }

    public static boolean isEquipment(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            final String display = item.getItemMeta().getDisplayName();
            final String type = item.getType().name();

            if (type.endsWith("BOOTS")
                    || type.endsWith("LEGGINGS")
                    || type.endsWith("CHESTPLATE")
                    || type.endsWith("HELMET")) {
                return true;
            }

            return display.contains("Capacete");
        }
        return false;
    }

    public static ItemStack close_item() {
        return new NeonItemStack(Material.BARRIER)
                .setDisplayName("§cFechar")
                .getItem();
    }

    public NeonItemStack setDisplayName(String displayName) {
        meta.setDisplayName(displayName);
        return this;
    }

    public NeonItemStack addEnchantment(Enchantment enchant, int lvl, boolean apply) {
        if (apply) {
            meta.addEnchant(enchant, lvl, true);
        }
        return this;
    }

    public NeonItemStack setGlowing(boolean toggle) {
        if (toggle) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            meta.removeEnchant(Enchantment.DURABILITY);
        }
        return this;
    }

    public NeonItemStack setUnbreakable(boolean toggle) {
        meta.spigot().setUnbreakable(toggle);
        return this;
    }

    public NeonItemStack setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public NeonItemStack setData(byte data) {
        item.setData(new MaterialData(item.getType(), data));
        return this;
    }

    public NeonItemStack setHideAllFlags(boolean toggle) {
        if (toggle) {
            meta.addItemFlags(ItemFlag.values());
        } else {
            meta.removeItemFlags(ItemFlag.values());
        }
        return this;
    }

    public NeonItemStack addItemFlags(ItemFlag... itemFlag) {
        meta.addItemFlags(itemFlag);
        return this;
    }

    public NeonItemStack addLoreLine(String line) {
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        lore.add(line);
        meta.setLore(lore);
        return this;
    }

    public NeonItemStack removeLoreLine(int index) {
        List<String> lore = meta.getLore();
        if (lore != null && index < lore.size()) {
            lore.remove(index);
            meta.setLore(lore);
        }
        return this;
    }

    public NeonItemStack addLore(List<String> lore) {
        List<String> itemLore = meta.getLore();
        if (itemLore == null) itemLore = new ArrayList<>();
        itemLore.addAll(lore);
        meta.setLore(itemLore);
        return this;
    }

    public NeonItemStack replaceLoreLine(String line, String toReplace) {
        List<String> lore = meta.getLore();
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).contains(line)) {
                    lore.set(i, toReplace);
                }
            }
            meta.setLore(lore);
        }
        return this;
    }

    public NeonItemStack replaceLoreLine(int line, String toReplace) {
        List<String> lore = meta.getLore();
        if (lore != null && line < lore.size()) {
            lore.set(line, toReplace);
            meta.setLore(lore);
        }
        return this;
    }

    public NeonItemStack setColor(int r, int g, int b) {
        if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
            leatherMeta.setColor(Color.fromRGB(r, g, b));
        }
        return this;
    }

    public NeonItemStack setPotionEffect(PotionEffectType potionEffect) {
        if (meta instanceof PotionMeta) {
            PotionMeta potionMeta = (PotionMeta) meta;
            potionMeta.setMainEffect(potionEffect);
        }
        return this;
    }

    public NeonItemStack setOwner(String owner) {
        SkullMeta headMeta = (SkullMeta) meta;
        headMeta.setOwner(owner);

        item.setItemMeta(meta);
        return this;
    }

    public NeonItemStack setTexture(String url) {
        SkullMeta headMeta = (SkullMeta) meta;
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field field = headMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(headMeta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        item.setItemMeta(headMeta);
        return this;
    }

    public NeonItemStack setTexture(String url, String uuid) {
        SkullMeta headMeta = (SkullMeta) meta;
        GameProfile profile = new GameProfile(UUID.fromString(uuid), null);
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field field = headMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(headMeta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        item.setItemMeta(headMeta);
        return this;
    }

    public NeonItemStack setNBTString(String key, String value) {
        neonNBT.setString(key, value);
        return this;
    }

    public NeonItemStack setNBTInt(String key, int value) {
        neonNBT.setInt(key, value);
        return this;
    }

    public NeonItemStack setNBTDouble(String key, double value) {
        neonNBT.setDouble(key, value);
        return this;
    }

    public NeonItemStack setNBTBoolean(String key, boolean value) {
        neonNBT.setBoolean(key, value);
        return this;
    }

    public NeonItemStack removeNBT(String key) {
        neonNBT.remove(key);
        return this;
    }

    public String getNBTString(String key) {
        return neonNBT.getString(key);
    }

    public int getNBTInt(String key) {
        return neonNBT.getInt(key);
    }

    public double getNBTDouble(String key) {
        return neonNBT.getDouble(key);
    }

    public boolean getNBTBoolean(String key) {
        return neonNBT.getBoolean(key);
    }

    public ItemStack getItem() {
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getItemNBT() {
        item.setItemMeta(meta);
        return neonNBT.apply(item);
    }

}
