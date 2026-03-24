import java.util.*;

/**
 * Reservation class (booking request)
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
 * Room Inventory (centralized)
 */
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decreaseAvailability(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

/**
 * Booking Queue (FIFO)
 */
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * Booking Service (Allocation + Confirmation)
 */
class BookingService {

    private Set<String> assignedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    // Process all requests
    public void processBookings(BookingQueue queue, RoomInventory inventory) {

        System.out.println("===== Processing Bookings =====");

        int idCounter = 1;

        while (!queue.isEmpty()) {

            Reservation req = queue.getNextRequest();
            String type = req.getRoomType();

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                // Generate unique room ID
                String roomId = type.substring(0, 2).toUpperCase() + idCounter++;

                // Ensure uniqueness
                if (!assignedRoomIds.contains(roomId)) {
                    assignedRoomIds.add(roomId);

                    // Map room type → assigned IDs
                    roomAllocations.putIfAbsent(type, new HashSet<>());
                    roomAllocations.get(type).add(roomId);

                    // Decrease inventory
                    inventory.decreaseAvailability(type);

                    // Confirm booking
                    System.out.println("Confirmed: " + req.getGuestName() +
                            " | " + type +
                            " | Room ID: " + roomId);
                }

            } else {
                System.out.println("Rejected (No Availability): " +
                        req.getGuestName() + " | " + type);
            }
        }
    }

    public void displayAllocations() {
        System.out.println("\n===== Allocated Rooms =====");
        for (Map.Entry<String, Set<String>> entry : roomAllocations.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

/**
 * Main Application
 */
public class Main {

    public static void main(String[] args) {

        // Step 1: Create Queue
        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // will be rejected
        queue.addRequest(new Reservation("David", "Suite Room"));

        // Step 2: Inventory
        RoomInventory inventory = new RoomInventory();

        // Step 3: Booking Service
        BookingService service = new BookingService();

        // Step 4: Process FIFO bookings
        service.processBookings(queue, inventory);

        // Step 5: Show results
        service.displayAllocations();
        inventory.displayInventory();

        System.out.println("\n===== End =====");
    }
}