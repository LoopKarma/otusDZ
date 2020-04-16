package cachehw;

import java.util.ArrayList;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    WeakHashMap<K, V> map = new WeakHashMap<>();
    ArrayList<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        if (!map.containsKey(key)) {
            map.put(key, value);
            listeners.forEach(l -> l.notify(key, value, "put"));
        }
    }

    @Override
    public void remove(K key) {
        if (map.containsKey(key)) {
            listeners.forEach(l -> l.notify(key, map.get(key), "remove"));
            map.remove(key);
        }
    }

    @Override
    public V get(K key) {
        if (map.containsKey(key)) {
            V v = map.get(key);
            listeners.forEach(l -> l.notify(key, v, "get"));
            return v;
        }
        return null;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
