/**
 * Use Case 8: Booking History & Reporting
 */

import java.util.*;

// Reservation (confirmed booking)
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

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// Booking History (List → maintains order)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed reservation
    public void addReservation(Reservation r) {
        history.add(r);
    }

    // View all bookings
    public void displayHistory() {
        System.out.println("\n===== Booking History =====");
        for (Reservation r : history) {
            r.display();
        }
    }

    public List<Reservation> getHistory() {
        return history;
    }
}

// Reporting Service
class ReportService {

    // Generate summary report
    public void generateReport(List<Reservation> history) {

        System.out.println("\n===== Booking Report =====");

        Map<String, Integer> report = new HashMap<>();

        // Count bookings per room type
        for (Reservation r : history) {
            report.put(r.getRoomType(),
                    report.getOrDefault(r.getRoomType(), 0) + 1);
        }

        // Display report
        for (Map.Entry<String, Integer> entry : report.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " bookings");
        }

        System.out.println("Total Bookings: " + history.size());
    }
}

// Main Class
public class Main {

    public static void main(String[] args) {

        // Step 1: Booking history
        BookingHistory history = new BookingHistory();

        // Step 2: Simulate confirmed bookings (from UC6)
        history.addReservation(new Reservation("SI1", "Alice", "Single Room"));
        history.addReservation(new Reservation("SI2", "Bob", "Single Room"));
        history.addReservation(new Reservation("SU1", "Charlie", "Suite Room"));
        history.addReservation(new Reservation("DR1", "David", "Double Room"));

        // Step 3: Display history (ordered)
        history.displayHistory();

        // Step 4: Generate report
        ReportService reportService = new ReportService();
        reportService.generateReport(history.getHistory());

        System.out.println("\n===== End =====");
    }
}