package lt.asinica.riskengine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RiskEngineApplicationTests {

	/**
	 * Would fail if you don't have Kafka instance running on 127.0.0.1:9092.
	 * An embedded Kafka could be started, but that bogs down otherwise fast unit tests quite a lot.
	 * This should be part of a different test sourceSet like integrationTests which would require a
	 * live environment with all runtime dependencies ready.
	 */
	@Test
	public void contextLoads() {}

}
