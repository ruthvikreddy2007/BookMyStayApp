import java.util.*;

/**
 * Abstract Room class
 */
abstract class Room {
    private String roomType;
    private int beds;
    private double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public abstract void displayDetails();
}

// Room Types
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000);
    }

    public void displayDetails() {
        System.out.println(getRoomType());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500);
    }

    public void displayDetails() {
        System.out.println(getRoomType());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000);
    }

    public void displayDetails() {
        System.out.println(getRoomType());
    }
}

/**
 * Reservation class → represents booking request
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested: " + roomType);
    }
}

/**
 * Booking Request Queue (FIFO)
 */
class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\n===== Booking Queue (FIFO) =====");
        for (Reservation r : queue) {
            r.display();
        }
    }
}

public class Main {

    public static void main(String[] args) {

        // Create booking queue
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate incoming requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Double Room"));

        // Display queue (FIFO order preserved)
        bookingQueue.displayQueue();

        System.out.println("\nNote: No inventory update is done here.");
    }
}