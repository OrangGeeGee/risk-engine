package lt.asinica.riskengine.data;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class User {
    private Integer id;
    private Map<String, BigDecimal> availableBalances;
    private List<Reservation> reservations;

    public User(Integer id, Map<String, BigDecimal> availableBalances) {
        this.id = id;
        this.availableBalances = availableBalances;
        this.reservations = new LinkedList<>();
    }

    public Integer getId() {
        return id;
    }

    public Map<String, BigDecimal> getAvailableBalances() {
        return availableBalances;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }
}
