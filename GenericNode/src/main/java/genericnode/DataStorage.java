package genericnode;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class DataStorage {
    private static final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    public DataStorage() {
    }
    public static synchronized void put(String key, String value) {
        map.put(key, value);
    }

    public static synchronized String get(String key) {
        return map.getOrDefault(key, "not found");
    }

    public static synchronized void del(String key) {
        map.remove(key);
    }

    public static synchronized ArrayList<String> store() {
        ArrayList<String> arrayList = new ArrayList<>();
        for(String key : map.keySet()) {
            String entry = "key:" + key + ":value:" + map.get(key)+ ":";
            arrayList.add(entry);
        }
        return arrayList;
    }

    // Remove the servers that are no longer responding
    public static synchronized void remove(long threshold) {
        map.entrySet().removeIf(entry -> Long.parseLong(entry.getValue()) < threshold);
    }
}
