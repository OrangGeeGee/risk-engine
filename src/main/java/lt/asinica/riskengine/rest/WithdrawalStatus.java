package lt.asinica.riskengine.rest;

public class WithdrawalStatus {
    public enum BalanceStatus {
        INSUFFICIENT_BALANCE,
        SUFFICIENT_BALANCE
    }

    private BalanceStatus balance;
    private String reservationId;

    public WithdrawalStatus(BalanceStatus balance, String reservationId) {
        this.balance = balance;
        this.reservationId = reservationId;
    }

    public BalanceStatus getBalance() {
        return balance;
    }

    public String getReservationId() {
        return reservationId;
    }
}
