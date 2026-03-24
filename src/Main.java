import java.util.*;

/**
 * Custom Exception for Invalid Booking
 */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * Reservation class
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
 * Room Inventory with validation protection
 */
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0); // zero to test validation
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void decreaseAvailability(String roomType) throws InvalidBookingException {
        int available = getAvailability(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for " + roomType);
        }

        inventory.put(roomType, available - 1);
    }
}

/**
 * Validator class (Fail-Fast)
 */
class BookingValidator {

    private static final Set<String> validRoomTypes =
            new HashSet<>(Arrays.asList("Single Room", "Double Room", "Suite Room"));

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        // Validate room type
        if (!validRoomTypes.contains(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "Room not available: " + r.getRoomType());
        }
    }
}

/**
 * Booking Service with exception handling
 */
class BookingService {

    public void bookRoom(Reservation r, RoomInventory inventory) {
        try {
            // Step 1: Validate (Fail-Fast)
            BookingValidator.validate(r, inventory);

            // Step 2: Process booking
            inventory.decreaseAvailability(r.getRoomType());

            // Step 3: Confirm
            System.out.println("Booking Confirmed: " +
                    r.getGuestName() + " -> " + r.getRoomType());

        } catch (InvalidBookingException e) {
            // Graceful error handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

/**
 * Main Class
 */
public class Main {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService();

        // Test cases (valid + invalid)
        Reservation r1 = new Reservation("Alice", "Single Room"); // valid
        Reservation r2 = new Reservation("", "Double Room");      // invalid name
        Reservation r3 = new Reservation("Bob", "Luxury Room");   // invalid type
        Reservation r4 = new Reservation("Charlie", "Suite Room");// no availability

        // Process bookings
        service.bookRoom(r1, inventory);
        service.bookRoom(r2, inventory);
        service.bookRoom(r3, inventory);
        service.bookRoom(r4, inventory);

        System.out.println("\nSystem continues running safely...");
    }
}