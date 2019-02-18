package lt.asinica.riskengine.kafka;

import lt.asinica.riskengine.services.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SettledListener {

    private WithdrawService withdrawService;

    @Autowired
    public SettledListener(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @KafkaListener(id = "risk-engine", topics = "settled")
    public void listen(SettledMessage message) {
        withdrawService.settle(message);
    }

}
