package parking_lot_01.Version4;

public interface PaymentProcessor {

    boolean processPayment(String ticketId, long amount);
}
