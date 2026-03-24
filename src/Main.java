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

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }

    public abstract void displayDetails();
}

// Concrete Room Classes
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000);
    }

    public void displayDetails() {
        System.out.println(getRoomType() + " | Beds: " + getBeds() + " | Price: ₹" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500);
    }

    public void displayDetails() {
        System.out.println(getRoomType() + " | Beds: " + getBeds() + " | Price: ₹" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000);
    }

    public void displayDetails() {
        System.out.println(getRoomType() + " | Beds: " + getBeds() + " | Price: ₹" + getPrice());
    }
}

/**
 * Centralized Inventory (same as UC3)
 */
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0); // intentionally 0 to test filtering
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // No update method used here → READ-ONLY scenario
}

/**
 * Search Service (Read-only)
 */
class SearchService {

    public void searchAvailableRooms(List<Room> rooms, RoomInventory inventory) {

        System.out.println("===== Available Rooms =====");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getRoomType());

            // Filter unavailable rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println("----------------------");
            }
        }
    }
}

/**
 * Main Application
 */
public class Main {

    public static void main(String[] args) {

        // Create room objects
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Inventory (centralized state)
        RoomInventory inventory = new RoomInventory();

        // Search Service (read-only)
        SearchService searchService = new SearchService();

        // Perform search
        searchService.searchAvailableRooms(rooms, inventory);

        System.out.println("===== End (No State Changed) =====");
    }
}