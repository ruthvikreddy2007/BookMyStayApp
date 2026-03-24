import java.util.*;

/**
 * Reservation (Booking Request)
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
}

/**
 * Thread-safe Inventory
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    // Critical section (synchronized)
    public synchronized boolean bookRoom(String type) {
        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void display() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }
}

/**
 * Shared Booking Queue
 */
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void add(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation get() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * Worker Thread (Booking Processor)
 */
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {
            Reservation r;

            // Critical section → fetch request
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.get();
            }

            // Process booking safely
            boolean success = inventory.bookRoom(r.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " booked for " + r.getGuestName() +
                        " (" + r.getRoomType() + ")");
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " failed for " + r.getGuestName() +
                        " (No availability)");
            }
        }
    }
}

/**
 * Main Class
 */
public class Main {

    public static void main(String[] args) {

        // Shared resources
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Simulate concurrent requests
        queue.add(new Reservation("Alice", "Single Room"));
        queue.add(new Reservation("Bob", "Single Room"));
        queue.add(new Reservation("Charlie", "Single Room")); // exceeds
        queue.add(new Reservation("David", "Double Room"));
        queue.add(new Reservation("Eve", "Double Room")); // exceeds

        // Create multiple threads
        Thread t1 = new BookingProcessor(queue, inventory);
        Thread t2 = new BookingProcessor(queue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final inventory
        inventory.display();

        System.out.println("\n===== End =====");
    }
}