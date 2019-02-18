package lt.asinica.riskengine.services;

import lt.asinica.riskengine.data.Reservation;
import lt.asinica.riskengine.data.User;
import lt.asinica.riskengine.data.UserRepository;
import lt.asinica.riskengine.exceptions.AmountNotValid;
import lt.asinica.riskengine.exceptions.CurrencyNotSupported;
import lt.asinica.riskengine.exceptions.ReservationNotFound;
import lt.asinica.riskengine.kafka.SettledMessage;
import lt.asinica.riskengine.rest.WithdrawalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

import static lt.asinica.riskengine.rest.WithdrawalStatus.BalanceStatus.INSUFFICIENT_BALANCE;
import static lt.asinica.riskengine.rest.WithdrawalStatus.BalanceStatus.SUFFICIENT_BALANCE;

@Service
public class WithdrawService {

    private UserRepository userRepository;

    @Autowired
    public WithdrawService(@Qualifier("cachedUserRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public WithdrawalStatus withdraw(Integer userId, String token, BigDecimal requestedAmount) {
        if(requestedAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new AmountNotValid();

        User user = userRepository.findOne(userId);
        BigDecimal spendable = determineSpendableBalance(user, token);
        if(spendable.compareTo(requestedAmount) >= 0) {
            Reservation reservation = new Reservation(UUID.randomUUID().toString(), token, requestedAmount);
            user.addReservation(reservation);
            userRepository.save(user);
            return new WithdrawalStatus(SUFFICIENT_BALANCE, reservation.getId());
        } else {
            return new WithdrawalStatus(INSUFFICIENT_BALANCE, null);
        }
    }

    private BigDecimal determineSpendableBalance(User user, String token) {
        if(!user.getAvailableBalances().containsKey(token))
            throw new CurrencyNotSupported();

        BigDecimal available = user.getAvailableBalances().get(token);
        BigDecimal reserved = user.getReservations().stream()
                .filter(reservation -> reservation.getToken().equals(token))
                .map(Reservation::getReservedAmount)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        return available.subtract(reserved);
    }

    /**
     * synchronized block works here only because our CachedUserRepository ensures the same object User references are used via threads.
     * This way we make sure no two updates to user balance are settled at the same time.
     */
    public void settle(SettledMessage message) {
        User user = userRepository.findOne(message.getUserId());
        synchronized (user) {
            Reservation reservation = user.getReservations().stream()
                    .filter(res -> res.getId().equals(message.getReservationId()))
                    .findAny().orElseThrow(ReservationNotFound::new);
            user.removeReservation(reservation);

            // many edge cases with sold token:
            // 1) possible token type mismatches, 2) negative balance if the sold amount was higher than originally reserved
            BigDecimal soldTokenBalance = user.getAvailableBalances().getOrDefault(message.getSoldToken(), BigDecimal.ZERO);
            user.getAvailableBalances().put(message.getSoldToken(), soldTokenBalance.subtract(message.getSoldQuantity()));

            BigDecimal boughtTokenBalance = user.getAvailableBalances().getOrDefault(message.getBoughtToken(), BigDecimal.ZERO);
            user.getAvailableBalances().put(message.getBoughtToken(), boughtTokenBalance.add(message.getBoughtQuantity()));

            userRepository.save(user);
        }
    }
}
