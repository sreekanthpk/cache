package sc.practice.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class CacheImpl<K, V>  implements Cache<K, V>{

    private final Function<K,V> loadCache;
    private final Map<K, V> cache;
    private final long concurrencyLevel;
    private final Map<Long, Lock> stripeLocks;

    public CacheImpl(Function<K, V> loadCache, long concurrencyLevel){
        Objects.requireNonNull(loadCache);
        this.loadCache = loadCache;
        this.cache = new HashMap<>();
        this.concurrencyLevel = concurrencyLevel;
        this.stripeLocks = new ConcurrentHashMap<>();
    }

    @Override
    public V get(K key) {
        long lockId = key.hashCode()%concurrencyLevel;
        Lock l = stripeLocks.computeIfAbsent(lockId, k -> new ReentrantLock());
        l.tryLock();
        try {
            V v = null;
            V newValue = null;
            return ((v = cache.get(key)) == null &&
                    (newValue = loadCache.apply(key)) != null &&
                    (v = cache.putIfAbsent(key, newValue)) == null) ? newValue : v;
        } finally {
            l.unlock();
        }

    }
}