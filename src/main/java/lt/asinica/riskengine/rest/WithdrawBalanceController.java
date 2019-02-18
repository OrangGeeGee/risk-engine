package lt.asinica.riskengine.rest;

import lt.asinica.riskengine.services.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController("/")
public class WithdrawBalanceController {

    private WithdrawService withdrawService;

    @Autowired
    public WithdrawBalanceController(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @GetMapping("withdraw")
    public WithdrawalStatus withdraw(Integer userId, String token, BigDecimal requestedAmount) {
        return withdrawService.withdraw(userId, token, requestedAmount);
    }
}
