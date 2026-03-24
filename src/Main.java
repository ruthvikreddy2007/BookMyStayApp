import java.io.*;
import java.util.*;

/**
 * Reservation class (Serializable)
 */
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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
 * System State (Inventory + Booking History)
 */
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> history;

    public SystemState(Map<String, Integer> inventory, List<Reservation> history) {
        this.inventory = inventory;
        this.history = history;
    }
}

/**
 * Persistence Service
 */
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("No saved state found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading state. Starting safe mode.");
        }

        // Return default state if failure
        return new SystemState(new HashMap<>(), new ArrayList<>());
    }
}

/**
 * Main Class
 */
public class Main {

    public static void main(String[] args) {

        PersistenceService service = new PersistenceService();

        // Step 1: Load previous state (Recovery)
        SystemState state = service.load();

        // If first run, initialize default data
        if (state.inventory.isEmpty()) {
            state.inventory.put("Single Room", 2);
            state.inventory.put("Double Room", 1);
        }

        // Simulate booking
        Reservation r1 = new Reservation("SI1", "Alice", "Single Room");
        state.history.add(r1);

        // Update inventory
        state.inventory.put("Single Room",
                state.inventory.get("Single Room") - 1);

        // Display current state
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> e : state.inventory.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }

        System.out.println("\nBooking History:");
        for (Reservation r : state.history) {
            r.display();
        }

        // Step 2: Save state (before shutdown)
        service.save(state);

        System.out.println("\n===== End =====");
    }
}