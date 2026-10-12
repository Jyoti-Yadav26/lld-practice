package parking_lot_01.Version4;

public class UpiPayment implements PaymentProcessor{
    @Override
    public boolean processPayment(String ticketId, long amount) {
        if(ticketId==null || ticketId.isBlank()) return false;

        if(amount<0) return false;
        System.out.println("Processing UPI payment for ticket: "+ticketId);
        System.out.println("Amount: Rs. "+amount);

        return true;
    }
}
