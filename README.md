# Airline Reservation System API ✈️

A simple yet comprehensive Airline Reservation System built using **Spring Boot** and **Java 17**. This project provides a complete RESTful API for managing airports, flights, and user bookings, secured with JWT authentication.

## 🚀 Tech Stack
- **Backend Framework:** Spring Boot 3.x
- **Language:** Java 17
- **Database:** PostgreSQL (via Docker Compose)
- **ORM:** Spring Data JPA / Hibernate
- **Security:** Spring Security & JWT (JSON Web Token)
- **API Documentation:** Springdoc OpenAPI (Swagger UI)
- **Other Tools:** Maven, Lombok, Validation API

## 💡 Key Features
- **Authentication & Authorization:** Secure user registration and login using JWT. Role-based access control (RBAC) separates standard users (`ROLE_USER`) from administrators (`ROLE_ADMIN`).
- **Airports Management:** Admins can add new airports. Users can view all available airports.
- **Flights Management:** Flight scheduling including departure/arrival times, pricing, and available seats. Only admins can create, update, or delete flights.
- **Flight Search:** Easily search for upcoming flights between two specific airports.
- **Booking System:** Smart booking logic that automatically decreases available seats upon booking, and restores them if a booking is cancelled.
- **Global Exception Handling:** Custom `@ControllerAdvice` to handle errors gracefully and return clean JSON error messages.
- **Data Transfer Objects (DTO):** Strict separation between database entities and API payloads for better security and clean architecture.

## 🛠️ How to Run the Project

### 1. Database Setup
This project includes a `docker-compose.yml` file. To spin up the PostgreSQL database, run:
```bash
docker compose up -d
```
*(Make sure port 5432 is available on your machine).*

### 2. Run the Spring Boot Application
In the project root directory, start the server using Maven:
```bash
./mvnw spring-boot:run
```

### 3. Explore the API (Swagger)
Once the application is running, open your browser and navigate to the Swagger UI to test all endpoints:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 📌 API Endpoints

### Auth
- `POST /api/auth/register` - Create a new user account
- `POST /api/auth/login` - Authenticate and get a JWT token

### Airports
- `GET /api/airports` - Get all airports
- `GET /api/airports/{id}` - Get a specific airport
- `POST /api/airports` - Add a new airport (Admin only)

### Flights
- `GET /api/flights` - Get all flights
- `GET /api/flights/search?from={code}&to={code}` - Search for a flight
- `POST /api/flights` - Add a new flight (Admin only)
- `PUT /api/flights/{id}` - Update a flight (Admin only)
- `DELETE /api/flights/{id}` - Delete a flight (Admin only)

### Bookings
- `POST /api/bookings` - Create a new flight booking
- `GET /api/bookings/my` - View my bookings
- `DELETE /api/bookings/{id}` - Cancel a booking (Only the owner can cancel)

---
*Developed as a practical demonstration of modern backend development using Spring Boot.*
