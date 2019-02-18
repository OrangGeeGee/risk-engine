package lt.asinica.riskengine.data;

import java.math.BigDecimal;

public class Reservation {
    private String id;
    private String token;
    private BigDecimal reservedAmount;

    public Reservation(String id, String token, BigDecimal reservedAmount) {
        this.id = id;
        this.token = token;
        this.reservedAmount = reservedAmount;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }
}
