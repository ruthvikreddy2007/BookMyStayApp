import java.util.*;

/**
 * Reservation class (already confirmed booking)
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

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * Add-On Service class
 */
class Service {
    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void display() {
        System.out.println(serviceName + " (₹" + cost + ")");
    }
}

/**
 * Add-On Service Manager
 */
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<Service>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, Service service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation: " + reservationId);

        List<Service> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        double total = 0;

        for (Service s : services) {
            s.display();
            total += s.getCost();
        }

        System.out.println("Total Add-On Cost: ₹" + total);
    }
}

/**
 * Main Application
 */
public class Main {

    public static void main(String[] args) {

        // Step 1: Existing reservation (from UC6)
        Reservation r1 = new Reservation("SI1", "Alice", "Single Room");

        // Step 2: Create services
        Service wifi = new Service("WiFi", 200);
        Service breakfast = new Service("Breakfast", 300);
        Service spa = new Service("Spa", 800);

        // Step 3: Service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Step 4: Add services to reservation
        manager.addService(r1.getReservationId(), wifi);
        manager.addService(r1.getReservationId(), breakfast);
        manager.addService(r1.getReservationId(), spa);

        // Step 5: Display services and cost
        manager.displayServices(r1.getReservationId());

        System.out.println("\nNote: Booking & inventory remain unchanged.");
    }
}