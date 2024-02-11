package genericnode;

import java.util.AbstractMap.SimpleEntry;

public class ExtendedEntry<K, V> extends SimpleEntry<K, V> {
    private final String methodName;

    public ExtendedEntry(K key, V value, String methodName) {
        super(key, value);
        this.methodName = methodName;
    }
    public ExtendedEntry(SimpleEntry<K, V> se, String methodName) {
        super(se);
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }
}
