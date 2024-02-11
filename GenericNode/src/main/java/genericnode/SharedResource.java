package genericnode;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SharedResource {
    private final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    //exception handling
    //access null key for PUT

    public synchronized void put(String key, String value) {
        map.put(key, value);
    }

    public synchronized String get(String key) {
        if(map.containsKey(key))
        {
            return map.get(key);
        }
        return "null value";
    }
    public synchronized void delete(String key) {
        map.remove(key);
    }
    public synchronized ArrayList<StringBuilder> store() {
        ArrayList<StringBuilder> responseArray = new ArrayList<>();
        if(map.isEmpty())
        {
            responseArray.add(new StringBuilder("empty"));
            return responseArray;
        }
        for(String key : map.keySet()) {
            StringBuilder response = new StringBuilder();
            responseArray.add(response.append("key:").append(key).append(":value:").append(map.get(key)));
        }
        return responseArray;
    }
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }
    public SharedResource() {
    }

}