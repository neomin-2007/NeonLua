package de.neomin.neonlua.utilities;

import org.luaj.vm2.LuaValue;
import java.util.HashMap;

public class NeonTags {
    private final HashMap<String, HashMap<String, LuaValue>> tags = new HashMap<>();

    public LuaValue getTag(String key, String mapKey) {
        final HashMap<String, LuaValue> map = tags.getOrDefault(key, new HashMap<>());
        return map.getOrDefault(mapKey, LuaValue.NIL);
    }

    public boolean hasTag(String key, String mapKey) {
        final HashMap<String, LuaValue> map = tags.getOrDefault(key, new HashMap<>());
        return map.containsKey(mapKey);
    }

    public void putTag(String key, String mapKey, LuaValue value) {
        final HashMap<String, LuaValue> map = tags.getOrDefault(key, new HashMap<>());
        map.put(mapKey, value);
        tags.put(key, map);
    }

    public void removeTag(String key, String mapKey) {
        final HashMap<String, LuaValue> map = tags.getOrDefault(key, new HashMap<>());
        map.remove(mapKey);
        tags.put(key, map);
    }
}
