import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class OnlineReservationSystem {

    static class User {
        String username, password;
        User(String u, String p) {
            username = u;
            password = p;
        }
    }

    static class Reservation {
        String pnr, username, name, trainNo, trainName, classType, date, fromPlace, toPlace;
        Reservation(String pnr, String username, String name, String trainNo, String trainName,
                    String classType, String date, String fromPlace, String toPlace) {
            this.pnr = pnr;
            this.username = username;
            this.name = name;
            this.trainNo = trainNo;
            this.trainName = trainName;
            this.classType = classType;
            this.date = date;
            this.fromPlace = fromPlace;
            this.toPlace = toPlace;
        }

        public String toString() {
            return String.join(",", pnr, username, name, trainNo, trainName, classType, date, fromPlace, toPlace);
        }

        public static Reservation fromString(String line) {
            String[] p = line.split(",", -1);
            if (p.length != 9) return null;
            return new Reservation(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8]);
        }
    }

    static final String USERS_FILE = "users.txt";
    static final String RESERVATIONS_FILE = "reservations.txt";

    static Map<String, User> users = new HashMap<>();
    static List<Reservation> reservations = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadUsers();

        if (!users.containsKey("avocado")) {
            users.put("avocado", new User("avocado", "2525"));
            saveUsers();
            System.out.println("Default user 'avocado' with password '2525' added.");
        }

        loadReservations();

        while (true) {
            System.out.println("\n--- ONLINE RESERVATION SYSTEM ---");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            String ch = sc.nextLine();

            switch (ch) {
                case "1":
                    User user = login();
                    if (user != null) userMenu(user);
                    break;
                case "2":
                    register();
                    break;
                case "3":
                    saveReservations();
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    static void register() {
        System.out.print("Choose username: ");
        String username = sc.nextLine().trim();

        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }

        System.out.print("Choose password: ");
        String password = sc.nextLine().trim();

        users.put(username, new User(username, password));
        saveUsers();
        System.out.println("Registration successful! You can now login.");
    }

    static User login() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            System.out.println("Login successful!");
            return user;
        } else {
            System.out.println("Invalid credentials.");
            return null;
        }
    }

    static void userMenu(User user) {
        while (true) {
            System.out.println("\n--- Welcome, " + user.username + " ---");
            System.out.println("1. Make Reservation");
            System.out.println("2. Cancel Reservation");
            System.out.println("3. View My Reservations");
            System.out.println("4. Logout");
            System.out.print("Choose option: ");
            String ch = sc.nextLine();

            switch (ch) {
                case "1":
                    makeReservation(user);
                    break;
                case "2":
                    cancelReservation(user);
                    break;
                case "3":
                    viewReservations(user);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    static void makeReservation(User user) {
        System.out.print("Your Name: ");
        String name = sc.nextLine();
        System.out.print("Train Number: ");
        String trainNo = sc.nextLine();
        System.out.print("Train Name: ");
        String trainName = sc.nextLine();
        System.out.print("Class Type: ");
        String classType = sc.nextLine();
        System.out.print("Date of Journey (YYYY-MM-DD): ");
        String date = sc.nextLine();

        if (!isValidDate(date)) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
            return;
        }

        System.out.print("From (Place): ");
        String fromPlace = sc.nextLine();
        System.out.print("To (Destination): ");
        String toPlace = sc.nextLine();

        String pnr = generatePNR();
        Reservation r = new Reservation(pnr, user.username, name, trainNo, trainName, classType, date, fromPlace, toPlace);
        reservations.add(r);
        saveReservations();
        System.out.println("Reservation successful! Your PNR: " + pnr);
    }

    static void cancelReservation(User user) {
        System.out.print("Enter PNR to cancel: ");
        String pnr = sc.nextLine();
        Reservation found = null;

        for (Reservation r : reservations) {
            if (r.pnr.equals(pnr) && r.username.equals(user.username)) {
                found = r;
                break;
            }
        }

        if (found != null) {
            System.out.println("Reservation found:");
            printReservation(found);
            System.out.print("Are you sure you want to cancel? (y/n): ");
            String confirm = sc.nextLine();
            if (confirm.equalsIgnoreCase("y")) {
                reservations.remove(found);
                saveReservations();
                System.out.println("Reservation cancelled.");
            } else {
                System.out.println("Cancellation aborted.");
            }
        } else {
            System.out.println("No reservation found with that PNR for your account.");
        }
    }

    static void viewReservations(User user) {
        boolean any = false;
        for (Reservation r : reservations) {
            if (r.username.equals(user.username)) {
                printReservation(r);
                any = true;
            }
        }
        if (!any) System.out.println("No reservations found.");
    }

    static void printReservation(Reservation r) {
        System.out.println("PNR: " + r.pnr + ", Name: " + r.name + ", Train: " + r.trainNo + " - " + r.trainName +
                ", Class: " + r.classType + ", Date: " + r.date + ", From: " + r.fromPlace + ", To: " + r.toPlace);
    }

    static String generatePNR() {
        return "PNR" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    static void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length == 2) users.put(p[0], new User(p[0], p[1]));
            }
        } catch (IOException e) {
            System.out.println("Users file not found. A new one will be created.");
        }
    }

    static void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users.values()) {
                pw.println(u.username + "," + u.password);
            }
        } catch (IOException e) {
            System.out.println("Error saving users.");
            e.printStackTrace();
        }
    }

    static void loadReservations() {
        try (BufferedReader br = new BufferedReader(new FileReader(RESERVATIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Reservation r = Reservation.fromString(line);
                if (r != null) reservations.add(r);
            }
        } catch (IOException e) {
            System.out.println("Reservations file not found. A new one will be created.");
        }
    }

    static void saveReservations() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Reservation r : reservations) {
                pw.println(r.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving reservations.");
            e.printStackTrace();
        }
    }
}

