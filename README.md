# 😊 Online Reservation System (Java)

## 📌 Overview
This project is a **console-based railway reservation system** built in Java. I
t lets users sign up, log in, and handle train bookings with options to **book tickets, cancel them, and check their bookings**. All data is saved in text files so it stays even after the program is closed.r>
---

## 🚇 Features
- **User Management**
- Create a new account
- Log in using a username and password
- Default user: `avocado / 2525`

- **Reservation System**
- Book a ticket by giving passenger name, train number, train name, class type, travel date, and start and end points
- Automatically creates a **unique PNR number** for each booking

- **Cancellation**
- Cancel a reservation using the PNR number
- Only the person who booked the ticket can cancel it

- **View Reservations**
- See all bookings that the logged-in user has made

- **File Storage**
- `users.txt` → Keeps track of user names and passwords
- `reservations.txt` → Keeps all reservation details

---

## 📋 Example Data

### users.txt
```
username,password
avocado,2525
john,1234
```

### reservations.txt
```
PNR1692982038921,avocado,John,12045,Shatabdi Express,AC,2025-09-01,Delhi,Mumbai
PNR1692982071259,john,Mary,11015,Karnataka Express,Sleeper,2025-09-02,Bangalore,Pune
```

---

## 🧱 How to Run
1. Comp
ile:
```bash
javac OnlineReservationSystem.java
```
2. Run:

```bash
java OnlineReservationSystem
```

---

## 🧰 Technologies Used
- Java (JDK 8 or newer)
- File Input/Output (BufferedReader, PrintWriter)
- Collections (Map, List)
- Java Time API (LocalDate)

---

## 🛠️ Future Improvements
- Use password encryption (currently stored as plain text)
- Add a GUI using JavaFX or Swing
- Connect to a database like MySQL or PostgreSQL
- Include ticket price calculation and a fake payment system
