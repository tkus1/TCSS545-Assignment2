package genericnode;

import java.util.concurrent.ConcurrentHashMap;

class SharedResource {
    private final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    //exception handling
    //access null key for PUT

    public void put(String key, String value) {
        map.put(key, value);
    }

    public String get(String key) {
        if(map.containsKey(key))
        {
            return map.get(key);
        }
        return "null value";
    }
    public void delete(String key) {
        map.remove(key);
    }
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }
    public SharedResource() {
    }
    //todo implement store that returns all items in the map
}