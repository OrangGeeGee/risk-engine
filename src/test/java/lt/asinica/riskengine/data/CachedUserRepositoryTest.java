package lt.asinica.riskengine.data;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CachedUserRepositoryTest {

    private CachedUserRepository cachedUserRepository;
    private SampleUserRepository sampleUserRepository;

    @Before
    public void setUp() {
        sampleUserRepository = spy(new SampleUserRepository());
        cachedUserRepository = new CachedUserRepository(sampleUserRepository);
    }

    @Test
    public void findOne_sameEntryAlwaysCached() {
        cachedUserRepository.findOne(100);
        cachedUserRepository.findOne(100);
        verify(sampleUserRepository, times(1)).findOne(100);
    }

    @Test
    public void findOne_cacheLastAccessedEviction() {
        for(int i = 0; i < 300; i++) {
            cachedUserRepository.findOne(100 + i);
        }
        // verify elements were loaded into cache once
        verify(sampleUserRepository, times(1)).findOne(100);
        // cache is now full, oldest accessed member 100 should be evicted
        cachedUserRepository.findOne(400);
        // cache should be loaded with 100 again
        cachedUserRepository.findOne(100);
        verify(sampleUserRepository, times(2)).findOne(100);
    }

    @Test
    public void save() {
        cachedUserRepository.save(new User(100, null));
        verify(sampleUserRepository, times(1)).save(any());
    }
}