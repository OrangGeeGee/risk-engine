package lt.asinica.riskengine.services;

import lt.asinica.riskengine.data.SampleUserRepository;
import lt.asinica.riskengine.data.User;
import lt.asinica.riskengine.data.UserRepository;
import lt.asinica.riskengine.exceptions.AmountNotValid;
import lt.asinica.riskengine.exceptions.CurrencyNotSupported;
import lt.asinica.riskengine.exceptions.ReservationNotFound;
import lt.asinica.riskengine.kafka.SettledMessage;
import lt.asinica.riskengine.rest.WithdrawalStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static lt.asinica.riskengine.rest.WithdrawalStatus.BalanceStatus.INSUFFICIENT_BALANCE;
import static lt.asinica.riskengine.rest.WithdrawalStatus.BalanceStatus.SUFFICIENT_BALANCE;
import static org.junit.Assert.*;

public class WithdrawServiceTest {

    private WithdrawService withdrawService;
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository = new SampleUserRepository();
        withdrawService = new WithdrawService(userRepository);
    }

    @Test
    public void withdraw_sufficient() {
        WithdrawalStatus status = withdrawService.withdraw(100, "BTC", BigDecimal.ONE);
        assertEquals(SUFFICIENT_BALANCE, status.getBalance());
        assertNotNull(status.getReservationId());
    }

    @Test
    public void withdraw_insufficient() {
        WithdrawalStatus status = withdrawService.withdraw(100, "USD", BigDecimal.TEN);
        assertEquals(INSUFFICIENT_BALANCE, status.getBalance());
        assertNull(status.getReservationId());
    }

    @Test
    public void withdraw_balanceInsufficientAfterSeveralReservations() {
        WithdrawalStatus status1 = withdrawService.withdraw(101, "BCH", BigDecimal.TEN);
        WithdrawalStatus status2 = withdrawService.withdraw(101, "BCH", BigDecimal.TEN);
        assertEquals(SUFFICIENT_BALANCE, status1.getBalance());
        assertEquals(INSUFFICIENT_BALANCE, status2.getBalance());
    }

    @Test(expected = CurrencyNotSupported.class)
    public void withdraw_currencyNotSupported() {
        withdrawService.withdraw(100, "LTL", BigDecimal.ONE);
    }

    @Test(expected = AmountNotValid.class)
    public void withdraw_negativeValue() {
        withdrawService.withdraw(100, "BTC", new BigDecimal(-1));
    }

    @Test(expected = ReservationNotFound.class)
    public void settle_noReservation() {
        SettledMessage msg = new SettledMessage(100, "unkown-reservation-id", "USD", BigDecimal.ONE, "ETH", BigDecimal.ZERO);
        withdrawService.settle(msg);
    }

    @Test
    public void settle_success() {
        // in static test data user 100, has 81.807344 BTC and 136.152897 ETH
        WithdrawalStatus status = withdrawService.withdraw(100, "BTC", BigDecimal.ONE);
        SettledMessage msg = new SettledMessage(100, status.getReservationId(), "ETH", BigDecimal.TEN, "BTC", BigDecimal.ONE);
        withdrawService.settle(msg);

        User user = userRepository.findOne(100);
        assertThat(new BigDecimal("80.807344"), Matchers.comparesEqualTo(user.getAvailableBalances().get("BTC")));
        assertThat(new BigDecimal("146.152897"), Matchers.comparesEqualTo(user.getAvailableBalances().get("ETH")));
    }
}