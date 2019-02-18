package lt.asinica.riskengine.data;

import lt.asinica.riskengine.exceptions.UserDoesNotExist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SampleUserRepositoryTest {

    private SampleUserRepository sampleUserRepository;

    @Before
    public void setUp() {
        sampleUserRepository = new SampleUserRepository();
    }

    @Test
    public void findOne_validUser() {
        User user = sampleUserRepository.findOne(100);
        assertEquals(100, user.getId().intValue());
        assertEquals(3128.39F, user.getAvailableBalances().get("EUR").floatValue(), 0.001F);
    }

    @Test(expected = UserDoesNotExist.class)
    public void findOne_invalidUser() {
        sampleUserRepository.findOne(0);
    }
}