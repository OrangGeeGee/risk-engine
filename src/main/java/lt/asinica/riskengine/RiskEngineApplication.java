package lt.asinica.riskengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

@SpringBootApplication
public class RiskEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiskEngineApplication.class, args);
	}

}
