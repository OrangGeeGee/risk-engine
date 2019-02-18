package lt.asinica.riskengine.kafka;

import java.math.BigDecimal;

public class SettledMessage {
    private Integer userId;
    private String reservationId;
    private String boughtToken;
    private BigDecimal boughtQuantity;
    private String soldToken;
    private BigDecimal soldQuantity;

    public SettledMessage() {
    }

    public SettledMessage(Integer userId, String reservationId, String boughtToken, BigDecimal boughtQuantity, String soldToken, BigDecimal soldQuantity) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.boughtToken = boughtToken;
        this.boughtQuantity = boughtQuantity;
        this.soldToken = soldToken;
        this.soldQuantity = soldQuantity;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getBoughtToken() {
        return boughtToken;
    }

    public BigDecimal getBoughtQuantity() {
        return boughtQuantity;
    }

    public String getSoldToken() {
        return soldToken;
    }

    public BigDecimal getSoldQuantity() {
        return soldQuantity;
    }

}
