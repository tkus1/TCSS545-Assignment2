package genericnode;

import java.util.concurrent.ConcurrentHashMap;

public class KeyLockManager {
    private final ConcurrentHashMap<String, Boolean> keyLocks = new ConcurrentHashMap<>();

    public void lockKey(String key) {
        keyLocks.put(key, true);
    }

    public void unlockKey(String key) {
        keyLocks.put(key, false);
    }

    public boolean isKeyLocked(String key) {
        return keyLocks.getOrDefault(key, false);
    }
}
