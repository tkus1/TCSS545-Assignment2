package genericnode;

import java.util.concurrent.ConcurrentHashMap;

class SharedResource {
    private ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    public void put(String key, String value) {
        map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }
    public void delete(String key) {
        map.remove(key);
    }
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }
    public SharedResource() {
    }

}