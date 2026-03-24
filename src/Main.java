import java.util.*;

/**
 * Reservation class (confirmed booking)
 */
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

/**
 * Inventory
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public void decrease(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void increase(String type) {
        inventory.put(type, inventory.get(type) + 1);
    }

    public void display() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }
}

/**
 * Booking History
 */
class BookingHistory {
    private Map<String, Reservation> history = new HashMap<>();

    public void add(Reservation r) {
        history.put(r.getReservationId(), r);
    }

    public Reservation get(String id) {
        return history.get(id);
    }

    public void remove(String id) {
        history.remove(id);
    }
}

/**
 * Cancellation Service with Stack (LIFO rollback)
 */
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancel(String reservationId,
                       BookingHistory history,
                       RoomInventory inventory) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Validate existence
        Reservation r = history.get(reservationId);
        if (r == null) {
            System.out.println("Cancellation Failed: Reservation not found");
            return;
        }

        // Step 1: push to stack (LIFO tracking)
        rollbackStack.push(reservationId);

        // Step 2: restore inventory
        inventory.increase(r.getRoomType());

        // Step 3: remove from history
        history.remove(reservationId);

        // Step 4: confirm cancellation
        System.out.println("Cancelled successfully: " + reservationId);
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

/**
 * Main Class
 */
public class Main {

    public static void main(String[] args) {

        // Setup
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService service = new CancellationService();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("SI1", "Alice", "Single Room");
        Reservation r2 = new Reservation("DR1", "Bob", "Double Room");

        history.add(r1);
        history.add(r2);

        // Inventory reduced after booking (simulate UC6)
        inventory.decrease("Single Room");
        inventory.decrease("Double Room");

        inventory.display();

        // Cancel bookings
        service.cancel("SI1", history, inventory);
        service.cancel("XYZ", history, inventory); // invalid case

        // Show final state
        inventory.display();
        service.displayRollbackStack();

        System.out.println("\n===== End =====");
    }
}