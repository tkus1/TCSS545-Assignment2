package genericnode;

public class LockableDataStorage extends DataStorage{
    private final KeyLockManager keyLockManager;

    public LockableDataStorage() {
        super();
        keyLockManager = new KeyLockManager();
    }
    public boolean isKeyLocked(String key) {
        return keyLockManager.isKeyLocked(key);
    }
    public void lockKey(String key) {
        keyLockManager.lockKey(key);
    }
    public void unlockKey(String key) {
        keyLockManager.unlockKey(key);
    }
}
