package lt.asinica.riskengine.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * It's a very simple mem cache implementation .
 * In real world app Guava, EhCache or similar is preferable. Not reinventing the wheel with caches should be the choice.
 * The task description was curious about cache eviction strategy, thus this reinvented wheel was born..
 */
@Repository
public class CachedUserRepository implements UserRepository {
    private static final int MAX_CACHE_SIZE = 300;

    private ConcurrentHashMap<Integer, CachedObject<User>> concurrentHashMap;
    private SampleUserRepository sampleUserRepository;

    @Autowired
    public CachedUserRepository(SampleUserRepository sampleUserRepository) {
        this.sampleUserRepository = sampleUserRepository;
        this.concurrentHashMap = new ConcurrentHashMap<>();
    }

    /**
     * synchronized helps in a situation where several threads from API calls and/or received Kafka messages
     * all try to get the same user's object at the same time. This ensures the reference to the object is the same
     * across threads. It is slows things down for multithreaded scenario though, so a proper Cache would be the way to go.
     */
    @Override
    public synchronized User findOne(Integer id) {
        if(concurrentHashMap.containsKey(id)) {
            return concurrentHashMap.get(id).get();
        } else {
            if(concurrentHashMap.size() >= MAX_CACHE_SIZE) {
                removeOldest();
            }
            User found = sampleUserRepository.findOne(id);
            concurrentHashMap.put(id, new CachedObject<>(found));
            return found;
        }
    }

    private void removeOldest() {
        concurrentHashMap.values().stream().min(Comparator.comparing(CachedObject::getLastAccessed))
            .ifPresent(oldest -> concurrentHashMap.remove(oldest.get().getId()));
    }

    @Override
    public void save(User user) {
        sampleUserRepository.save(user);
    }

    private class CachedObject<T> {
        private T object;
        private long lastAccessed;

        CachedObject(T object) {
            this.touch();
            this.object = object;
        }

        T get() {
            this.touch();
            return object;
        }

        long getLastAccessed() {
            return lastAccessed;
        }

        private void touch() {
            this.lastAccessed = System.currentTimeMillis();
        }
    }
}
