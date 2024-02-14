package genericnode;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class DataStorage {
    private static final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    public static void put(String key, String value) {
        map.put(key, value);
    }

    public static String get(String key) {
        return map.getOrDefault(key, "not found");
    }

    public static void del(String key) {
        map.remove(key);
    }

    public static ArrayList<String> store() {
        ArrayList<String> arrayList = new ArrayList<>();
        for(String key : map.keySet()) {
            String entry = "key:" + key + ":value:" + map.get(key)+ ":";
            arrayList.add(entry);
        }
        return arrayList;
    }
}
